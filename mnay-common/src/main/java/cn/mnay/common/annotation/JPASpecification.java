package cn.mnay.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JPASpecification {

    String filedName();

    /**
     * 是否仅支持equal条件，默认不使用，如果此属性为true，则不考虑其他匹配条件
     */
    boolean onlyEq() default false;

    /**
     * filedName 对应的 QueryCriteria
     */
    String queryCriteria() default "";

    /**
     * 是否是区间的开头(当supportSection为true时，此属性才有用处，才需要对应的字段)<br/>
     * 注意:标注此属性的字段不需要标注supportSection属性
     */
    boolean startOfSection() default false;
    /**
     * 是否是区间的尾部(当supportSection为true时，此属性才有用处，才需要对应的字段)<br/>
     * 注意:标注此属性的字段不需要标注supportSection属性
     */
    boolean endOfSection() default false;


}
