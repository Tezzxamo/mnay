package cn.mnay.api.service.auth;

import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.api.model.request.auth.HttpAuthReq;
import cn.mnay.api.model.request.auth.HttpLoginReq;
import cn.mnay.common.model.request.HttpEmailReq;
import cn.mnay.common.model.request.HttpIdReq;

public interface LoginService {

    /**
     * 系统-用户登录
     *
     * @param httpLoginReq 登录请求体
     * @param forceLogin   是否强制登录（如果有其他地方登录是否挤下线）
     * @return MemberDTO
     */
    MemberDTO authLogin(HttpLoginReq httpLoginReq, boolean forceLogin);

    /**
     * 系统-用户登出/踢下线
     * 【接口权限需求：①自身;②系统管理员】
     *
     * @param httpIdReq 请求体
     * @return 成功登出->true
     */
    Boolean authLogout(HttpIdReq httpIdReq);

    /**
     * 系统-用户重置密码验证码发送
     *
     * @param httpEmailReq@return 用户信息
     */
    Boolean authResetPwdCaptcha(HttpEmailReq httpEmailReq);

    /**
     * 系统-用户重置密码请求
     *
     * @param httpAuthReq 包含邮箱的dto
     * @return 用户信息
     */
    MemberDTO authResetPwd(HttpAuthReq httpAuthReq);

    /**
     * 获取token信息
     *
     * @param memberDTO memberDTO
     * @return token
     */
    String getToken(MemberDTO memberDTO);

}
