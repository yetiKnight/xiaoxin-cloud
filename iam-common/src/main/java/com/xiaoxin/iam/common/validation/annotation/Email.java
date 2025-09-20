package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.EmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 邮箱验证注解
 * <p>
 * 支持标准邮箱格式和严格邮箱格式验证
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.EMAIL_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 是否使用严格模式验证
     */
    boolean strict() default false;

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 允许的邮箱域名（空数组表示不限制）
     */
    String[] allowedDomains() default {};

    /**
     * 禁止的邮箱域名
     */
    String[] blockedDomains() default {};
}
