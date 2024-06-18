package cn.mnay.api.model.dto.oss;

import cn.hutool.core.collection.ListUtil;
import com.amazonaws.services.s3.model.PartSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "FileUploadDTO", description = "文件上传DTO")
public class FileUploadDTO {

    @Schema(name = "inited", description = "是否已初始化（true->已经初始化）", type = "Boolean")
    private Boolean inited;

    @Schema(name = "secondTransmission", description = "是否应用秒传（true->直接调用merge）", type = "Boolean")
    private Boolean secondTransmission;

    @Schema(name = "finished", description = "是否完成上传（是否已经合并分片）", type = "Boolean")
    private Boolean finished;

    @Schema(name = "uploadId", description = "minio的uploadId", type = "String")
    private String uploadId;

    @Schema(name = "partNumber", description = "切片数", type = "Integer")
    private Integer partNumber;

    @Schema(name = "chunkSize", description = "每个分片大小（byte）", type = "Integer")
    private Long chunkSize;

    @Schema(name = "fileHash", description = "文件哈希[标识文件]", type = "String")
    private String fileHash;

    @Schema(name = "fileName", description = "文件名称", type = "String")
    private String fileName;

    @Schema(name = "bucketName", description = "所属桶名", type = "String")
    private String bucketName;

    @Schema(name = "objectName", description = "文件存储名称", type = "String")
    private String objectName;

    @Schema(name = "totalSize", description = "文件大小（byte）", type = "String")
    private Long totalSize;

    @Schema(name = "uploadIdList", description = "已上传完的分片 （finished为true时，该字段为null）", type = "List")
    private List<PartSummary> uploadIdList;


    public static FileUploadDTO init() {
        return new FileUploadDTO()
                .setInited(false)
                .setFinished(false)
                .setSecondTransmission(false)
                .setUploadIdList(ListUtil.toList());
    }

}
