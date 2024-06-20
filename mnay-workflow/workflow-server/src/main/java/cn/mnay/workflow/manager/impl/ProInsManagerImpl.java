package cn.mnay.workflow.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.mnay.workflow.manager.ProInsManager;
import cn.mnay.workflow.model.dto.HisProInsDTO;
import cn.mnay.workflow.model.dto.ProcessInstanceDTO;
import cn.mnay.workflow.model.wrapper.HisProInsWrapper;
import cn.mnay.workflow.model.wrapper.ProInsWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@DubboService
@RequiredArgsConstructor
public class ProInsManagerImpl implements ProInsManager {

    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;


    @Override
    public String createProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables).getProcessInstanceId();
    }

    @Override
    public Boolean existProcessInstance(String processDefinitionKey, String businessKey) {
        // 查询正在流转的流程实例是否存在,如果存在则返回true
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .processInstanceBusinessKey(businessKey)
                .singleResult();
        return Objects.nonNull(processInstance);
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String reason) {
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    @Override
    public void clearAllProcessInstances() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        runtimeService.deleteProcessInstances(list.stream().map(Execution::getProcessInstanceId).collect(Collectors.toList()),
                "移除所有流程实例", true, false);
    }

    @Override
    public List<ProcessInstanceDTO> getAllProcessInstancesByProcessDefinitionKey(String processDefinitionKey) {
        return ProInsWrapper.INSTANCE.to(runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).list());
    }

    @Override
    public void clearAllProcessInstancesByProcessDefinitionKey(String processDefinitionKey) {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).list();
        runtimeService.deleteProcessInstances(list.stream().map(Execution::getProcessInstanceId).collect(Collectors.toList()),
                "移除所有" + processDefinitionKey + "流程实例", true, false);
    }

    @Override
    public ProcessInstanceDTO getProcessInstanceByBusinessKey(String businessKey) {
        return ProInsWrapper.INSTANCE.to(runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult());
    }

    @Override
    public List<HisProInsDTO> getProcessInstanceListByBusinessKey(String businessKey) {
        return HisProInsWrapper.INSTANCE.to(historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list());
    }

    @Override
    public void completeProcessInstance(String processInstanceId, String userId, Map<String, Object> variables) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(userId)
                .singleResult();

        // 断言：task不为空，否则报错
        Assert.notNull(task, "流程不存在，或审批人不对应无法审批");

        // 拾取流程，设置变量并完成流程
        taskService.setVariablesLocal(task.getId(), variables);
        taskService.complete(task.getId(), variables);
    }

    @Override
    public void mandatoryCompleteProcessInstanceForSettlement(String processInstanceId, String userId, Map<String, Object> variables, String nodeName, String description) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // 断言：task不为空，否则报错
        Assert.notNull(task, "task不存在或task审批人不对应");

        // 拾取流程，设置变量并完成流程
        task.setName(nodeName);
        task.setDescription(description);
        taskService.saveTask(task);
        taskService.setVariablesLocal(task.getId(), variables);
        this.mandatoryClaimProcessInstance(processInstanceId, userId);
        taskService.complete(task.getId(), variables);
    }

    @Override
    public void mandatoryCompleteProcessInstance(String processInstanceId, String userId, Map<String, Object> variables) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        // 强制拾取
        this.mandatoryClaimTask(task.getId(), userId);
        taskService.setVariablesLocal(task.getId(), variables);
        // 完成任务
        taskService.complete(task.getId(), variables);
    }

    @Override
    public String getCurrentNode(String processInstanceId) {
        return this.getCurrentTask(processInstanceId).getFirst().getName();
    }

    @Override
    public Object getVariableByProcessInstanceId(String processInstanceId, String variableName) {
        HistoricVariableInstance result = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .variableName(variableName)
                .singleResult();
        return Objects.isNull(result) ? null : result.getValue();
    }

    @Override
    public String getBusinessKeyByProcessInstanceId(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult()
                .getBusinessKey();
    }

    @Override
    public void setVariableOnProcessInstance(String processInstanceId, Map<String, Object> variables) {
        runtimeService.setVariables(processInstanceId, variables);
    }

    @Override
    public void clearHistoryByProcessDefinitionKey(String processDefinitionKey) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        if (CollUtil.isNotEmpty(list)) {
            historyService.deleteHistoricProcessInstances(list.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList()));
        } else {
            throw new ServiceException("该流程定义下没有历史数据，无需清理");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Desc:使userId这个用户成为对于该task的审批人（强制获取、用于强制更改）
     *
     * @param processInstanceId 流程实例id
     * @param userId            审批人
     */
    public void mandatoryClaimProcessInstance(String processInstanceId, String userId) {
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        this.mandatoryClaimTask(task.getId(), userId);
    }

    /**
     * Desc:强制拾取任务
     *
     * @param taskId 任务id
     * @param userId 用户id
     */
    public void mandatoryClaimTask(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    /**
     * Desc:获取当前流程实例的所有task
     *
     * @param processInstanceId 流程实例id
     * @return task列表
     */
    public List<Task> getCurrentTask(String processInstanceId) {
        return taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * Desc:获取当前流程实例的最新task
     *
     * @param processInstanceId 流程实例id
     * @return task
     */
    public Task getLastTask(String processInstanceId) {
        List<Task> list = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
        return list.stream().sorted(Comparator.comparing(Task::getCreateTime).reversed()).toList().getFirst();
    }


}
