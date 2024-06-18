package cn.mnay.common.model.dbo;

import cn.mnay.common.jpa.comment.annotation.ColumnComment;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @IdGenerator
    private String id;

    @CreatedDate
    @ColumnComment("创建时间")
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;
    @LastModifiedDate
    @ColumnComment("更新时间")
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "description", columnDefinition = " varchar(255) default ''")
    @ColumnComment("备注")
    private String description;
}
