package cn.mnay;

import cn.mnay.workflow.manager.ProDefManager;
import cn.mnay.workflow.model.dto.ProcessDefinitionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

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
@SpringBootApplication(
        scanBasePackages = {"cn.mnay"}
)
@EnableCaching
@EnableAspectJAutoProxy
@EnableAsync
@EnableTransactionManagement
@RequiredArgsConstructor
@Slf4j
public class MnayServerApplication implements CommandLineRunner {

    @DubboReference
    ProDefManager proDefManager;

    public static void main(String[] args) {
        SpringApplication.run(MnayServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 启动时的一些逻辑：

        // 用于查看流程定义
        List<ProcessDefinitionDTO> processDefinitionList = proDefManager.listLatestActiveProcessDefinition();
        log.info("> 处于激活状态的最新版本的流程定义数量: {}", processDefinitionList.size());
        for (ProcessDefinitionDTO pd : processDefinitionList) {
            log.info("\t ===> Process definition: {} 版本：{}", pd.getProcessDefinitionKey(), pd.getVersion());
        }
    }
}
