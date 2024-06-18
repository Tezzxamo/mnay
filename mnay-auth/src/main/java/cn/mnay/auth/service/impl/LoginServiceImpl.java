package cn.mnay.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.mnay.api.model.dto.auth.Auditor;
import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.api.model.request.auth.HttpAuthReq;
import cn.mnay.api.model.request.auth.HttpLoginReq;
import cn.mnay.api.service.auth.LoginService;
import cn.mnay.api.service.auth.MemberService;
import cn.mnay.common.constant.RedisConstants;
import cn.mnay.common.enums.auth.MemberTypeEnum;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import cn.mnay.common.manager.MailManager;
import cn.mnay.common.model.dto.auth.MemberInfo;
import cn.mnay.common.model.dto.auth.SendMailDTO;
import cn.mnay.common.model.request.HttpEmailReq;
import cn.mnay.common.model.request.HttpIdReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MailManager mailManager;
    private final MemberService memberService;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public MemberDTO authLogin(HttpLoginReq httpLoginReq, boolean forceLogin) {
        MemberDTO memberDTO;
        // 1、获取登录用户信息：
        switch (httpLoginReq.getLoginType()) {
            case PASSWORD_PHONE, CAPTCHA_PHONE -> memberDTO = memberService.queryByPhone(httpLoginReq.getPhone());
            case PASSWORD_EMAIL, CAPTCHA_EMAIL -> memberDTO = memberService.queryByEmail(httpLoginReq.getEmail());
            default -> throw new BusinessException(CodeEnum.USER_LOGIN_ERROR);
        }

        // 2、判断用户是否被锁定(只要不被锁定就有权限登录)
        if (memberDTO.getLocked()) {
            log.warn("用户已经被锁定，请联系管理员!");
            throw new BusinessException(CodeEnum.USER_LOCKED_ERROR);
        }

        // 3、密码登录or验证码登录
        switch (httpLoginReq.getLoginType()) {
            case PASSWORD_PHONE, PASSWORD_EMAIL -> {
                // 密码不正确则报错
                if (!StrUtil.equals(memberDTO.getMemberPassword(), httpLoginReq.getPassword())) {
                    log.error("用户名或密码错误，请重新输入!");
                    throw new BusinessException(CodeEnum.USER_LOGIN_ERROR);
                }
                // 登录成功，更新登录时间,返回token
                if (forceLogin) {
                    // 如果强制登录，则将该ID其他已经登录的地方踢下线
                    StpUtil.logout(memberDTO.getId());
                }
                StpUtil.login(memberDTO.getId());
                memberService.updateLastLoginTime(memberDTO);
                memberDTO.setToken(StpUtil.getTokenValue());
            }
            case CAPTCHA_PHONE, CAPTCHA_EMAIL -> {
                // 验证码不正确则报错
                // fixme: 获取验证码
                String captcha = "123456";
                if (!StrUtil.equals(captcha, httpLoginReq.getCaptcha())) {
                    log.error("验证码错误，请重新输入!");
                    throw new BusinessException(CodeEnum.USER_LOGIN_ERROR);
                }
                // 登录成功，更新登录时间,返回token
                if (forceLogin) {
                    // 如果强制登录，则将该ID其他已经登录的地方踢下线
                    StpUtil.logout(memberDTO.getId());
                }
                StpUtil.login(memberDTO.getId());
                memberService.updateLastLoginTime(memberDTO);
                memberDTO.setToken(StpUtil.getTokenValue());
            }
        }

        return memberDTO;
    }

    @Override
    public Boolean authLogout(HttpIdReq httpIdReq) {
        // 如果是系统管理员，可以踢人下线，如果不是，则只能将自己登出
        MemberInfo memberInfo = Auditor.getCurrentMemberInfo();
        // 是自己，直接登出
        if (StrUtil.equals(memberInfo.getMemberId(), httpIdReq.getId())) {
            StpUtil.logout();
            return true;
        }
        // 不是自己，且自己是管理员（系统管理员，不是部门管理员、组织管理员），则可以踢下线
        if (memberInfo.getMemberType() == MemberTypeEnum.ADMIN) {
            // 不是自己，踢下线
            StpUtil.kickout(httpIdReq.getId());
            return true;
        }
        return false;
    }

    @Override
    public Boolean authResetPwdCaptcha(HttpEmailReq httpEmailReq) {
        // 断言存在，否则抛错
        Assert.isTrue(memberService.existsByMemberEmail(httpEmailReq.getEmail()), () -> new BusinessException(CodeEnum.USER_GET_ERROR));

        // 发送验证码
        SendMailDTO sendMailDTO = new SendMailDTO();
        sendMailDTO.setToEmail(httpEmailReq.getEmail());
        sendMailDTO.setSubject("重置密码验证码");
        sendMailDTO.setText("验证码：");
        mailManager.sendResetPwdMail(sendMailDTO);
        return null;
    }

    @Override
    public MemberDTO authResetPwd(HttpAuthReq httpAuthReq) {
        // 密码一致校验
        Assert.equals(httpAuthReq.getMemberPassword(), httpAuthReq.getMemberPassword(), () -> new BusinessException(CodeEnum.USER_PASSWORD_RESET_ERROR, "密码与确认密码不一致"));

        // 用户存在校验
        Assert.isTrue(memberService.existsByMemberEmail(httpAuthReq.getMemberEmail()), () -> new BusinessException(CodeEnum.USER_GET_ERROR));

        // 从redis中查询是否存在key及value
        String key = RedisConstants.RESET_PASSWORD + httpAuthReq.getMemberEmail();
        String value = Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(Object::toString)
                .orElseThrow(() -> new BusinessException(CodeEnum.USER_PASSWORD_RESET_ERROR, "验证码过期，请重新发送验证码"));

        // 判断验证码是否一致
        Assert.equals(httpAuthReq.getCaptcha(), value, () -> new BusinessException(CodeEnum.USER_PASSWORD_RESET_ERROR, "验证码不一致，请重新输入"));

        // 更改密码
        MemberDTO memberDTO = memberService.resetPassword(httpAuthReq);

        // 移除验证码缓存
        redisTemplate.delete(key);
        return memberDTO;
    }

    @Override
    public String getToken(MemberDTO memberDTO) {
        return StpUtil.getTokenValueByLoginId(memberDTO.getId());
    }
}
