package com.xiaoxin.iam.common.validation;

/**
 * 验证消息常量
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public final class ValidationMessage {

    private ValidationMessage() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 通用验证消息 ====================
    public static final String NOT_NULL = "不能为空";
    public static final String NOT_BLANK = "不能为空";
    public static final String NOT_EMPTY = "不能为空";
    public static final String INVALID_FORMAT = "格式不正确";
    public static final String INVALID_VALUE = "值不正确";
    public static final String OUT_OF_RANGE = "超出范围";

    // ==================== 邮箱验证消息 ====================
    public static final String EMAIL_INVALID = "邮箱格式不正确";
    public static final String EMAIL_REQUIRED = "邮箱不能为空";

    // ==================== 手机号验证消息 ====================
    public static final String PHONE_INVALID = "手机号格式不正确";
    public static final String PHONE_REQUIRED = "手机号不能为空";

    // ==================== 身份证验证消息 ====================
    public static final String ID_CARD_INVALID = "身份证号格式不正确";
    public static final String ID_CARD_REQUIRED = "身份证号不能为空";

    // ==================== 用户名验证消息 ====================
    public static final String USERNAME_INVALID = "用户名格式不正确";
    public static final String USERNAME_REQUIRED = "用户名不能为空";
    public static final String USERNAME_LENGTH = "用户名长度必须在{min}到{max}个字符之间";

    // ==================== 密码验证消息 ====================
    public static final String PASSWORD_INVALID = "密码格式不正确";
    public static final String PASSWORD_REQUIRED = "密码不能为空";
    public static final String PASSWORD_TOO_WEAK = "密码强度太弱";
    public static final String PASSWORD_LENGTH = "密码长度必须在{min}到{max}个字符之间";

    // ==================== 枚举值验证消息 ====================
    public static final String ENUM_INVALID = "枚举值不正确";
    public static final String ENUM_REQUIRED = "枚举值不能为空";

    // ==================== 字符串长度验证消息 ====================
    public static final String STRING_LENGTH_INVALID = "字符串长度必须在{min}到{max}个字符之间";
    public static final String STRING_TOO_SHORT = "字符串长度不能少于{min}个字符";
    public static final String STRING_TOO_LONG = "字符串长度不能超过{max}个字符";

    // ==================== 数字范围验证消息 ====================
    public static final String NUMBER_RANGE_INVALID = "数字必须在{min}到{max}之间";
    public static final String NUMBER_TOO_SMALL = "数字不能小于{min}";
    public static final String NUMBER_TOO_LARGE = "数字不能大于{max}";

    // ==================== 日期验证消息 ====================
    public static final String DATE_INVALID = "日期格式不正确";
    public static final String DATE_REQUIRED = "日期不能为空";
    public static final String DATE_RANGE_INVALID = "日期必须在{start}到{end}之间";
    public static final String DATE_TOO_EARLY = "日期不能早于{date}";
    public static final String DATE_TOO_LATE = "日期不能晚于{date}";

    // ==================== URL验证消息 ====================
    public static final String URL_INVALID = "URL格式不正确";
    public static final String URL_REQUIRED = "URL不能为空";

    // ==================== IP地址验证消息 ====================
    public static final String IP_INVALID = "IP地址格式不正确";
    public static final String IPV4_INVALID = "IPv4地址格式不正确";
    public static final String IPV6_INVALID = "IPv6地址格式不正确";

    // ==================== 文件验证消息 ====================
    public static final String FILE_INVALID = "文件不正确";
    public static final String FILE_REQUIRED = "文件不能为空";
    public static final String FILE_TYPE_INVALID = "文件类型不正确";
    public static final String FILE_SIZE_EXCEEDED = "文件大小超出限制";

    // ==================== 业务验证消息 ====================
    public static final String CAPTCHA_INVALID = "验证码不正确";
    public static final String CAPTCHA_EXPIRED = "验证码已过期";
    public static final String ORDER_NO_INVALID = "订单号格式不正确";
    public static final String SERIAL_NO_INVALID = "序列号格式不正确";
    public static final String VERSION_INVALID = "版本号格式不正确";

    // ==================== 集合验证消息 ====================
    public static final String COLLECTION_EMPTY = "集合不能为空";
    public static final String COLLECTION_SIZE_INVALID = "集合大小必须在{min}到{max}之间";
    public static final String ARRAY_EMPTY = "数组不能为空";
    public static final String ARRAY_SIZE_INVALID = "数组大小必须在{min}到{max}之间";

    // ==================== 正则表达式验证消息 ====================
    public static final String PATTERN_INVALID = "格式不匹配正则表达式";
    public static final String CHINESE_ONLY = "只能包含中文字符";
    public static final String LETTER_ONLY = "只能包含字母";
    public static final String NUMBER_ONLY = "只能包含数字";
    public static final String ALPHANUMERIC_ONLY = "只能包含字母和数字";

    // ==================== 条件验证消息 ====================
    public static final String CONDITION_NOT_MET = "条件不满足";
    public static final String ASSERTION_FAILED = "断言失败";
    public static final String TRUE_REQUIRED = "必须为true";
    public static final String FALSE_REQUIRED = "必须为false";

    // ==================== 唯一性验证消息 ====================
    public static final String UNIQUE_VIOLATION = "值必须唯一";
    public static final String DUPLICATE_VALUE = "值重复";
    public static final String ALREADY_EXISTS = "数据已存在";

    // ==================== 关联验证消息 ====================
    public static final String REFERENCE_NOT_FOUND = "关联数据不存在";
    public static final String CIRCULAR_REFERENCE = "存在循环引用";
    public static final String DEPENDENCY_VIOLATION = "违反依赖约束";
}
