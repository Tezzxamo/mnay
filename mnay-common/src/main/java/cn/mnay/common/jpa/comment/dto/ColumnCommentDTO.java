package cn.mnay.common.jpa.comment.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ColumnCommentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String comment;
    private boolean important;

    public ColumnCommentDTO() {
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ColumnCommentDTO other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object thisName = this.getName();
                Object otherName = other.getName();
                if (thisName == null) {
                    if (otherName != null) {
                        return false;
                    }
                } else if (!thisName.equals(otherName)) {
                    return false;
                }
                Object thisComment = this.getComment();
                Object otherComment = other.getComment();
                if (thisComment == null) {
                    return otherComment == null;
                } else {
                    return thisComment.equals(otherComment);
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ColumnCommentDTO;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object name = this.getName();
        result = result * 59 + (name == null ? 43 : name.hashCode());
        Object comment = this.getComment();
        result = result * 59 + (comment == null ? 43 : comment.hashCode());
        return result;
    }


    @Override
    public String toString() {
        return "ColumnCommentDTO{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", important=" + important +
                '}';
    }
}