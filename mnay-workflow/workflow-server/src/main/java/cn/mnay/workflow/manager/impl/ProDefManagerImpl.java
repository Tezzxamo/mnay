package cn.mnay.workflow.manager.impl;

import cn.mnay.workflow.manager.ProDefManager;
import cn.mnay.workflow.model.dto.ProcessDefinitionDTO;
import cn.mnay.workflow.model.wrapper.ProDefWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@DubboService
@RequiredArgsConstructor
public class ProDefManagerImpl implements ProDefManager {

    private final RepositoryService repositoryService;

    @Override
    public void deployProcessDefinition(String deploymentName, String classpathResource) {
        // 部署指定资源路径下的bpmn20.xml
        repositoryService.createDeployment()
                .name(deploymentName)
                .addClasspathResource(classpathResource)
                .deploy();
    }

    @Override
    public boolean isProcessDefinitionDeployed(String processDefinitionName) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName(processDefinitionName)
                .active()
                .latestVersion()
                .singleResult();
        return Objects.nonNull(processDefinition);
    }


    @Override
    public void checkProcessDefinition(String processDefinitionName, String processDefinitionClassPath) {
        if (!this.isProcessDefinitionDeployed(processDefinitionName)) {
            this.deployProcessDefinition(processDefinitionName, processDefinitionClassPath);
        }
    }

    @Override
    public List<ProcessDefinitionDTO> listLatestActiveProcessDefinition() {
        return ProDefWrapper.INSTANCE.to(repositoryService.createProcessDefinitionQuery().active().latestVersion().list());
    }

}
