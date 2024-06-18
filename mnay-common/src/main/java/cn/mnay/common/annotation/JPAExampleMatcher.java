package cn.mnay.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 灵活匹配只支持String类型，其他类型只支持精准匹配，所以只有String类型需要使用该注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JPAExampleMatcher {
    String filedName();

    boolean eq() default false;
    boolean fuzzyQuery() default false;
    boolean startsWith() default false;
    boolean endsWith() default false;
    boolean ignoreFiled() default false;


}
