package cn.mnay.common.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "HttpEmailReq", description = "仅邮箱字段的请求体")
public class HttpEmailReq {
    @Schema(name = "email", description = "邮箱")
    String email;
}
