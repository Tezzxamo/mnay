package cn.mnay.workflow.model.wrapper;

import cn.mnay.common.model.wrapper.BaseToMapping;
import cn.mnay.workflow.model.dto.HisProInsDTO;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HisProInsWrapper extends BaseToMapping<HistoricProcessInstance, HisProInsDTO> {

    HisProInsWrapper INSTANCE = Mappers.getMapper(HisProInsWrapper.class);


}
