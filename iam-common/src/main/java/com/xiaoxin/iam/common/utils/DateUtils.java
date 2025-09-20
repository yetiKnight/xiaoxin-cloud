package com.xiaoxin.iam.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * 日期时间工具类
 * 提供常用的日期时间操作方法
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class DateUtils {

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    
    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 紧凑日期时间格式
     */
    public static final String COMPACT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    
    /**
     * ISO 8601 格式
     */
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 私有构造函数，防止实例化
     */
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 获取当前时间 ====================

    /**
     * 获取当前日期
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间
     */
    public static LocalTime nowTime() {
        return LocalTime.now();
    }

    /**
     * 获取当前日期时间
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前时间戳（毫秒）
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间戳（秒）
     */
    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    // ==================== 日期格式化 ====================

    /**
     * 格式化日期
     */
    public static String format(LocalDate date) {
        return format(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化时间
     */
    public static String format(LocalTime time) {
        return format(time, DEFAULT_TIME_FORMAT);
    }

    /**
     * 格式化时间
     */
    public static String format(LocalTime time, String pattern) {
        if (time == null) {
            return null;
        }
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期时间
     */
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 格式化日期时间
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化当前日期时间
     */
    public static String formatNow() {
        return formatNow(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 格式化当前日期时间
     */
    public static String formatNow(String pattern) {
        return nowDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== 日期解析 ====================

    /**
     * 解析日期字符串
     */
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 解析日期字符串
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析时间字符串
     */
    public static LocalTime parseTime(String timeStr) {
        return parseTime(timeStr, DEFAULT_TIME_FORMAT);
    }

    /**
     * 解析时间字符串
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期时间字符串
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return parseDateTime(dateTimeStr, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 解析日期时间字符串
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== 日期计算 ====================

    /**
     * 获取指定日期的开始时间（00:00:00）
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * 获取指定日期的结束时间（23:59:59.999999999）
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 获取指定日期的开始时间
     */
    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取指定日期的结束时间
     */
    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 获取月份的第一天
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取月份的最后一天
     */
    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取年份的第一天
     */
    public static LocalDate firstDayOfYear(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取年份的最后一天
     */
    public static LocalDate lastDayOfYear(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    // ==================== 日期加减 ====================

    /**
     * 日期加天数
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date.plusDays(days);
    }

    /**
     * 日期减天数
     */
    public static LocalDate minusDays(LocalDate date, long days) {
        return date.minusDays(days);
    }

    /**
     * 日期加月数
     */
    public static LocalDate plusMonths(LocalDate date, long months) {
        return date.plusMonths(months);
    }

    /**
     * 日期减月数
     */
    public static LocalDate minusMonths(LocalDate date, long months) {
        return date.minusMonths(months);
    }

    /**
     * 日期加年数
     */
    public static LocalDate plusYears(LocalDate date, long years) {
        return date.plusYears(years);
    }

    /**
     * 日期减年数
     */
    public static LocalDate minusYears(LocalDate date, long years) {
        return date.minusYears(years);
    }

    /**
     * 日期时间加天数
     */
    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime.plusDays(days);
    }

    /**
     * 日期时间减天数
     */
    public static LocalDateTime minusDays(LocalDateTime dateTime, long days) {
        return dateTime.minusDays(days);
    }

    /**
     * 日期时间加小时
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime.plusHours(hours);
    }

    /**
     * 日期时间减小时
     */
    public static LocalDateTime minusHours(LocalDateTime dateTime, long hours) {
        return dateTime.minusHours(hours);
    }

    /**
     * 日期时间加分钟
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.plusMinutes(minutes);
    }

    /**
     * 日期时间减分钟
     */
    public static LocalDateTime minusMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime.minusMinutes(minutes);
    }

    /**
     * 日期时间加秒数
     */
    public static LocalDateTime plusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime.plusSeconds(seconds);
    }

    /**
     * 日期时间减秒数
     */
    public static LocalDateTime minusSeconds(LocalDateTime dateTime, long seconds) {
        return dateTime.minusSeconds(seconds);
    }

    // ==================== 日期比较 ====================

    /**
     * 比较两个日期的大小
     * @return 负数表示date1小于date2，0表示相等，正数表示date1大于date2
     */
    public static int compare(LocalDate date1, LocalDate date2) {
        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    /**
     * 比较两个日期时间的大小
     * @return 负数表示dateTime1小于dateTime2，0表示相等，正数表示dateTime1大于dateTime2
     */
    public static int compare(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null && dateTime2 == null) {
            return 0;
        }
        if (dateTime1 == null) {
            return -1;
        }
        if (dateTime2 == null) {
            return 1;
        }
        return dateTime1.compareTo(dateTime2);
    }

    /**
     * 判断日期1是否在日期2之前
     */
    public static boolean isBefore(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) < 0;
    }

    /**
     * 判断日期1是否在日期2之后
     */
    public static boolean isAfter(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) > 0;
    }

    /**
     * 判断两个日期是否相等
     */
    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        return compare(date1, date2) == 0;
    }

    /**
     * 判断日期时间1是否在日期时间2之前
     */
    public static boolean isBefore(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) < 0;
    }

    /**
     * 判断日期时间1是否在日期时间2之后
     */
    public static boolean isAfter(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) > 0;
    }

    /**
     * 判断两个日期时间是否相等
     */
    public static boolean isEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return compare(dateTime1, dateTime2) == 0;
    }

    // ==================== 日期差值计算 ====================

    /**
     * 计算两个日期之间的天数差
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个日期时间之间的小时差
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的分钟差
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的秒数差
     */
    public static long secondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.SECONDS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的毫秒差
     */
    public static long millisecondsBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.MILLIS.between(startDateTime, endDateTime);
    }

    // ==================== 时间戳转换 ====================

    /**
     * 将时间戳转换为LocalDateTime
     */
    public static LocalDateTime timestampToDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * 将LocalDateTime转换为时间戳
     */
    public static long dateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 将时间戳转换为LocalDate
     */
    public static LocalDate timestampToDate(long timestamp) {
        return timestampToDateTime(timestamp).toLocalDate();
    }

    /**
     * 将LocalDate转换为时间戳（当天开始时间）
     */
    public static long dateToTimestamp(LocalDate date) {
        return dateTimeToTimestamp(date.atStartOfDay());
    }

    // ==================== 日期判断 ====================

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeapYear(LocalDate date) {
        return date.isLeapYear();
    }

    /**
     * 判断是否为今天
     */
    public static boolean isToday(LocalDate date) {
        return isEqual(date, now());
    }

    /**
     * 判断是否为今天
     */
    public static boolean isToday(LocalDateTime dateTime) {
        return isEqual(dateTime.toLocalDate(), now());
    }

    /**
     * 判断是否为昨天
     */
    public static boolean isYesterday(LocalDate date) {
        return isEqual(date, now().minusDays(1));
    }

    /**
     * 判断是否为明天
     */
    public static boolean isTomorrow(LocalDate date) {
        return isEqual(date, now().plusDays(1));
    }

    /**
     * 判断是否为本周
     */
    public static boolean isThisWeek(LocalDate date) {
        LocalDate now = now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return !isBefore(date, startOfWeek) && !isAfter(date, endOfWeek);
    }

    /**
     * 判断是否为本月
     */
    public static boolean isThisMonth(LocalDate date) {
        LocalDate now = now();
        return date.getYear() == now.getYear() && date.getMonth() == now.getMonth();
    }

    /**
     * 判断是否为本年
     */
    public static boolean isThisYear(LocalDate date) {
        return date.getYear() == now().getYear();
    }

    // ==================== 日期范围 ====================

    /**
     * 获取日期范围（开始日期到结束日期之间的所有日期）
     */
    public static java.util.List<LocalDate> getDateRange(LocalDate startDate, LocalDate endDate) {
        java.util.List<LocalDate> dates = new java.util.ArrayList<>();
        if (startDate == null || endDate == null || isAfter(startDate, endDate)) {
            return dates;
        }
        
        LocalDate current = startDate;
        while (!isAfter(current, endDate)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        return dates;
    }

    /**
     * 获取指定日期的周范围（周一到周日）
     */
    public static java.util.List<LocalDate> getWeekRange(LocalDate date) {
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return getDateRange(startOfWeek, endOfWeek);
    }

    /**
     * 获取指定日期的月范围
     */
    public static java.util.List<LocalDate> getMonthRange(LocalDate date) {
        LocalDate startOfMonth = firstDayOfMonth(date);
        LocalDate endOfMonth = lastDayOfMonth(date);
        return getDateRange(startOfMonth, endOfMonth);
    }

    // ==================== 其他工具方法 ====================

    /**
     * 获取年龄
     */
    public static int getAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(birthDate, now());
    }

    /**
     * 获取年龄
     */
    public static int getAge(LocalDate birthDate, LocalDate currentDate) {
        if (birthDate == null || currentDate == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(birthDate, currentDate);
    }

    /**
     * 获取星期几（中文）
     */
    public static String getDayOfWeekChinese(LocalDate date) {
        if (date == null) {
            return null;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY: return "星期一";
            case TUESDAY: return "星期二";
            case WEDNESDAY: return "星期三";
            case THURSDAY: return "星期四";
            case FRIDAY: return "星期五";
            case SATURDAY: return "星期六";
            case SUNDAY: return "星期日";
            default: return null;
        }
    }

    /**
     * 获取星期几（英文）
     */
    public static String getDayOfWeekEnglish(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.getDayOfWeek().name();
    }

    /**
     * 获取月份（中文）
     */
    public static String getMonthChinese(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.getMonthValue() + "月";
    }

    /**
     * 获取年份
     */
    public static int getYear(LocalDate date) {
        return date != null ? date.getYear() : 0;
    }

    /**
     * 获取月份
     */
    public static int getMonth(LocalDate date) {
        return date != null ? date.getMonthValue() : 0;
    }

    /**
     * 获取日期
     */
    public static int getDay(LocalDate date) {
        return date != null ? date.getDayOfMonth() : 0;
    }

    /**
     * 获取小时
     */
    public static int getHour(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.getHour() : 0;
    }

    /**
     * 获取分钟
     */
    public static int getMinute(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.getMinute() : 0;
    }

    /**
     * 获取秒数
     */
    public static int getSecond(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.getSecond() : 0;
    }
}
