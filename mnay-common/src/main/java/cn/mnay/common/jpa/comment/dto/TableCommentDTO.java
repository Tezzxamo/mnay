package cn.mnay.common.jpa.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
public class TableCommentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String comment;
    private String schema;
    private List<ColumnCommentDTO> columnCommentDTOList;


    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TableCommentDTO other)) {
            return false;
        } else {
            if (!other.canEqual(this)) {
                return false;
            } else {
                label:
                {
                    Object thisName = this.getName();
                    Object otherName = other.getName();
                    if (thisName == null) {
                        if (otherName == null) {
                            break label;
                        }
                    } else if (thisName.equals(otherName)) {
                        break label;
                    }
                    return false;
                }

                Object thisComment = this.getComment();
                Object otherComment = other.getComment();
                if (thisComment == null) {
                    if (otherComment != null) {
                        return false;
                    }
                } else if (!thisComment.equals(otherComment)) {
                    return false;
                }

                Object thisSchema = this.getSchema();
                Object otherSchema = other.getSchema();
                if (thisSchema == null) {
                    if (otherSchema != null) {
                        return false;
                    }
                } else if (!thisSchema.equals(otherSchema)) {
                    return false;
                }

                Object thisColumnCommentDTOList = this.getColumnCommentDTOList();
                Object otherColumnCommentDTOList = other.getColumnCommentDTOList();
                if (thisColumnCommentDTOList == null) {
                    return otherColumnCommentDTOList == null;
                } else {
                    return thisColumnCommentDTOList.equals(otherColumnCommentDTOList);
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TableCommentDTO;
    }

    @Override
    public int hashCode() {
//        int PRIME = true;
        int result = 1;
        Object name = this.getName();
        result = result * 59 + (name == null ? 43 : name.hashCode());
        Object comment = this.getComment();
        result = result * 59 + (comment == null ? 43 : comment.hashCode());
        Object schema = this.getSchema();
        result = result * 59 + (schema == null ? 43 : schema.hashCode());
        Object columnCommentDTOList = this.getColumnCommentDTOList();
        result = result * 59 + (columnCommentDTOList == null ? 43 : columnCommentDTOList.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "TableCommentDTO(name=" + this.getName() + ", comment=" + this.getComment() + ", schema=" + this.getSchema() + ", columnCommentDTOList=" + this.getColumnCommentDTOList() + ")";
    }

}
