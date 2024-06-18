package cn.mnay.api.model.vo.oss;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MinioVO {

    private List<MinioObject> minioObject;

    private Boolean success;

    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MinioObject {
        private String bucketName;
        private String ObjectName;
        private String objectUrl;
    }

}
