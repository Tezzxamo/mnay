package cn.mnay.api.model.dto.auth;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.mnay.api.service.auth.MemberService;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import cn.mnay.common.model.dto.auth.MemberInfo;
import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * 用途：
 * <li>①：用于JPA审计功能</li>
 * <li>②：用于获取当前登录成员信息（需要先set）</li>
 */
@Slf4j
@Component
public class Auditor implements AuditorAware<String> {

    /**
     * 从每次请求中解析token获取出成员信息并储存在线程变量中，所以每次请求都可以在当前线程中获取到成员信息：MemberInfo
     * <br/><br/>
     * 如果在某个接口中使用了多线程，且在多线程中应用到需要权限验证的接口或需要获取成员信息的接口（即使用了Auditor），
     * 则需要将MemberInfo传入多线程中，并在其中的每个线程调用Auditor.setCurrentMemberInfo方法
     */
    private static final TransmittableThreadLocal<MemberInfo> CURRENT_MEMBER_INFO = new TransmittableThreadLocal<>();

    public static MemberInfo getCurrentMemberInfo() {
        MemberInfo memberInfo = CURRENT_MEMBER_INFO.get();
        if (Objects.isNull(memberInfo)) {
            if (StpUtil.isLogin()) {
                setCurrentMemberInfo((String) StpUtil.getLoginId());
            } else {
                log.warn("Auditor.getCurrentMemberInfo()未获取到MemberInfo");
            }
        }
        return CURRENT_MEMBER_INFO.get();
    }

    public static void setCurrentMemberInfo(String memberId) {
        if (!StringUtils.isBlank(memberId)) {
            // 得到memberId
            // 通过id找到用户信息存入线程变量
            MemberService memberService = SpringUtil.getBean(MemberService.class);
            MemberDTO memberDTO = memberService.queryById(memberId);
            Auditor.setCurrentMemberInfo(MemberInfo.builder()
                    .memberId(memberId)
                    .memberName(memberDTO.getMemberName())
                    .memberType(memberDTO.getMemberType())
                    .memberEmail(memberDTO.getMemberEmail())
                    .locked(memberDTO.getLocked())
                    .token(StpUtil.getTokenValue())
                    .build());
        }
        log.error("memberId 为空,无法获取对应用户信息");
    }

    public static void setCurrentMemberInfo(MemberInfo info) {
        CURRENT_MEMBER_INFO.set(info);
    }

    public static void clearCurrentMemberInfo() {
        CURRENT_MEMBER_INFO.remove();
    }

    public static Optional<String> getCurrentMemberId() {
        return Optional.ofNullable(CURRENT_MEMBER_INFO.get()).map(MemberInfo::getMemberId);
    }

    public static String getCurrentMemberIdOrThrow() {
        return getCurrentMemberId().orElseThrow(() -> new BusinessException(CodeEnum.CURRENT_MEMBER_INFO_GET_ERROR, "无法获取当前登录用户的ID"));
    }

    /**
     * 判断当前登录用户是否是管理员账户(即：最高级别权限用户)
     */
    public static boolean isAdminOrScript() {
        return switch (getCurrentMemberInfo().getMemberType()) {
            case NORMAL, INIT -> false;
            case ADMIN, SCRIPT -> true;
        };
    }

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        String memberName = Optional.ofNullable(CURRENT_MEMBER_INFO.get())
                .orElseThrow(() -> new BusinessException(CodeEnum.CURRENT_MEMBER_INFO_GET_ERROR))
                .getMemberName();
        return Optional.ofNullable(memberName);
    }

}
