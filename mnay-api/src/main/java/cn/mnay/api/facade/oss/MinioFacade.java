package cn.mnay.api.facade.oss;

import cn.mnay.api.model.dto.oss.FileUploadDTO;
import cn.mnay.api.model.request.oss.HttpUploadReq;
import cn.mnay.common.model.model.R;
import cn.mnay.common.validation.ValidationGroups;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "v1/oss/minio", name = "Minio-文件上传-接口")
public interface MinioFacade {

    @PostMapping(value = "/uploadInit", name = "上传-初始化上传")
    FileUploadDTO uploadInit(@RequestBody @Validated(ValidationGroups.UploadInit.class) HttpUploadReq httpUploadReq);

    @PostMapping(value = "/uploadCheck", name = "上传-获取上传切片位置")
    FileUploadDTO uploadCheck(@RequestBody @Validated HttpUploadReq httpUploadReq);

    @PostMapping(value = "/preSignUploadUrl", name = "上传-获取预签名上传地址")
    R<?> preSignUploadUrl(@RequestBody @Validated(ValidationGroups.UploadPreUrl.class) HttpUploadReq httpUploadReq);

    @PostMapping(value = "/uploadMerge", name = "上传-合并切片")
    Boolean uploadMerge(@RequestBody @Validated(value = ValidationGroups.UploadMerge.class) HttpUploadReq httpUploadReq);


}
