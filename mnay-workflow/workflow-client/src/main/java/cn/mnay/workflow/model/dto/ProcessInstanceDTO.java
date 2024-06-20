package cn.mnay.workflow.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
@Schema(description = "工作流模块：流程实例DTO")
public class ProcessInstanceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "流程实例ID")
    private String processInstanceId;
    @Schema(description = "流程定义ID")
    private String processDefinitionId;
    @Schema(description = "业务主键")
    private String businessKey;

    // 需要时添加其他字段

}
