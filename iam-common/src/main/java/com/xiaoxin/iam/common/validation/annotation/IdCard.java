package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.IdCardValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 身份证号验证注解
 * <p>
 * 支持15位和18位身份证号验证
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = IdCardValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IdCard {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.ID_CARD_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 身份证类型
     */
    IdCardType type() default IdCardType.BOTH;

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 是否验证校验码（仅对18位身份证有效）
     */
    boolean checkCode() default true;

    /**
     * 身份证类型枚举
     */
    enum IdCardType {
        /**
         * 15位身份证
         */
        ID_15,
        
        /**
         * 18位身份证
         */
        ID_18,
        
        /**
         * 兼容15位和18位
         */
        BOTH
    }
}
