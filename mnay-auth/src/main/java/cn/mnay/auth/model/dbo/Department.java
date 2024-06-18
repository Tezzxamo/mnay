package cn.mnay.auth.model.dbo;

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

import java.util.List;

@TableComment("部门表")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_DEPARTMENT, uniqueConstraints = {
        @UniqueConstraint(name = "auth_department_unidx_company_id_department_name", columnNames = {"company_id", "department_name"})
})
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Department extends BaseTimeUserEntity {

    @ColumnComment("部门名称")
    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @ColumnComment("根部门(一级部门为空，所有子级部门为一级部门的id)")
    @Column(name = "root_department_id")
    private String rootDepartmentId;

    @ColumnComment("上级部门(可为空)")
    @Column(name = "parent_department_id")
    private String parentDepartmentId;

    @ColumnComment("第几级部门")
    @Column(name = "department_level", nullable = false)
    private Integer departmentLevel;

    @ColumnComment("部门邮箱")
    @Column(name = "department_email")
    private String departmentEmail;

    @ColumnComment("部门电话")
    @Column(name = "department_phone")
    private String departmentPhone;

    @ColumnComment("部门地址")
    @Column(name = "department_address")
    private String departmentAddress;

    @ColumnComment("部门邮编")
    @Column(name = "department_post_code")
    private String departmentPostCode;

    @ColumnComment("部门状态(是否启用)")
    @Column(name = "department_status", nullable = false)
    private Boolean departmentStatus;

    /**
     * 关联成员：部门负责人
     */
    @Column(name = "in_charge_member_id")
    @ColumnComment("部门负责人ID")
    private String inChargeMemberId;

    /**
     * 所属组织：公司表
     */
    @ManyToOne(fetch = FetchType.EAGER)                              // 不产生中间表，需要即刻获取到公司的数据
    @JoinColumn(name = "company_id", referencedColumnName = "id")    // 在department表中只有一个company_id外键
    private Company company;

    /**
     * 自关联,查询出来的时候是[所有的子部门]
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_department_id")
    private List<Department> departmentList;

}
