package com.xiaoxin.iam.common.validation.validator;

import com.xiaoxin.iam.common.validation.annotation.DateRange;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * 日期范围验证器
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private String min;
    private String max;
    private String pattern;
    private boolean nullable;
    private DateRange.RelativeDate relative;
    private int offsetDays;
    private boolean inclusive;
    private ZoneId timezone;
    private DateTimeFormatter formatter;

    @Override
    public void initialize(DateRange dateRange) {
        this.min = dateRange.min();
        this.max = dateRange.max();
        this.pattern = dateRange.pattern();
        this.nullable = dateRange.nullable();
        this.relative = dateRange.relative();
        this.offsetDays = dateRange.offsetDays();
        this.inclusive = dateRange.inclusive();
        
        // 解析时区
        try {
            this.timezone = ZoneId.of(dateRange.timezone());
        } catch (Exception e) {
            this.timezone = ZoneId.of("Asia/Shanghai");
        }
        
        // 创建日期格式化器
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 如果值为空且允许为空，则验证通过
        if (value == null) {
            return nullable;
        }

        try {
            // 将输入值转换为LocalDateTime
            LocalDateTime dateTime = convertToLocalDateTime(value);
            if (dateTime == null) {
                return false;
            }

            // 获取比较基准日期
            LocalDateTime minDateTime = getMinDateTime();
            LocalDateTime maxDateTime = getMaxDateTime();

            // 验证范围
            return isInRange(dateTime, minDateTime, maxDateTime);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将输入值转换为LocalDateTime
     *
     * @param value 输入值
     * @return LocalDateTime对象
     */
    private LocalDateTime convertToLocalDateTime(Object value) {
        try {
            if (value instanceof String) {
                String dateStr = (String) value;
                if (dateStr.trim().isEmpty()) {
                    return null;
                }
                // 尝试解析为LocalDate或LocalDateTime
                try {
                    LocalDate date = LocalDate.parse(dateStr, formatter);
                    return date.atStartOfDay();
                } catch (DateTimeParseException e) {
                    return LocalDateTime.parse(dateStr, formatter);
                }
            } else if (value instanceof Date) {
                return ((Date) value).toInstant().atZone(timezone).toLocalDateTime();
            } else if (value instanceof LocalDate) {
                return ((LocalDate) value).atStartOfDay();
            } else if (value instanceof LocalDateTime) {
                return (LocalDateTime) value;
            } else if (value instanceof Long) {
                return new Date((Long) value).toInstant().atZone(timezone).toLocalDateTime();
            }
        } catch (Exception e) {
            // 解析失败
        }
        return null;
    }

    /**
     * 获取最小日期时间
     *
     * @return 最小日期时间
     */
    private LocalDateTime getMinDateTime() {
        if (min == null || min.trim().isEmpty()) {
            return null;
        }

        try {
            // 处理相对日期
            if (relative != DateRange.RelativeDate.NONE) {
                return getRelativeDateTime().plusDays(offsetDays);
            }

            // 解析固定日期
            try {
                LocalDate date = LocalDate.parse(min, formatter);
                return date.atStartOfDay();
            } catch (DateTimeParseException e) {
                return LocalDateTime.parse(min, formatter);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取最大日期时间
     *
     * @return 最大日期时间
     */
    private LocalDateTime getMaxDateTime() {
        if (max == null || max.trim().isEmpty()) {
            return null;
        }

        try {
            // 处理相对日期
            if (relative != DateRange.RelativeDate.NONE) {
                return getRelativeDateTime().plusDays(offsetDays);
            }

            // 解析固定日期
            try {
                LocalDate date = LocalDate.parse(max, formatter);
                return date.atTime(23, 59, 59, 999999999);
            } catch (DateTimeParseException e) {
                return LocalDateTime.parse(max, formatter);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取相对日期时间
     *
     * @return 相对日期时间
     */
    private LocalDateTime getRelativeDateTime() {
        LocalDateTime now = LocalDateTime.now(timezone);
        
        switch (relative) {
            case TODAY:
                return now.toLocalDate().atStartOfDay();
            case NOW:
                return now;
            case WEEK_START:
                return now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                          .toLocalDate().atStartOfDay();
            case MONTH_START:
                return now.with(TemporalAdjusters.firstDayOfMonth())
                          .toLocalDate().atStartOfDay();
            case YEAR_START:
                return now.with(TemporalAdjusters.firstDayOfYear())
                          .toLocalDate().atStartOfDay();
            default:
                return now;
        }
    }

    /**
     * 检查日期是否在范围内
     *
     * @param dateTime    待验证日期
     * @param minDateTime 最小日期
     * @param maxDateTime 最大日期
     * @return 是否在范围内
     */
    private boolean isInRange(LocalDateTime dateTime, LocalDateTime minDateTime, LocalDateTime maxDateTime) {
        if (minDateTime != null) {
            if (inclusive) {
                if (dateTime.isBefore(minDateTime)) {
                    return false;
                }
            } else {
                if (dateTime.isBefore(minDateTime) || dateTime.isEqual(minDateTime)) {
                    return false;
                }
            }
        }

        if (maxDateTime != null) {
            if (inclusive) {
                if (dateTime.isAfter(maxDateTime)) {
                    return false;
                }
            } else {
                if (dateTime.isAfter(maxDateTime) || dateTime.isEqual(maxDateTime)) {
                    return false;
                }
            }
        }

        return true;
    }
}
