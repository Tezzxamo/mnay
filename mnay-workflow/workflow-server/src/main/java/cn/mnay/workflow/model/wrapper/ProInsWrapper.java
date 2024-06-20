package cn.mnay.workflow.model.wrapper;

import cn.mnay.common.model.wrapper.BaseToMapping;
import cn.mnay.workflow.model.dto.ProcessInstanceDTO;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProInsWrapper extends BaseToMapping<ProcessInstance, ProcessInstanceDTO> {

    ProInsWrapper INSTANCE = Mappers.getMapper(ProInsWrapper.class);

}
