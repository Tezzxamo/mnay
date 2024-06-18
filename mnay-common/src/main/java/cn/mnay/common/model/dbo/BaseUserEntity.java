package cn.mnay.common.model.dbo;

import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @IdGenerator
    private String id;

    @CreatedBy
    @ColumnComment("创建用户")
    @Column(name = "create_user", nullable = false, updatable = false)
    private String createUser;
    @LastModifiedBy
    @ColumnComment("更新用户")
    @Column(name = "update_user", nullable = false)
    private String updateUser;

    @Column(name = "description", columnDefinition = " varchar(255) default ''")
    @ColumnComment("备注")
    private String description;

}
