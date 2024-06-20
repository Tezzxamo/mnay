package cn.mnay.workflow.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "工作流模块：流程定义DTO")
public class ProcessDefinitionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 流程定义Key/Name
     */
    @Schema(description = "流程定义Key", example = "wholesaleApproveProcess")
    private String processDefinitionKey;

    @Schema(description = "流程定义Name", example = "process_1")
    private String processDefinitionName;

    @Schema(description = "流程定义版本", example = "1")
    private Integer version;

    // 需要时添加其他字段

}
