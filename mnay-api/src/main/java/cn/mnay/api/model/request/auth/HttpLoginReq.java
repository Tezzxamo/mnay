package cn.mnay.api.model.request.auth;

import cn.mnay.api.common.validation.LoginValidation;
import cn.mnay.common.enums.auth.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;

@Data
@Schema(name = "HttpLoginReq", description = "登录请求体")
@GroupSequenceProvider(LoginValidation.class)
public class HttpLoginReq {

    @NotNull(message = "[登录类型]不能为空")
    @Schema(name = "loginType", description = "登录类型")
    private LoginType loginType;

    @Schema(name = "phone", description = "手机号")
    private String phone;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "password", description = "密码")
    private String password;

    @Schema(name = "captcha", description = "验证码")
    private String captcha;

}
