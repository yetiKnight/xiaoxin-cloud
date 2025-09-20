package com.xiaoxin.iam.common.validation.annotation;

import com.xiaoxin.iam.common.validation.ValidationMessage;
import com.xiaoxin.iam.common.validation.validator.DateRangeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期范围验证注解
 * <p>
 * 验证日期是否在指定范围内，支持多种日期类型和格式
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {

    /**
     * 验证失败时的错误消息
     */
    String message() default ValidationMessage.DATE_RANGE_INVALID;

    /**
     * 验证分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 最小日期（字符串格式）
     */
    String min() default "";

    /**
     * 最大日期（字符串格式）
     */
    String max() default "";

    /**
     * 日期格式
     */
    String pattern() default "yyyy-MM-dd";

    /**
     * 是否允许为空
     */
    boolean nullable() default true;

    /**
     * 相对日期基准
     */
    RelativeDate relative() default RelativeDate.NONE;

    /**
     * 相对天数偏移（负数表示过去，正数表示未来）
     */
    int offsetDays() default 0;

    /**
     * 是否包含边界值
     */
    boolean inclusive() default true;

    /**
     * 时区
     */
    String timezone() default "Asia/Shanghai";

    /**
     * 相对日期枚举
     */
    enum RelativeDate {
        /**
         * 无相对日期
         */
        NONE,
        
        /**
         * 相对于当前日期
         */
        TODAY,
        
        /**
         * 相对于当前时间
         */
        NOW,
        
        /**
         * 本周开始
         */
        WEEK_START,
        
        /**
         * 本月开始
         */
        MONTH_START,
        
        /**
         * 本年开始
         */
        YEAR_START
    }
}
