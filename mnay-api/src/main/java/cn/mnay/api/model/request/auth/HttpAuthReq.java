package cn.mnay.api.model.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HttpAuthReq {

    @NotBlank(message = "[邮箱]不能为空")
    @Email(message = "[邮箱]格式不正确")
    private String memberEmail;

    /**
     * 传加密后的密码
     */
    @NotBlank(message = "[密码]不能为空")
    private String memberPassword;
    @NotBlank(message = "[确认密码]不能为空")
    private String memberPasswordRepeat;

    @NotBlank(message = "[验证码]不能为空")
    @Size(min = 6, max = 6, message = "验证码长度有误")
    private String captcha;

}
