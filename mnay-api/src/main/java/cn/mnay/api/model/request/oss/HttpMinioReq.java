package cn.mnay.api.model.request.oss;

import cn.mnay.common.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HttpMinioReq {

    /**
     * 存储桶的名称
     */
    @Size(min = 3, max = 63)
    private String bucketName;

    /**
     * 存储对象的名称，在上传后会生成一个名称返回
     */
    @NotBlank(message = "[存储对象名]不能为空", groups = ValidationGroups.MinioObject.class)
    private String objectName;

}
