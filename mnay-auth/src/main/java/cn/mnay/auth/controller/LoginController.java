package cn.mnay.auth.controller;

import cn.mnay.api.facade.auth.LoginFacade;
import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.api.model.request.auth.HttpAuthReq;
import cn.mnay.api.model.request.auth.HttpLoginReq;
import cn.mnay.api.service.auth.LoginService;
import cn.mnay.common.model.request.HttpEmailReq;
import cn.mnay.common.model.request.HttpIdReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "LoginController", description = "AUTH-登录相关接口")
public class LoginController implements LoginFacade {

    private final LoginService loginService;

    @Override
    @Operation(summary = "系统-用户登录")
    public MemberDTO authLogin(HttpLoginReq httpLoginReq) {
        return loginService.authLogin(httpLoginReq, false);
    }

    @Override
    @Operation(summary = "系统-用户登出/踢下线")
    public Boolean authLogout(HttpIdReq httpIdReq) {
        return loginService.authLogout(httpIdReq);
    }

    @Override
    @Operation(summary = "系统-用户重置密码验证码发送")
    public Boolean authResetPwdCaptcha(HttpEmailReq httpEmailReq) {
        return loginService.authResetPwdCaptcha(httpEmailReq);
    }

    @Override
    @Operation(summary = "系统-用户重置密码请求")
    public MemberDTO authResetPwd(HttpAuthReq httpAuthReq) {
        return loginService.authResetPwd(httpAuthReq);
    }


}
