package cn.mnay.workflow;

import cn.mnay.workflow.manager.ProDefManager;
import cn.mnay.workflow.model.dto.ProcessDefinitionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@EnableDubbo(scanBasePackages = "cn.mnay.workflow.manager")
@SpringBootApplication(scanBasePackageClasses = {WorkflowApplication.class})
@RequiredArgsConstructor
@Slf4j
public class WorkflowApplication implements CommandLineRunner {

    private final ProDefManager proDefManager;

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }

    @Override
    public void run(String... args) {
        List<ProcessDefinitionDTO> processDefinitionList = proDefManager.listLatestActiveProcessDefinition();
        log.info("> WorkflowApplication 处于激活状态的最新版本的流程定义数量: {}", processDefinitionList.size());
        for (ProcessDefinitionDTO pd : processDefinitionList) {
            log.info("\t ===> Process definition: {} 版本：{}", pd.getProcessDefinitionKey(), pd.getVersion());
        }
    }
}
