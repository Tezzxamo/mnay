package cn.mnay.oss.factory;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioFactory {

    public static String staticEndpoint;
    public static String staticAccessKey;
    public static String staticSecretKey;
    public static String staticBucketName;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public static MinioClient getInstance() {
        return SingletonEnum.INSTANCE.getInstance();
    }

    public static String getBucketName() {
        return staticBucketName;
    }

    @PostConstruct
    public void init() {
        staticEndpoint = endpoint;
        staticAccessKey = accessKey;
        staticSecretKey = secretKey;
        staticBucketName = bucketName;
    }

    enum SingletonEnum {
        INSTANCE;
        final MinioClient minioClient;

        SingletonEnum() {
            minioClient = MinioClient.builder()
                    .endpoint(staticEndpoint)
                    .credentials(staticAccessKey, staticSecretKey)
                    .build();
        }

        public MinioClient getInstance() {
            // 获取MinioClient
            return minioClient;
        }
    }

}
