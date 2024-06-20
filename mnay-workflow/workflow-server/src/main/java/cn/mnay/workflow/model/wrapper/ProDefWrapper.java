package cn.mnay.workflow.model.wrapper;

import cn.mnay.common.model.wrapper.BaseToMapping;
import cn.mnay.workflow.model.dto.ProcessDefinitionDTO;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProDefWrapper extends BaseToMapping<ProcessDefinition, ProcessDefinitionDTO> {

    ProDefWrapper INSTANCE = Mappers.getMapper(ProDefWrapper.class);

    @Override
    @Mappings(value = {
            @Mapping(source = "key", target = "processDefinitionKey"),
            @Mapping(source = "name", target = "processDefinitionName"),
            @Mapping(source = "version", target = "version")
    })
    ProcessDefinitionDTO to(ProcessDefinition processDefinition);

}
