package cn.mnay.common.annotation.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 手机号码校验注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface Phone {

    /**
     * 报错信息
     */
    String message() default "手机号码格式不正确";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
