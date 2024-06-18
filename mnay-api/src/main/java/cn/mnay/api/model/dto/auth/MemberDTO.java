package cn.mnay.api.model.dto.auth;

import cn.mnay.common.enums.auth.MemberTypeEnum;
import cn.mnay.common.enums.base.BaseEnumDeSerializer;
import cn.mnay.common.enums.base.BaseEnumSerializer;
import cn.mnay.common.validation.ValidationGroups;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDTO {

    @NotBlank(message = "[ID]不能为空", groups = {ValidationGroups.QueryById.class})
    private String id;
    @NotBlank(message = "[用户名]不能为空", groups = {ValidationGroups.QueryByName.class})
    private String memberName;
    @NotBlank(message = "[邮箱]不能为空", groups = {ValidationGroups.QueryByEmail.class})
    private String memberEmail;


    private String memberNo;
    // 密码不返回
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String memberPassword;
    private String memberPhone;                 // 非必填
    //    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonSerialize(using = BaseEnumSerializer.class)
    @JsonDeserialize(using = BaseEnumDeSerializer.class)
    private MemberTypeEnum memberType;          // 新建用户默认普通用户,用户类型只能由ADMIN用户和SCRIPT用户
    private Boolean locked;                     // 新建用户默认false
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastLoginTime;        // 新建用户此条暂无
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lockedTime;           // 未锁定用户此条暂无
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime passwordModifyTime;   // 新建用户默认创建时间


    @Null(message = "创建时间不可传入")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;   // 自动更新，自动返回
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Null(message = "更新时间不可传入")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;   // 自动更新，自动返回
    private String description;                 // 成员描述

    /**
     * 登录后返回信息，如果操作不是登录，则无需对token进行填充
     */
    private String token;


}
