package cn.mnay.auth.model.dbo.middle;

import cn.mnay.common.constant.Constants;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import cn.mnay.common.model.dbo.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@TableComment("成员-部门表")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_MEMBER_DEPARTMENT)
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class MemberDepartment extends BaseEntity {

    @ColumnComment("成员id")
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @ColumnComment("部门id")
    @Column(name = "department_id", nullable = false)
    private String departmentId;

}
