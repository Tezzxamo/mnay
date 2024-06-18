package cn.mnay.common.model.dto.auth;

import cn.mnay.common.enums.auth.MemberTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo {

    private String memberId;
    private String memberName;
    private String memberEmail;
    private MemberTypeEnum memberType;
    private Boolean locked;
    private String token;


}
