package cn.mnay.api.model.request.oss;

import cn.mnay.common.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class HttpUploadReq {


    // check只需要填写fileHash
    @NotBlank(message = "[文件哈希]不能为空")
    private String fileHash;

    @NotBlank(message = "[文件名称]不能为空", groups = {ValidationGroups.UploadInit.class, ValidationGroups.UploadMerge.class})
    private String fileName;
    @NotNull(message = "[切片大小]不能为空", groups = {ValidationGroups.UploadInit.class})
    private Long chunkSize;
    @NotNull(message = "[文件大小]不能为空", groups = {ValidationGroups.UploadInit.class, ValidationGroups.UploadMerge.class})
    private Long totalSize;

    @NotNull(message = "[切片位置]不能为空", groups = {ValidationGroups.UploadPreUrl.class})
    private Integer partNumber;

}
