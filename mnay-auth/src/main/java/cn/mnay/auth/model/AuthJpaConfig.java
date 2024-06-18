package cn.mnay.auth.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {
        AuthJpaConfig.class
})
@EntityScan(basePackageClasses = {
        AuthJpaConfig.class
})
public class AuthJpaConfig {
}
