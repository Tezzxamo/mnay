package cn.mnay.common.model.dbo;


import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @IdGenerator
    @ColumnComment("id")
    private String id;

    @Column(name = "description", columnDefinition = " varchar(255) default ''")
    @ColumnComment("备注")
    private String description;

}
