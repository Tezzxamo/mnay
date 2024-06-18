package cn.mnay.common.model.dbo;

import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(SnowflakeIdGenerator.class)
@ValueGenerationType(generatedBy = SnowflakeIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IdGenerator {
}