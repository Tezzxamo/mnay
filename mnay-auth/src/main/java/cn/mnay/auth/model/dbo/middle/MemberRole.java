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

/**
 * 中间表，部分中间表的数据更新直接使用中间表，而不是关联表，可以提高更新效率(如:组织或部门负责人更新)
 * (用户的角色更新可以直接使用Member表进行更新，不使用中间表)
 */
@TableComment("成员-角色表")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_MEMBER_ROLE)
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class MemberRole extends BaseEntity {

    @ColumnComment("成员id")
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @ColumnComment("角色id")
    @Column(name = "role_id", nullable = false)
    private String roleId;

}
