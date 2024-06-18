package cn.mnay.oss.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.mnay.api.model.dto.auth.Auditor;
import cn.mnay.common.enums.error.CodeEnum;
import cn.mnay.common.exception.BusinessException;
import cn.mnay.common.model.dbo.SnowflakeIdGenerator;
import cn.mnay.oss.model.dbo.MnayFile;
import cn.mnay.oss.model.repo.MnayFileRepo;
import cn.mnay.oss.factory.MinioFactory;
import cn.mnay.api.model.dto.oss.FileUploadDTO;
import cn.mnay.api.model.request.oss.HttpUploadReq;
import cn.mnay.api.service.oss.MinioService;
import cn.mnay.oss.util.MinioUtil;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MnayFileRepo mnayFileRepo;
    private final AmazonS3 amazonS3;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadDTO uploadCheck(HttpUploadReq httpUploadReq) {
        FileUploadDTO retDTO = FileUploadDTO.init();

        // 秒传功能是否启用
        if (this.canApplySecondTransmission(httpUploadReq.getFileHash())) {
            retDTO.setSecondTransmission(true);
            retDTO.setFinished(true);
            return retDTO;
        }

        // 如果数据库中暂时还没有该fileHash，则无法进行【秒传】，需要查验是否【断点上传】
        String key = httpUploadReq.getFileHash() + Auditor.getCurrentMemberIdOrThrow();

        // 【②-需要初始化】:没上传过，且没初始化，则需要初始化
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            return retDTO;
        }

        // 取出dto
        String jsonString = Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(Object::toString).orElse(null);
        retDTO = JSONUtil.toBean(jsonString, FileUploadDTO.class);
        assert retDTO != null;
        boolean doesObjectExist = amazonS3.doesObjectExist(retDTO.getBucketName(), retDTO.getObjectName());

        // 【③-断点续传】:如果不存在说明未上传完毕,返回已上传切片列表，让前端继续请求获取上传url进行上传切片
        if (!doesObjectExist) {
            ListPartsRequest listPartsRequest = new ListPartsRequest(retDTO.getBucketName(), retDTO.getObjectName(), retDTO.getUploadId());
            PartListing partListing = amazonS3.listParts(listPartsRequest);
            // 将已上传完毕的切片放入retDTO
            retDTO.setUploadIdList(partListing.getParts());
            // 续签
            redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(retDTO), 60, TimeUnit.MINUTES);
        } else {
            retDTO.setFinished(true);
            // 如果已经合并完毕，则delete（使当前用户的重复文件可以上传，但是重复文件必须等待前一个相同的文件上传完毕才可以上传）
            redisTemplate.delete(key);
        }
        return retDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadDTO uploadInit(HttpUploadReq httpUploadReq) {
        FileUploadDTO retDTO = FileUploadDTO.init();

        // 秒传功能是否启用
        if (this.canApplySecondTransmission(httpUploadReq.getFileHash())) {
            retDTO.setSecondTransmission(true);
            retDTO.setFinished(true);
            return retDTO;
        }

        // 如果当前文件存在在redis中，返回redis中的FileUploadDTO，而不是重新初始化
        String key = httpUploadReq.getFileHash() + Auditor.getCurrentMemberIdOrThrow();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            String jsonString = Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(Object::toString).orElse(null);
            return JSONUtil.toBean(jsonString, FileUploadDTO.class);
        }

        // bucketName/objectName/contentType
        String bucketName = MinioFactory.getBucketName();
        String objectName = MinioUtil.generateFileName(httpUploadReq.getFileHash(), httpUploadReq.getFileName());
        String contentType = MediaTypeFactory.getMediaType(objectName).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        // 计算分片数
        int partNumber = (int) Math.ceil(httpUploadReq.getTotalSize() * 1.0 / httpUploadReq.getChunkSize());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3.initiateMultipartUpload(
                new InitiateMultipartUploadRequest(bucketName, objectName).withObjectMetadata(objectMetadata)
        );
        String uploadId = initiateMultipartUploadResult.getUploadId();

        // 设置
        retDTO.setUploadId(uploadId)
                .setInited(true)
                .setFileName(httpUploadReq.getFileName())
                .setFileHash(httpUploadReq.getFileHash())
                .setBucketName(bucketName)
                .setObjectName(objectName)
                .setChunkSize(httpUploadReq.getChunkSize())
                .setTotalSize(httpUploadReq.getTotalSize())
                .setPartNumber(partNumber)
                .setUploadIdList(Lists.newArrayList());

        // 将初始化的dto存入redis. key:filehash+MemberId   value:fileUploadDTO
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(retDTO), 60, TimeUnit.MINUTES);

        // 返回
        return retDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String preSignUploadUrl(HttpUploadReq httpUploadReq) {
        // redis的key
        String key = httpUploadReq.getFileHash() + Auditor.getCurrentMemberIdOrThrow();

        // 如果不存在
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            throw new BusinessException(CodeEnum.ERROR, "不存在该上传任务，请先初始化上传任务");
        }

        // 取出dto
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);    // 续签
        String jsonString = Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(Object::toString).orElse(null);
        FileUploadDTO fileUploadDTO = JSONUtil.toBean(jsonString, FileUploadDTO.class);
        assert fileUploadDTO != null;

        // 构建预签名url
        Date currentDate = new Date();
        Date expireDate = DateUtil.offsetMillisecond(currentDate, 60 * 10 * 1000);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(fileUploadDTO.getBucketName(), fileUploadDTO.getObjectName())
                .withExpiration(expireDate).withMethod(HttpMethod.PUT);
        request.addRequestParameter("partNumber", httpUploadReq.getPartNumber().toString());
        request.addRequestParameter("uploadId", fileUploadDTO.getUploadId());

        // 生成URL
        URL preSignedUrl = amazonS3.generatePresignedUrl(request);

        // 返回url的字符串
        return preSignedUrl.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadDTO uploadMerge(HttpUploadReq httpUploadReq) {
        // 先试图走一遍秒传功能
        if (this.uploadSecondTransmission(httpUploadReq)) {
            return null;
        }

        // redis的key
        String key = httpUploadReq.getFileHash() + Auditor.getCurrentMemberIdOrThrow();
        // 如果不存在
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            throw new BusinessException(CodeEnum.ERROR, "不存在该上传任务，请先初始化上传任务");
        }

        // 取出dto
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);    // 续签
        String jsonString = Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(Object::toString).orElse(null);
        FileUploadDTO fileUploadDTO = JSONUtil.toBean(jsonString, FileUploadDTO.class);
        assert fileUploadDTO != null;

        // 获取分片记录
        ListPartsRequest listPartsRequest = new ListPartsRequest(fileUploadDTO.getBucketName(), fileUploadDTO.getObjectName(), fileUploadDTO.getUploadId());
        PartListing partListing = amazonS3.listParts(listPartsRequest);

        // 因为PartListing一次性最多1000分片，所以要对于分片数量较大的情况作以处理
        List<PartSummary> parts = new ArrayList<>(partListing.getParts());
        int partSize = partListing.getParts().size();
        // 是否需要更多的请求
        while (partListing.isTruncated()) {
            // 构造新的请求并设置位置
            ListPartsRequest nextRequest = new ListPartsRequest(fileUploadDTO.getBucketName(), fileUploadDTO.getObjectName(), fileUploadDTO.getUploadId())
                    .withPartNumberMarker(partListing.getNextPartNumberMarker());
            //
            partListing = amazonS3.listParts(nextRequest);
            parts.addAll(partListing.getParts());
            partSize = partSize + partListing.getParts().size();
        }

        // 断言已上传分块数量与记录中一致，否则不能合并分块并报错
        Assert.isTrue(fileUploadDTO.getPartNumber() == partSize, () -> new BusinessException(CodeEnum.ERROR, "分片缺失，请重新上传"));

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest()
                .withUploadId(fileUploadDTO.getUploadId())
                .withKey(fileUploadDTO.getObjectName())
                .withBucketName(fileUploadDTO.getBucketName())
                .withPartETags(parts.stream().map(partSummary -> new PartETag(partSummary.getPartNumber(), partSummary.getETag())).collect(Collectors.toList()));
        // 合并之前的所有的切片
        amazonS3.completeMultipartUpload(completeMultipartUploadRequest);

        // 保存文件存储信息
        MnayFile mnayFile = new MnayFile();
        mnayFile.setId(SnowflakeIdGenerator.nextIdStr());
        mnayFile.setFileHash(fileUploadDTO.getFileHash())
                .setBucketName(fileUploadDTO.getBucketName())
                .setObjectName(fileUploadDTO.getObjectName());
        MnayFile save = mnayFileRepo.save(mnayFile);
        // 断言row为1保存成功，否则报错
        Assert.isTrue(StrUtil.isNotBlank(save.getId()), () -> new BusinessException(CodeEnum.ERROR, "保存文件存储信息失败"));

        return fileUploadDTO;
    }


    // ————————————————————————————————————————————————————————————————————————————————————————


    /**
     * 是否可以应用秒传功能
     *
     * @param fileHash 文件hash
     * @return true->应用
     */
    private boolean canApplySecondTransmission(String fileHash) {
        List<MnayFile> list = mnayFileRepo.findByFileHash(fileHash);
        // 不为空->true
        return CollUtil.isNotEmpty(list);
    }

    /**
     * 判断是否以及存在相同的fileHash，如果存在的话，则不重复上传相同文件，直接使用之前上传的文件，通过添加document记录，代表成功上传
     * 即：秒传
     *
     * @param httpUploadReq 待上传文件信息
     */
    private boolean uploadSecondTransmission(HttpUploadReq httpUploadReq) {
        List<MnayFile> list = mnayFileRepo.findByFileHash(httpUploadReq.getFileHash());
        // 【①-秒传】:如果list的size＞0，说明当前文件hash已经存在于minIo中，任意取其一的bucketName和ObjectName，然后保存当前文件信息即可
        return CollUtil.isNotEmpty(list);
    }

}
