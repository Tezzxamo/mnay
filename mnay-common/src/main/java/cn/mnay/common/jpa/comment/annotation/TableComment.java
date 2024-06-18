package cn.mnay.common.jpa.comment.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TableComment {
    String value() default "";
}
