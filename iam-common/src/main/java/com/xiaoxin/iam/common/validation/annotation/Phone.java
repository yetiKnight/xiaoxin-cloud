package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.PhoneValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 手机号验证注解
 * <p>
 * 支持中国大陆手机号和国际手机号验证
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.PHONE_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 手机号类型
     */
    PhoneType type() default PhoneType.CN;

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 手机号类型枚举
     */
    enum PhoneType {
        /**
         * 中国大陆手机号
         */
        CN,
        
        /**
         * 国际手机号
         */
        INTERNATIONAL,
        
        /**
         * 任意类型（优先匹配中国大陆）
         */
        ANY
    }
}
