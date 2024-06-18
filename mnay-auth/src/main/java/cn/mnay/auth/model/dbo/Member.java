package cn.mnay.auth.model.dbo;

import cn.mnay.common.enums.auth.MemberTypeEnum;
import cn.mnay.common.constant.Constants;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import cn.mnay.common.model.dbo.BaseTimeUserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 成员表的属性继承自用户表，成员属于部门，但用户可以不归属于部门，比如系统管理员用户、临时租户等
 */
@TableComment("成员表(继承自用户表)")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_MEMBER)
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Member extends BaseTimeUserEntity {

    @ColumnComment("用户工号")
    @Column(name = "member_no", nullable = false)
    private String memberNo;

    @ColumnComment("用户名")
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @ColumnComment("密码")
    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    @ColumnComment("电话")
    @Column(name = "member_phone")
    private String memberPhone;

    @ColumnComment("邮箱(唯一)")
    @Column(name = "member_email", unique = true)
    private String memberEmail;

    @ColumnComment("成员类型")
    @Column(name = "member_type")
    @Enumerated(EnumType.STRING)
    private MemberTypeEnum memberType;

    @ColumnComment("是否锁定")
    @Column(name = "locked")
    private Boolean locked;

    @ColumnComment("最近登录时间")
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @ColumnComment("最近锁定时间")
    @Column(name = "locked_time")
    private LocalDateTime lockedTime;

    @ColumnComment("最近密码修改时间")
    @Column(name = "password_modify_time")
    private LocalDateTime passwordModifyTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = Constants.AUTH_MEMBER_DEPARTMENT,   // 中间表名称
            uniqueConstraints = {@UniqueConstraint(
                    name = Constants.INDEX_MEMBER_DEPARTMENT,
                    columnNames = {"member_id", "department_id"}
            )},
            joinColumns = {@JoinColumn(
                    name = "member_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "department_id",
                    referencedColumnName = "id"
            )}
    )
    private Department department;

    @ManyToMany(fetch = FetchType.EAGER)    // 需要即刻获取到成员的所有角色
    @JoinTable(
            name = Constants.AUTH_MEMBER_ROLE,   // 中间表名称
            uniqueConstraints = {@UniqueConstraint(
                    name = Constants.INDEX_MEMBER_ROLE,
                    columnNames = {"member_id", "role_id"}
            )},
            joinColumns = {@JoinColumn(
                    name = "member_id",
                    referencedColumnName = "id"
            )},
            inverseJoinColumns = {@JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )}
    )
    private List<Role> roleList;

}
