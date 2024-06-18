package cn.mnay.oss.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackageClasses = {
        OssJpaConfig.class
})
@EnableJpaRepositories(basePackageClasses = {
        OssJpaConfig.class
})
public class OssJpaConfig {
}
