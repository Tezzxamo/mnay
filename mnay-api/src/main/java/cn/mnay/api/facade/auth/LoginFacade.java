package cn.mnay.api.facade.auth;

import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.api.model.request.auth.HttpAuthReq;
import cn.mnay.api.model.request.auth.HttpLoginReq;
import cn.mnay.common.model.request.HttpEmailReq;
import cn.mnay.common.model.request.HttpIdReq;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "v1/auth/login", name = "AUTH-登录相关接口")
public interface LoginFacade {

    @PostMapping(value = {"/login"}, name = "系统-用户登录")
    MemberDTO authLogin(@RequestBody @Validated HttpLoginReq httpLoginReq);

    @PostMapping(value = {"/logout"}, name = "系统-用户登出/踢下线")
    Boolean authLogout(@RequestBody @Validated HttpIdReq httpIdReq);

    @PostMapping(value = {"/reset-captcha"}, name = "系统-用户重置密码验证码发送")
    Boolean authResetPwdCaptcha(@RequestBody @Validated HttpEmailReq httpEmailReq);

    @PostMapping(value = {"/reset-password"}, name = "系统-用户重置密码请求")
    MemberDTO authResetPwd(@RequestBody @Validated HttpAuthReq httpAuthReq);

}
