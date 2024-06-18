package cn.mnay.oss.controller;

import cn.mnay.api.facade.oss.MinioFacade;
import cn.mnay.api.model.dto.oss.FileUploadDTO;
import cn.mnay.api.model.request.oss.HttpUploadReq;
import cn.mnay.api.service.oss.MinioService;
import cn.mnay.common.model.model.R;
import cn.mnay.common.model.model.RFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "MinioController", description = "Minio-文件上传-接口")
public class MinioController implements MinioFacade {

    private final MinioService minioService;

    @Override
    @Operation(summary = "上传-初始化上传")
    public FileUploadDTO uploadInit(HttpUploadReq httpUploadReq) {
        return minioService.uploadInit(httpUploadReq);
    }

    @Override
    @Operation(summary = "上传-获取上传切片位置")
    public FileUploadDTO uploadCheck(HttpUploadReq httpUploadReq) {
        return minioService.uploadCheck(httpUploadReq);
    }

    @Override
    @Operation(summary = "上传-获取预签名上传地址")
    public R<?> preSignUploadUrl(HttpUploadReq httpUploadReq) {
        return RFactory.newSuccess(minioService.preSignUploadUrl(httpUploadReq));
    }

    @Override
    @Operation(summary = "上传-合并切片")
    public Boolean uploadMerge(HttpUploadReq httpUploadReq) {
        minioService.uploadMerge(httpUploadReq);
        return true;
    }

}
