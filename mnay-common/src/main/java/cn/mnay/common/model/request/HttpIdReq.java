package cn.mnay.common.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "HttpIdReq", description = "仅ID字段的请求体")
public class HttpIdReq {

    @Schema(name = "id", description = "ID")
    private String id;

}
