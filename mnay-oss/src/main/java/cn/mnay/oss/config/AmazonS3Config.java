package cn.mnay.oss.config;

import cn.mnay.oss.factory.MinioFactory;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonS3Config {

    private final MinioFactory minioFactory;

    @Bean(name = "amazonS3Client")
    public AmazonS3 amazonS3Client () {
        // 设置连接时的参数
        ClientConfiguration config = new ClientConfiguration();

        // 设置连接方式为HTTP，可选参数为HTTP和HTTPS
        config.setProtocol(Protocol.HTTP);

        // 设置网络访问超时时间
        config.setConnectionTimeout(5000);

        // 如果要传输大文件，设置为true
        config.setUseExpectContinue(true);

        // 提供对用于访问 AWS 服务的 AWS 凭证的访问
        AWSCredentials credentials = new BasicAWSCredentials(minioFactory.getAccessKey(), minioFactory.getSecretKey());

        // 设置Endpoint
        AwsClientBuilder.EndpointConfiguration end_point = new AwsClientBuilder.EndpointConfiguration(minioFactory.getEndpoint(), Regions.US_EAST_1.name());

        // 设置、返回
        return AmazonS3ClientBuilder.standard()
                .withClientConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(end_point)
                .withPathStyleAccessEnabled(true).build();
    }

}