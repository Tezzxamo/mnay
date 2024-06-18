package cn.mnay.auth.model.dbo.middle;

import cn.mnay.common.constant.Constants;
import cn.mnay.common.model.dbo.BaseEntity;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@TableComment("角色-权限表")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_ROLE_PERMISSION)
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class RolePermission extends BaseEntity {

    @ColumnComment("角色id")
    @Column(name = "role_id", nullable = false)
    private String roleId;

    @ColumnComment("权限id")
    @Column(name = "permission_id", nullable = false)
    private String permissionId;

}
