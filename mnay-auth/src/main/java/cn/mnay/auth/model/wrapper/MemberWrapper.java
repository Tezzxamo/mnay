package cn.mnay.auth.model.wrapper;

import cn.mnay.api.model.dto.auth.MemberDTO;
import cn.mnay.auth.model.dbo.Member;
import cn.mnay.common.model.wrapper.BaseMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberWrapper extends BaseMapping<Member, MemberDTO> {

    MemberWrapper INSTANCE = Mappers.getMapper(MemberWrapper.class);


}
