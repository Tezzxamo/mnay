package cn.mnay.workflow.manager;

import cn.mnay.workflow.model.dto.HisProInsDTO;
import cn.mnay.workflow.model.dto.ProcessInstanceDTO;

import java.util.List;
import java.util.Map;

public interface ProInsManager {


    /**
     * 创建流程实例
     *
     * @param processDefinitionKey 流程定义key(在此系统中，流程定义key和流程定义name是一致的)
     * @param businessKey          业务key
     * @param variables            流程变量
     * @return 流程实例id
     */
    String createProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables);

    /**
     * Desc：判断流程实例是否存在
     *
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务Key
     * @return true->存在;false->不存在
     */
    Boolean existProcessInstance(String processDefinitionKey, String businessKey);

    /**
     * Desc：删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);

    /**
     * Desc:无论任何情况，清除所有的流程实例，实质上还是一个一个删除<br/>
     * (注:当本身没有流程实例的时候使用此方法也不会报错)
     */
    void clearAllProcessInstances();

    /**
     * Desc:获取所有的流程实例
     *
     * @param processDefinitionKey 流程定义Key
     * @return 流程实例
     */
    List<ProcessInstanceDTO> getAllProcessInstancesByProcessDefinitionKey(String processDefinitionKey);

    /**
     * Desc：清除所有给定的流程定义Key有关的流程实例
     *
     * @param processDefinitionKey 流程定义Key
     */
    void clearAllProcessInstancesByProcessDefinitionKey(String processDefinitionKey);

    /**
     * 通过businessKey获取流程实例
     *
     * @param businessKey 业务key
     * @return 流程实例
     */
    ProcessInstanceDTO getProcessInstanceByBusinessKey(String businessKey);

    /**
     * 通过businessKey获取历史流程实例
     *
     * @param businessKey 业务key
     * @return 历史流程实例
     */
    List<HisProInsDTO> getProcessInstanceListByBusinessKey(String businessKey);

    /**
     * Desc：完成当前的task，使其走向下一步(只有拥有审批人，且审批人是拾取人时才可以操作)
     *
     * @param processInstanceId 流程实例id
     * @param userId            用户
     * @param variables         要携带的变量数据(不需要的地方可以为null)
     */
    void completeProcessInstance(String processInstanceId, String userId, Map<String, Object> variables);

    /**
     * 【结算审批】强制拾取流程，并完成当前节点
     *
     * @param processInstanceId 流程实例id
     * @param userId            用户
     * @param variables         要携带的变量数据
     * @param nodeName          节点名称
     * @param description       节点描述
     */
    void mandatoryCompleteProcessInstanceForSettlement(String processInstanceId, String userId, Map<String, Object> variables, String nodeName, String description);

    /**
     * Desc:强制拾取并完成任务【用于该节点没有task assign或者没有task candidate时候完成当前节点，流转到下一个节点】
     *
     * @param processInstanceId 流程实例id
     * @param userId            用户id
     * @param variables         要携带的变量数据(不需要的地方可以为null)
     */
    void mandatoryCompleteProcessInstance(String processInstanceId, String userId, Map<String, Object> variables);

    /**
     * 通过processInstanceId获取当前节点位置
     *
     * @param processInstanceId 流程实例Id
     * @return 当前节点位置
     */
    String getCurrentNode(String processInstanceId);

    /**
     * Desc:获取流程实例的变量(唯一值)【如果不是唯一的变量，则会报错】
     *
     * @param processInstanceId 流程实例id
     * @param variableName      变量名
     * @return 变量值
     */
    Object getVariableByProcessInstanceId(String processInstanceId, String variableName);

    /**
     * 获取businessKey
     *
     * @param processInstanceId 流程实例id
     * @return businessKey
     */
    String getBusinessKeyByProcessInstanceId(String processInstanceId);

    /**
     * 给制定的流程实例设置变量(可以更新之前的变量)
     *
     * @param processInstanceId 流程实例id
     * @param variables         要携带的变量数据
     */
    void setVariableOnProcessInstance(String processInstanceId, Map<String, Object> variables);

    /**
     * 清理历史数据
     *
     * @param processDefinitionKey 流程定义Key
     */
    void clearHistoryByProcessDefinitionKey(String processDefinitionKey);

}
