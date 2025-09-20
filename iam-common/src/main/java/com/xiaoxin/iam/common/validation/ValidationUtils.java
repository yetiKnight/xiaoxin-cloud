package com.xiaoxin.iam.common.validation;

import com.xiaoxin.iam.common.constant.RegexConstants;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 邮箱验证 ====================

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.EMAIL, email);
    }

    /**
     * 验证邮箱格式（严格模式）
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmailStrict(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.EMAIL_STRICT, email);
    }

    // ==================== 手机号验证 ====================

    /**
     * 验证中国大陆手机号
     *
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.PHONE_CN, phone);
    }

    /**
     * 验证国际手机号
     *
     * @param phone 手机号
     * @return 是否有效
     */
    public static boolean isValidInternationalPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.PHONE_INTERNATIONAL, phone);
    }

    // ==================== 身份证验证 ====================

    /**
     * 验证18位身份证号
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCard(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.ID_CARD_18, idCard);
    }

    /**
     * 验证15位身份证号
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCard15(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.ID_CARD_15, idCard);
    }

    /**
     * 验证身份证号（兼容15位和18位）
     *
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCardAny(String idCard) {
        return isValidIdCard(idCard) || isValidIdCard15(idCard);
    }

    // ==================== 用户名验证 ====================

    /**
     * 验证用户名格式
     *
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.USERNAME, username);
    }

    /**
     * 验证用户名格式（支持中文）
     *
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsernameCn(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.USERNAME_CN, username);
    }

    // ==================== 密码验证 ====================

    /**
     * 验证密码格式
     *
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.PASSWORD, password);
    }

    /**
     * 验证强密码格式
     *
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidStrongPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.PASSWORD_STRONG, password);
    }

    // ==================== URL验证 ====================

    /**
     * 验证URL格式
     *
     * @param url URL地址
     * @return 是否有效
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.URL, url);
    }

    /**
     * 验证HTTP URL格式
     *
     * @param url URL地址
     * @return 是否有效
     */
    public static boolean isValidHttpUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.HTTP_URL, url);
    }

    // ==================== IP地址验证 ====================

    /**
     * 验证IPv4地址
     *
     * @param ip IP地址
     * @return 是否有效
     */
    public static boolean isValidIpv4(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.IPV4, ip);
    }

    /**
     * 验证IPv6地址
     *
     * @param ip IP地址
     * @return 是否有效
     */
    public static boolean isValidIpv6(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.IPV6, ip);
    }

    /**
     * 验证IP地址（兼容IPv4和IPv6）
     *
     * @param ip IP地址
     * @return 是否有效
     */
    public static boolean isValidIp(String ip) {
        return isValidIpv4(ip) || isValidIpv6(ip);
    }

    // ==================== 数字验证 ====================

    /**
     * 验证正整数
     *
     * @param number 数字字符串
     * @return 是否有效
     */
    public static boolean isValidPositiveInteger(String number) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.POSITIVE_INTEGER, number);
    }

    /**
     * 验证整数
     *
     * @param number 数字字符串
     * @return 是否有效
     */
    public static boolean isValidInteger(String number) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.INTEGER, number);
    }

    /**
     * 验证小数
     *
     * @param number 数字字符串
     * @return 是否有效
     */
    public static boolean isValidDecimal(String number) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.DECIMAL, number);
    }

    // ==================== 字符验证 ====================

    /**
     * 验证中文字符
     *
     * @param text 文本
     * @return 是否有效
     */
    public static boolean isValidChinese(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.CHINESE, text);
    }

    /**
     * 验证英文字母
     *
     * @param text 文本
     * @return 是否有效
     */
    public static boolean isValidLetter(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.LETTER, text);
    }

    /**
     * 验证字母数字
     *
     * @param text 文本
     * @return 是否有效
     */
    public static boolean isValidAlphanumeric(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.ALPHANUMERIC, text);
    }

    // ==================== 日期验证 ====================

    /**
     * 验证日期格式 yyyy-MM-dd
     *
     * @param date 日期字符串
     * @return 是否有效
     */
    public static boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.DATE_YYYY_MM_DD, date);
    }

    /**
     * 验证时间格式 HH:mm:ss
     *
     * @param time 时间字符串
     * @return 是否有效
     */
    public static boolean isValidTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.TIME_HH_MM_SS, time);
    }

    /**
     * 验证日期时间格式 yyyy-MM-dd HH:mm:ss
     *
     * @param datetime 日期时间字符串
     * @return 是否有效
     */
    public static boolean isValidDateTime(String datetime) {
        if (datetime == null || datetime.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.DATETIME_YYYY_MM_DD_HH_MM_SS, datetime);
    }

    // ==================== 文件验证 ====================

    /**
     * 验证文件名
     *
     * @param fileName 文件名
     * @return 是否有效
     */
    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.FILE_NAME, fileName);
    }

    /**
     * 验证图片文件扩展名
     *
     * @param extension 文件扩展名
     * @return 是否有效
     */
    public static boolean isValidImageExtension(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.IMAGE_EXTENSION, extension.toLowerCase());
    }

    /**
     * 验证文档文件扩展名
     *
     * @param extension 文件扩展名
     * @return 是否有效
     */
    public static boolean isValidDocumentExtension(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.DOCUMENT_EXTENSION, extension.toLowerCase());
    }

    // ==================== 特殊格式验证 ====================

    /**
     * 验证UUID格式
     *
     * @param uuid UUID字符串
     * @return 是否有效
     */
    public static boolean isValidUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.UUID, uuid);
    }

    /**
     * 验证MAC地址
     *
     * @param macAddress MAC地址
     * @return 是否有效
     */
    public static boolean isValidMacAddress(String macAddress) {
        if (macAddress == null || macAddress.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.MAC_ADDRESS, macAddress);
    }

    /**
     * 验证十六进制颜色
     *
     * @param color 颜色值
     * @return 是否有效
     */
    public static boolean isValidHexColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.HEX_COLOR, color);
    }

    // ==================== 业务格式验证 ====================

    /**
     * 验证订单号格式
     *
     * @param orderNo 订单号
     * @return 是否有效
     */
    public static boolean isValidOrderNo(String orderNo) {
        if (orderNo == null || orderNo.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.ORDER_NO, orderNo);
    }

    /**
     * 验证序列号格式
     *
     * @param serialNo 序列号
     * @return 是否有效
     */
    public static boolean isValidSerialNo(String serialNo) {
        if (serialNo == null || serialNo.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.SERIAL_NO, serialNo);
    }

    /**
     * 验证版本号格式
     *
     * @param version 版本号
     * @return 是否有效
     */
    public static boolean isValidVersion(String version) {
        if (version == null || version.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.VERSION, version) || 
               Pattern.matches(RegexConstants.VERSION_FULL, version);
    }

    // ==================== 网络相关验证 ====================

    /**
     * 验证域名
     *
     * @param domain 域名
     * @return 是否有效
     */
    public static boolean isValidDomain(String domain) {
        if (domain == null || domain.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.DOMAIN, domain);
    }

    /**
     * 验证端口号
     *
     * @param port 端口号字符串
     * @return 是否有效
     */
    public static boolean isValidPort(String port) {
        if (port == null || port.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.PORT, port);
    }

    /**
     * 验证主机名
     *
     * @param hostname 主机名
     * @return 是否有效
     */
    public static boolean isValidHostname(String hostname) {
        if (hostname == null || hostname.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(RegexConstants.HOSTNAME, hostname);
    }

    // ==================== 自定义正则验证 ====================

    /**
     * 验证自定义正则表达式
     *
     * @param text    待验证文本
     * @param pattern 正则表达式
     * @return 是否匹配
     */
    public static boolean matches(String text, String pattern) {
        if (text == null || pattern == null) {
            return false;
        }
        return Pattern.matches(pattern, text);
    }

    /**
     * 验证自定义正则表达式（编译缓存）
     *
     * @param text    待验证文本
     * @param pattern 编译后的正则表达式
     * @return 是否匹配
     */
    public static boolean matches(String text, Pattern pattern) {
        if (text == null || pattern == null) {
            return false;
        }
        return pattern.matcher(text).matches();
    }

    // ==================== 长度验证 ====================

    /**
     * 验证字符串长度范围
     *
     * @param text 文本
     * @param min  最小长度
     * @param max  最大长度
     * @return 是否在范围内
     */
    public static boolean isLengthValid(String text, int min, int max) {
        if (text == null) {
            return false;
        }
        int length = text.length();
        return length >= min && length <= max;
    }

    /**
     * 验证字符串最小长度
     *
     * @param text 文本
     * @param min  最小长度
     * @return 是否满足最小长度
     */
    public static boolean isMinLengthValid(String text, int min) {
        if (text == null) {
            return false;
        }
        return text.length() >= min;
    }

    /**
     * 验证字符串最大长度
     *
     * @param text 文本
     * @param max  最大长度
     * @return 是否满足最大长度
     */
    public static boolean isMaxLengthValid(String text, int max) {
        if (text == null) {
            return false;
        }
        return text.length() <= max;
    }

    // ==================== 范围验证 ====================

    /**
     * 验证数字范围
     *
     * @param value 数值
     * @param min   最小值
     * @param max   最大值
     * @return 是否在范围内
     */
    public static boolean isRangeValid(Number value, Number min, Number max) {
        if (value == null) {
            return false;
        }
        double val = value.doubleValue();
        double minVal = min != null ? min.doubleValue() : Double.MIN_VALUE;
        double maxVal = max != null ? max.doubleValue() : Double.MAX_VALUE;
        return val >= minVal && val <= maxVal;
    }

    // ==================== 枚举验证 ====================

    /**
     * 验证枚举值
     *
     * @param value     待验证值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 是否有效
     */
    public static <T extends Enum<T>> boolean isValidEnum(String value, Class<T> enumClass) {
        if (value == null || enumClass == null) {
            return false;
        }
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 验证枚举值（忽略大小写）
     *
     * @param value     待验证值
     * @param enumClass 枚举类
     * @param <T>       枚举类型
     * @return 是否有效
     */
    public static <T extends Enum<T>> boolean isValidEnumIgnoreCase(String value, Class<T> enumClass) {
        if (value == null || enumClass == null) {
            return false;
        }
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
