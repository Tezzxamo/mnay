package cn.mnay.auth.model.dbo;

import cn.mnay.common.constant.Constants;
import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import cn.mnay.common.jpa.comment.annotation.TableComment;
import cn.mnay.common.model.dbo.BaseTimeUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@TableComment("组织表")
@Table(schema = Constants.MYSQL_SCHEMA, name = Constants.AUTH_COMPANY)
@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Accessors(chain = true)
public class Company extends BaseTimeUserEntity {

    @ColumnComment("组织名称")
    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @ColumnComment("组织邮箱")
    @Column(name = "company_email")
    private String companyEmail;

    @ColumnComment("组织电话")
    @Column(name = "company_phone")
    private String companyPhone;

    @ColumnComment("组织地址")
    @Column(name = "company_address")
    private String companyAddress;

    @ColumnComment("组织邮编")
    @Column(name = "company_post_code")
    private String companyPostCode;

    @ColumnComment("组织状态(是否启用)")
    @Column(name = "company_status", nullable = false)
    private Boolean companyStatus;

    /**
     * 关联成员:组织负责人
     */
    @Column(name = "in_charge_member_id")
    @ColumnComment("组织负责人ID")
    private String inChargeMemberId;

}
