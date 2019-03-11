package com.meta.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.meta.enums.SensitiveTypeEnum;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标明敏感字段的注解
 */
@Target({FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface Mask {

    /** 敏感字段类型 */
    MaskTypeEnum type();

    /** 该字段在http响应中是否也需要脱敏，默认不需要 */
    boolean needMaskingInResponse() default false;
}
