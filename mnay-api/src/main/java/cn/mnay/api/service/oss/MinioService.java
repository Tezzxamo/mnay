package cn.mnay.api.service.oss;


import cn.mnay.api.model.dto.oss.FileUploadDTO;
import cn.mnay.api.model.request.oss.HttpUploadReq;

public interface MinioService {

    /**
     * 第一步：确认是否应用秒传功能<p>
     * 第二步：如果不应用，从redis中根据【fileHash】这个key进行查询/获取，查看是否有过上传任务<p>
     * 第三步：如果有上传任务，返回上传进度，如果没有，返回通知<p>
     * 如果有将结果（已上传完毕的切片数）置入dto并返回；如果没有则返回切片数0<p>
     *
     * @param httpUploadReq httpUploadReq
     * @return {@link FileUploadDTO}
     */
    FileUploadDTO uploadCheck(HttpUploadReq httpUploadReq);

    /**
     * 第一步：确认是否应用秒传功能<p>
     * 第二步：如果不应用，则初始化上传任务<p>
     *
     * @param httpUploadReq 文件信息
     * @return {@link FileUploadDTO}
     */
    FileUploadDTO uploadInit(HttpUploadReq httpUploadReq);

    /**
     * 构建预签名上传url，通过该url使用PUT方法进行上传
     *
     * @param httpUploadReq 切片位置和文件hash
     * @return url字符串
     */
    String preSignUploadUrl(HttpUploadReq httpUploadReq);

    /**
     * 合并切片/储存document并上链
     *
     * @param httpUploadReq 只需要填写filehash的入参
     * @return {@link FileUploadDTO}
     */
    FileUploadDTO uploadMerge(HttpUploadReq httpUploadReq);



}
