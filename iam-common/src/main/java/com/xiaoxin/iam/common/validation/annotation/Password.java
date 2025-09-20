package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.PasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 密码强度验证注解
 * <p>
 * 支持多种密码强度验证策略
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.PASSWORD_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 密码强度等级
     */
    PasswordStrength strength() default PasswordStrength.MEDIUM;

    /**
     * 最小长度
     */
    int min() default 8;

    /**
     * 最大长度
     */
    int max() default 32;

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 是否必须包含大写字母
     */
    boolean requireUppercase() default false;

    /**
     * 是否必须包含小写字母
     */
    boolean requireLowercase() default false;

    /**
     * 是否必须包含数字
     */
    boolean requireDigit() default false;

    /**
     * 是否必须包含特殊字符
     */
    boolean requireSpecialChar() default false;

    /**
     * 允许的特殊字符
     */
    String allowedSpecialChars() default "!@#$%^&*()_+-=[]{}|;:,.<>?";

    /**
     * 是否禁止连续相同字符
     */
    boolean forbidConsecutiveSame() default false;

    /**
     * 连续相同字符的最大数量
     */
    int maxConsecutiveSame() default 3;

    /**
     * 是否禁止常见弱密码
     */
    boolean forbidCommonPasswords() default true;

    /**
     * 密码强度等级枚举
     */
    enum PasswordStrength {
        /**
         * 弱密码：只需要长度要求
         */
        WEAK,
        
        /**
         * 中等密码：至少包含字母和数字
         */
        MEDIUM,
        
        /**
         * 强密码：包含大小写字母、数字和特殊字符
         */
        STRONG,
        
        /**
         * 自定义：根据其他参数自定义强度要求
         */
        CUSTOM
    }
}
