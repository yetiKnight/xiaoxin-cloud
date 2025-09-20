package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.UsernameValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户名验证注解
 * <p>
 * 支持英文用户名和中文用户名验证，可自定义长度范围
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.USERNAME_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 用户名类型
     */
    UsernameType type() default UsernameType.ENGLISH;

    /**
     * 最小长度
     */
    int min() default 3;

    /**
     * 最大长度
     */
    int max() default 20;

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 是否允许下划线
     */
    boolean allowUnderscore() default true;

    /**
     * 是否允许数字
     */
    boolean allowDigit() default true;

    /**
     * 是否必须以字母开头
     */
    boolean mustStartWithLetter() default false;

    /**
     * 禁止的用户名列表
     */
    String[] blacklist() default {};

    /**
     * 用户名类型枚举
     */
    enum UsernameType {
        /**
         * 英文用户名（字母、数字、下划线）
         */
        ENGLISH,
        
        /**
         * 中文用户名（中文、字母、数字、下划线）
         */
        CHINESE,
        
        /**
         * 自定义（不进行格式验证，只验证长度和黑名单）
         */
        CUSTOM
    }
}
