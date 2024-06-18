package cn.mnay.auth.model.dbo;

import cn.mnay.api.enums.auth.PermissionTypeEnum;
import cn.mnay.api.enums.auth.ResourceOperationTypeEnum;
import cn.mnay.api.enums.auth.ResourceTypeEnum;
import cn.mnay.common.constant.Constants;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import cn.mnay.common.jpa.converter.JpaConverterList;
import cn.mnay.common.model.dbo.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@TableComment("权限表")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_PERMISSION, uniqueConstraints = {
        @UniqueConstraint(name = "auth_unique_idx_permission_name_type", columnNames = {"permission_name", "permission_type"})
})
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Permission extends BaseTimeEntity {

    // permissionName字段存储的值：
    // ①页面权限(固定数量)
    // ②资源操作权限(固定数量)
    @ColumnComment("权限名称")
    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    // 该页面对应的所有接口权限(fixme:后续添加)
    @ColumnComment("该页面对应的所有接口权限")
    @Column(name = "url_list", length = 2000)
    @Convert(converter = JpaConverterList.class)
    private List<String> urlList;

    // 区分权限类型
    @ColumnComment("权限类型")
    @Column(name = "permission_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermissionTypeEnum permissionType;

    @ColumnComment("别名")
    @Column(name = "alias")
    private String alias;

    @ColumnComment("资源类型")
    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private ResourceTypeEnum resourceType;

    // 只有是资源操作权限，才拥有值
    @ColumnComment("资源操作类型")
    @Column(name = "resource_operation_type")
    @Enumerated(EnumType.STRING)
    private ResourceOperationTypeEnum resourceOperationType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = Constants.AUTH_ROLE_PERMISSION,   // 中间表名称
            joinColumns = {@JoinColumn(
                    name = "permission_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )}
    )
    private List<Role> roleList;

}
