package com.xiaoxin.iam.common.constant;

/**
 * 正则表达式常量定义
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public final class RegexConstants {

    private RegexConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 邮箱相关 ====================
    public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String EMAIL_STRICT = "^[a-zA-Z0-9]([a-zA-Z0-9._-]*[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9.-]*[a-zA-Z0-9])?\\.[a-zA-Z]{2,}$";

    // ==================== 手机号相关 ====================
    public static final String PHONE_CN = "^1[3-9]\\d{9}$";
    public static final String PHONE_INTERNATIONAL = "^\\+?[1-9]\\d{1,14}$";

    // ==================== 身份证相关 ====================
    public static final String ID_CARD_18 = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    public static final String ID_CARD_15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}$";

    // ==================== URL相关 ====================
    public static final String URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    public static final String HTTP_URL = "^https?://[^\\s/$.?#].[^\\s]*$";

    // ==================== IP地址相关 ====================
    public static final String IPV4 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    public static final String IPV6 = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";

    // ==================== 用户名相关 ====================
    public static final String USERNAME = "^[a-zA-Z0-9_]{3,20}$";
    public static final String USERNAME_CN = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{2,20}$";

    // ==================== 密码相关 ====================
    public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$";
    public static final String PASSWORD_STRONG = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    // ==================== 数字相关 ====================
    public static final String NUMBER = "^\\d+$";
    public static final String INTEGER = "^-?\\d+$";
    public static final String DECIMAL = "^-?\\d+(\\.\\d+)?$";
    public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";
    public static final String NEGATIVE_INTEGER = "^-\\d+$";
    public static final String NON_NEGATIVE_INTEGER = "^\\d+$";
    public static final String NON_POSITIVE_INTEGER = "^-?\\d+$";

    // ==================== 字符相关 ====================
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";
    public static final String LETTER = "^[a-zA-Z]+$";
    public static final String ALPHANUMERIC = "^[a-zA-Z0-9]+$";
    public static final String LETTER_CN = "^[\\u4e00-\\u9fa5a-zA-Z]+$";
    public static final String ALPHANUMERIC_CN = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    // ==================== 日期相关 ====================
    public static final String DATE_YYYY_MM_DD = "^\\d{4}-\\d{2}-\\d{2}$";
    public static final String DATE_YYYYMMDD = "^\\d{8}$";
    public static final String TIME_HH_MM_SS = "^\\d{2}:\\d{2}:\\d{2}$";
    public static final String TIME_HHMMSS = "^\\d{6}$";
    public static final String DATETIME_YYYY_MM_DD_HH_MM_SS = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
    public static final String DATETIME_YYYYMMDDHHMMSS = "^\\d{14}$";

    // ==================== 文件相关 ====================
    public static final String FILE_NAME = "^[^<>:\"/\\\\|?*]+$";
    public static final String FILE_EXTENSION = "^\\.([a-zA-Z0-9]+)$";
    public static final String IMAGE_EXTENSION = "^\\.(jpg|jpeg|png|gif|bmp|webp)$";
    public static final String DOCUMENT_EXTENSION = "^\\.(pdf|doc|docx|xls|xlsx|ppt|pptx|txt)$";
    public static final String ARCHIVE_EXTENSION = "^\\.(zip|rar|7z|tar|gz)$";

    // ==================== 特殊格式 ====================
    public static final String UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String MAC_ADDRESS = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
    public static final String HEX_COLOR = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    public static final String POSTAL_CODE_CN = "^\\d{6}$";
    public static final String POSTAL_CODE_US = "^\\d{5}(-\\d{4})?$";

    // ==================== 业务相关 ====================
    public static final String ORDER_NO = "^[A-Z0-9]{10,20}$";
    public static final String SERIAL_NO = "^[A-Z0-9]{8,16}$";
    public static final String VERSION = "^\\d+\\.\\d+(\\.\\d+)?$";
    public static final String VERSION_FULL = "^\\d+\\.\\d+\\.\\d+(-[a-zA-Z0-9]+)?$";

    // ==================== 网络相关 ====================
    public static final String DOMAIN = "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?)*$";
    public static final String PORT = "^([1-9]\\d{0,3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
    public static final String HOSTNAME = "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?$";

    // ==================== 其他常用 ====================
    public static final String BLANK = "^\\s*$";
    public static final String NOT_BLANK = "^\\s*\\S+\\s*$";
    public static final String WHITESPACE = "^\\s+$";
    public static final String NON_WHITESPACE = "^\\S+$";
    public static final String CREDIT_CARD = "^\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}$";
    public static final String CURRENCY = "^\\d+(\\.\\d{1,2})?$";
    public static final String PERCENTAGE = "^\\d+(\\.\\d{1,2})?%$";
}
