package cn.mnay;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <li>@EnableJpaAuditing : 开启JPA审计功能</li>
 * <li>@EnableConfigurationProperties : 开启配置文件属性绑定功能</li>
 * <li>@EnableCaching : 开启缓存功能</li>
 * <li>@EnableAspectJAutoProxy : 开启AOP功能</li>
 * <li>@EnableAsync : 开启异步功能</li>
 * <li>@EnableTransactionManagement : 开启事务功能</li>
 */
@EnableJpaAuditing
@EnableConfigurationProperties
@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableAsync
@EnableTransactionManagement
public class MnayServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MnayServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 启动时的一些逻辑：
    }
}
