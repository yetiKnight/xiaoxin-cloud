package com.xiaoxin.iam.common.utils;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * 提供常用的数据验证方法
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class ValidationUtils {

    /**
     * 私有构造函数，防止实例化
     */
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 正则表达式常量 ====================

    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * 手机号正则表达式
     */
    public static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    /**
     * 身份证号正则表达式
     */
    public static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * URL正则表达式
     */
    public static final String URL_REGEX = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    /**
     * IP地址正则表达式
     */
    public static final String IP_REGEX = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    /**
     * 中文正则表达式
     */
    public static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]";

    /**
     * 数字正则表达式
     */
    public static final String NUMBER_REGEX = "\\d+";

    /**
     * 字母正则表达式
     */
    public static final String LETTER_REGEX = "[a-zA-Z]+";

    /**
     * 字母数字正则表达式
     */
    public static final String LETTER_NUMBER_REGEX = "[a-zA-Z0-9]+";

    /**
     * 用户名正则表达式（3-20位字母数字下划线）
     */
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";

    /**
     * 密码正则表达式（6-20位字母数字特殊字符）
     */
    public static final String PASSWORD_REGEX = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{6,20}$";

    /**
     * 强密码正则表达式（至少包含大小写字母、数字、特殊字符）
     */
    public static final String STRONG_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

    // ==================== 基础验证 ====================

    /**
     * 验证字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 验证字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 验证字符串是否为空（包括空白字符）
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 验证字符串是否不为空（包括空白字符）
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 验证对象是否为null
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 验证对象是否不为null
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    // ==================== 长度验证 ====================

    /**
     * 验证字符串长度是否在指定范围内
     */
    public static boolean isLengthBetween(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    /**
     * 验证字符串长度是否等于指定值
     */
    public static boolean isLengthEqual(String str, int length) {
        return str != null && str.length() == length;
    }

    /**
     * 验证字符串长度是否大于指定值
     */
    public static boolean isLengthGreaterThan(String str, int length) {
        return str != null && str.length() > length;
    }

    /**
     * 验证字符串长度是否小于指定值
     */
    public static boolean isLengthLessThan(String str, int length) {
        return str != null && str.length() < length;
    }

    // ==================== 数字验证 ====================

    /**
     * 验证字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证字符串是否为整数
     */
    public static boolean isInteger(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证字符串是否为长整数
     */
    public static boolean isLong(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证数字是否在指定范围内
     */
    public static boolean isNumberBetween(String str, double min, double max) {
        if (!isNumeric(str)) {
            return false;
        }
        try {
            double value = Double.parseDouble(str);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证整数是否在指定范围内
     */
    public static boolean isIntegerBetween(String str, int min, int max) {
        if (!isInteger(str)) {
            return false;
        }
        try {
            int value = Integer.parseInt(str);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ==================== 邮箱验证 ====================

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * 验证邮箱格式（严格模式）
     */
    public static boolean isEmailStrict(String email) {
        if (isEmpty(email)) {
            return false;
        }
        // 更严格的邮箱验证
        String strictEmailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(strictEmailRegex, email) && email.length() <= 254;
    }

    // ==================== 手机号验证 ====================

    /**
     * 验证手机号格式
     */
    public static boolean isPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(PHONE_REGEX, phone);
    }

    /**
     * 验证手机号格式（支持国际号码）
     */
    public static boolean isPhoneInternational(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        // 支持国际手机号格式
        String internationalPhoneRegex = "^\\+?[1-9]\\d{1,14}$";
        return Pattern.matches(internationalPhoneRegex, phone);
    }

    // ==================== 身份证验证 ====================

    /**
     * 验证身份证号格式
     */
    public static boolean isIdCard(String idCard) {
        if (isEmpty(idCard)) {
            return false;
        }
        return Pattern.matches(ID_CARD_REGEX, idCard);
    }

    /**
     * 验证身份证号格式（包含校验位验证）
     */
    public static boolean isIdCardWithChecksum(String idCard) {
        if (!isIdCard(idCard)) {
            return false;
        }
        return validateIdCardChecksum(idCard);
    }

    /**
     * 验证身份证校验位
     */
    private static boolean validateIdCardChecksum(String idCard) {
        if (idCard.length() != 18) {
            return false;
        }
        
        // 权重因子
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 校验码
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Character.getNumericValue(idCard.charAt(i)) * weights[i];
        }
        
        int remainder = sum % 11;
        char checkCode = checkCodes[remainder];
        
        return Character.toUpperCase(idCard.charAt(17)) == checkCode;
    }

    // ==================== URL验证 ====================

    /**
     * 验证URL格式
     */
    public static boolean isUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        return Pattern.matches(URL_REGEX, url);
    }

    /**
     * 验证HTTP URL格式
     */
    public static boolean isHttpUrl(String url) {
        if (isEmpty(url)) {
            return false;
        }
        return url.startsWith("http://") || url.startsWith("https://");
    }

    // ==================== IP地址验证 ====================

    /**
     * 验证IP地址格式
     */
    public static boolean isIpAddress(String ip) {
        if (isEmpty(ip)) {
            return false;
        }
        return Pattern.matches(IP_REGEX, ip);
    }

    /**
     * 验证IPv4地址格式
     */
    public static boolean isIPv4(String ip) {
        return isIpAddress(ip);
    }

    /**
     * 验证IPv6地址格式
     */
    public static boolean isIPv6(String ip) {
        if (isEmpty(ip)) {
            return false;
        }
        // 简化的IPv6验证
        String ipv6Regex = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
        return Pattern.matches(ipv6Regex, ip);
    }

    // ==================== 中文验证 ====================

    /**
     * 验证字符串是否包含中文
     */
    public static boolean containsChinese(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return Pattern.matches(".*" + CHINESE_REGEX + ".*", str);
    }

    /**
     * 验证字符串是否全为中文
     */
    public static boolean isAllChinese(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return Pattern.matches("^" + CHINESE_REGEX + "+$", str);
    }

    // ==================== 用户名验证 ====================

    /**
     * 验证用户名格式
     */
    public static boolean isUsername(String username) {
        if (isEmpty(username)) {
            return false;
        }
        return Pattern.matches(USERNAME_REGEX, username);
    }

    /**
     * 验证用户名格式（支持中文）
     */
    public static boolean isUsernameWithChinese(String username) {
        if (isEmpty(username)) {
            return false;
        }
        String usernameWithChineseRegex = "^[a-zA-Z0-9_\\u4e00-\\u9fa5]{3,20}$";
        return Pattern.matches(usernameWithChineseRegex, username);
    }

    // ==================== 密码验证 ====================

    /**
     * 验证密码格式
     */
    public static boolean isPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    /**
     * 验证强密码格式
     */
    public static boolean isStrongPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        return Pattern.matches(STRONG_PASSWORD_REGEX, password);
    }

    /**
     * 验证密码强度
     */
    public static PasswordStrength getPasswordStrength(String password) {
        if (isEmpty(password)) {
            return PasswordStrength.WEAK;
        }
        
        int score = 0;
        
        // 长度检查
        if (password.length() >= 8) {
            score += 1;
        }
        if (password.length() >= 12) {
            score += 1;
        }
        
        // 包含小写字母
        if (Pattern.matches(".*[a-z].*", password)) {
            score += 1;
        }
        
        // 包含大写字母
        if (Pattern.matches(".*[A-Z].*", password)) {
            score += 1;
        }
        
        // 包含数字
        if (Pattern.matches(".*\\d.*", password)) {
            score += 1;
        }
        
        // 包含特殊字符
        if (Pattern.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", password)) {
            score += 1;
        }
        
        if (score <= 2) {
            return PasswordStrength.WEAK;
        } else if (score <= 4) {
            return PasswordStrength.MEDIUM;
        } else {
            return PasswordStrength.STRONG;
        }
    }

    // ==================== 日期验证 ====================

    /**
     * 验证日期格式（yyyy-MM-dd）
     */
    public static boolean isDate(String date) {
        if (isEmpty(date)) {
            return false;
        }
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        return Pattern.matches(dateRegex, date);
    }

    /**
     * 验证日期时间格式（yyyy-MM-dd HH:mm:ss）
     */
    public static boolean isDateTime(String dateTime) {
        if (isEmpty(dateTime)) {
            return false;
        }
        String dateTimeRegex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
        return Pattern.matches(dateTimeRegex, dateTime);
    }

    /**
     * 验证时间格式（HH:mm:ss）
     */
    public static boolean isTime(String time) {
        if (isEmpty(time)) {
            return false;
        }
        String timeRegex = "^\\d{2}:\\d{2}:\\d{2}$";
        return Pattern.matches(timeRegex, time);
    }

    // ==================== 文件验证 ====================

    /**
     * 验证文件名格式
     */
    public static boolean isFileName(String fileName) {
        if (isEmpty(fileName)) {
            return false;
        }
        // 文件名不能包含特殊字符
        String fileNameRegex = "^[^<>:\"/\\\\|?*]+$";
        return Pattern.matches(fileNameRegex, fileName);
    }

    /**
     * 验证文件扩展名
     */
    public static boolean isFileExtension(String fileName, String extension) {
        if (isEmpty(fileName) || isEmpty(extension)) {
            return false;
        }
        return fileName.toLowerCase().endsWith("." + extension.toLowerCase());
    }

    /**
     * 验证图片文件扩展名
     */
    public static boolean isImageFile(String fileName) {
        if (isEmpty(fileName)) {
            return false;
        }
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
        for (String ext : imageExtensions) {
            if (isFileExtension(fileName, ext)) {
                return true;
            }
        }
        return false;
    }

    // ==================== 范围验证 ====================

    /**
     * 验证数字是否在范围内
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * 验证数字是否在范围内
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * 验证字符串长度是否在范围内
     */
    public static boolean isLengthInRange(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }

    // ==================== 自定义正则验证 ====================

    /**
     * 验证字符串是否匹配正则表达式
     */
    public static boolean matches(String str, String regex) {
        if (isEmpty(str) || isEmpty(regex)) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 验证字符串是否包含匹配正则表达式的部分
     */
    public static boolean contains(String str, String regex) {
        if (isEmpty(str) || isEmpty(regex)) {
            return false;
        }
        return Pattern.compile(regex).matcher(str).find();
    }

    // ==================== 枚举类 ====================

    /**
     * 密码强度枚举
     */
    public enum PasswordStrength {
        WEAK("弱"),
        MEDIUM("中等"),
        STRONG("强");

        private final String description;

        PasswordStrength(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 验证多个条件是否都为真
     */
    public static boolean allTrue(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return false;
        }
        for (boolean condition : conditions) {
            if (!condition) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证多个条件是否至少有一个为真
     */
    public static boolean anyTrue(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return false;
        }
        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证多个条件是否都为假
     */
    public static boolean allFalse(boolean... conditions) {
        if (conditions == null || conditions.length == 0) {
            return true;
        }
        for (boolean condition : conditions) {
            if (condition) {
                return false;
            }
        }
        return true;
    }
}
