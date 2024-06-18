package cn.mnay.auth.model.dbo;

import cn.mnay.common.constant.Constants;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import cn.mnay.common.model.dbo.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@TableComment("角色表")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_ROLE)
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Role extends BaseTimeEntity {

    @ColumnComment("组织id")
    @Column(name = "company_id", nullable = false)
    private String companyId;

    @ColumnComment("部门id")
    @Column(name = "department_id")
    private String departmentId;

    @ColumnComment("相连的部门id")
    @Column(name = "concat_department_id")
    private String concatDepartmentId;

    @ColumnComment("角色名")
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ColumnComment("别名")
    @Column(name = "alias")
    private String alias;

    @ColumnComment("是否是管理员用户(无论是系统/组织/部门管理员)")
    @Column(name = "admin", nullable = false)
    private boolean admin;

    @ColumnComment("该角色是否支持更新修改(默认角色不支持修改，新建角色可以修改)")
    @Column(name = "update", nullable = false)
    private boolean update;

    @ManyToMany(fetch = FetchType.LAZY) // role不一定需要使用members的相关数据，所以可以lazy
    @JoinTable(
            name = Constants.AUTH_MEMBER_ROLE,
            joinColumns = {@JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "member_id",
                    referencedColumnName = "id"
            )}
    )
    private List<Member> memberList;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = Constants.AUTH_ROLE_PERMISSION,   // 中间表名称
            uniqueConstraints = {@UniqueConstraint(
                    name = Constants.INDEX_ROLE_PERMISSION,
                    columnNames = {"role_id", "permission_id"}
            )},
            joinColumns = {@JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "permission_id",
                    referencedColumnName = "id"
            )}
    )
    private List<Permission> permissionList;

}
