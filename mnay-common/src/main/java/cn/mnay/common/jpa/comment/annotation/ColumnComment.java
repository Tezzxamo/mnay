package cn.mnay.common.jpa.comment.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ColumnComment {
    String value() default "";
}
