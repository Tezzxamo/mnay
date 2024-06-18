package cn.mnay.api.model.request.auth;

import cn.mnay.common.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建用户信息/更新用户信息/更改锁定状态:使用的DTO
 */
@Data
public class HttpMemberUpdateReq {

    @NotBlank(message = "[id]不能为空", groups = {ValidationGroups.Update.class, ValidationGroups.ChangeStatus.class})
    private String id;
    @NotBlank(message = "[用户名]不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String memberName;
    @NotBlank(message = "[工号]不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String memberNo;
    @NotBlank(message = "[邮箱]不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String memberEmail;
    @NotNull(message = "[锁定状态]不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class, ValidationGroups.ChangeStatus.class})
    private Boolean locked;

}
