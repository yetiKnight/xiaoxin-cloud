package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.StringLengthValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字符串长度范围验证注解
 * <p>
 * 验证字符串长度是否在指定范围内，支持字符数和字节数两种计算方式
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = StringLengthValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringLength {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.STRING_LENGTH_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 最小长度
     */
    int min() default 0;

    /**
     * 最大长度
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 长度计算方式
     */
    LengthType lengthType() default LengthType.CHARACTER;

    /**
     * 字符编码（仅在按字节计算时使用）
     */
    String charset() default "UTF-8";

    /**
     * 是否去除前后空白字符再计算长度
     */
    boolean trim() default false;

    /**
     * 是否允许空字符串（当nullable=false时有效）
     */
    boolean allowEmpty() default true;

    /**
     * 长度计算类型枚举
     */
    enum LengthType {
        /**
         * 按字符数计算
         */
        CHARACTER,
        
        /**
         * 按字节数计算
         */
        BYTE
    }
}
