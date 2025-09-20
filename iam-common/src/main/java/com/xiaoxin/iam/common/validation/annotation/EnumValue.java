package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.EnumValueValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举值验证注解
 * <p>
 * 验证字符串值是否为指定枚举类的有效值
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.ENUM_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 是否忽略大小写
     */
    boolean ignoreCase() default false;

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 允许的枚举值（如果指定，则只允许这些值）
     */
    String[] allowedValues() default {};

    /**
     * 禁止的枚举值
     */
    String[] forbiddenValues() default {};

    /**
     * 验证方法（默认使用name()方法）
     */
    String method() default "name";
}
