package cn.mnay.workflow.manager;

import cn.mnay.workflow.model.dto.ProcessDefinitionDTO;

import java.util.List;

public interface ProDefManager {

    /**
     * 部署流程定义
     *
     * @param deploymentName    部署名称
     * @param classpathResource 资源路径
     */
    void deployProcessDefinition(String deploymentName, String classpathResource);

    /**
     * 判断流程定义是否已经部署
     *
     * @param processDefinitionName 流程定义名称
     * @return true:已部署 false:未部署
     */
    boolean isProcessDefinitionDeployed(String processDefinitionName);

    /**
     * 校验是否存在，如果不存在则部署
     *
     * @param processDefinitionName      流程定义名称
     * @param processDefinitionClassPath 流程定义资源路径
     */
    void checkProcessDefinition(String processDefinitionName, String processDefinitionClassPath);

    /**
     * 输出所有处于激活状态的最新版本的流程定义
     *
     * @return 流程定义列表
     */
    List<ProcessDefinitionDTO> listLatestActiveProcessDefinition();

}
