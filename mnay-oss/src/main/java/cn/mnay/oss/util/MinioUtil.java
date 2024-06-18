package cn.mnay.oss.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import cn.mnay.oss.factory.MinioFactory;
import cn.mnay.api.model.vo.oss.MinioVO;
import cn.mnay.api.model.request.oss.HttpMinioReq;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MinIO操作的工具类<br/>
 * 说明：<br/>
 * ①新部署的系统，对应的minio的bucket不通过该工具类增删，所以移除了bucket的相关操作，直接在前端页面创建<br/>
 * ②上传至minio中的文件，不通过该工具类进行删除，即不允许删除操作；
 */
@Slf4j
public class MinioUtil {

    public static MinioClient minioClient = MinioFactory.getInstance();

    /**
     * 文件上传
     */
    public static MinioVO upload(MultipartFile file, String bucketName, String fileName) {
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();

            // 文件名称相同会覆盖
            minioClient.putObject(objectArgs);
            return new MinioVO()
                    .setMinioObject(CollectionUtil.toList(new MinioVO.MinioObject().setBucketName(bucketName).setObjectName(fileName)))
                    .setSuccess(true);
        } catch (Exception e) {
            throw new BusinessException(CodeEnum.DOCUMENT_UPLOAD_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * 预览
     */
    public static MinioVO preview(HttpMinioReq httpMinioReq) {
        try {
            // 查看文件地址
            GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                    .bucket(httpMinioReq.getBucketName())
                    .object(httpMinioReq.getObjectName())
                    .method(Method.GET)
                    .build();
            return new MinioVO()
                    .setMinioObject(CollectionUtil.toList(new MinioVO.MinioObject().setBucketName(httpMinioReq.getBucketName()).setObjectName(httpMinioReq.getObjectName()).setObjectUrl(minioClient.getPresignedObjectUrl(build))))
                    .setSuccess(true);
        } catch (Exception e) {
            throw new BusinessException(CodeEnum.DOCUMENT_PREVIEW_ERROR, e.getLocalizedMessage());
        }
    }

    public static MinioVO.MinioObject download(HttpMinioReq httpMinioReq) {
        Map<String, String> reqParams = new HashMap<>(4);
        reqParams.put("response-content-type", "application/octet-stream");
        try {
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(httpMinioReq.getBucketName())
                            .object(httpMinioReq.getObjectName())
                            .expiry(2, TimeUnit.HOURS)
                            .extraQueryParams(reqParams)
                            .build());
            return new MinioVO.MinioObject()
                    .setObjectUrl(url)
                    .setBucketName(httpMinioReq.getBucketName())
                    .setObjectName(httpMinioReq.getObjectName());
        } catch (Exception e) {
            throw new BusinessException(CodeEnum.DOCUMENT_DOWNLOAD_ERROR, e.getLocalizedMessage());
        }
    }

    public static String generateFileName(String fileHash, String originalFilename) {
        if (StrUtil.isBlank(originalFilename)) {
            throw new BusinessException(CodeEnum.DOCUMENT_NULL_ERROR);
        }
        // path生成规则：year/month/day
        LocalDate now = LocalDate.now();
        String path = now.getYear() + "/" + now.getMonthValue() + "/" + now.getDayOfMonth();

        // fileName生成规则：path/时间戳-UUID-文件名
        return path + "/" + fileHash + "RealName:" + originalFilename;
    }

    /**
     * 返回带B/KB/MB/GB的文件大小字符串
     *
     * @param fileSize 实际大小
     * @return 文件大小
     */
    public static String getFileSize(long fileSize) {
        long init = 1024;
        if (fileSize < init) {
            return fileSize + "B";
        }
        if (fileSize < init * init) {
            return String.format("%.2f", fileSize / 1024.0F) + "KB";
        }
        if (fileSize < init * init * init) {
            return String.format("%.2f", fileSize / 1024.0F / 1024.0F) + "MB";
        }
        if (fileSize < init * init * init * init) {
            return String.format("%.2f", fileSize / 1024.0F / 1024.0F / 1024.0F) + "GB";
        } else {
            throw new BusinessException(CodeEnum.DOCUMENT_SIZE_OUT_OF_SUPPORTED_ERROR);
        }
    }
}

