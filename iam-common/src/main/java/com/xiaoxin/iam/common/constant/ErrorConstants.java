package com.xiaoxin.iam.common.constant;

/**
 * 错误码常量定义
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public final class ErrorConstants {

    private ErrorConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 通用错误码 ====================
    public static final String SUCCESS = "0000";
    public static final String SYSTEM_ERROR = "1000";
    public static final String PARAM_ERROR = "1001";
    public static final String VALIDATION_ERROR = "1002";
    public static final String BUSINESS_ERROR = "1003";
    public static final String UNAUTHORIZED = "1004";
    public static final String FORBIDDEN = "1005";
    public static final String NOT_FOUND = "1006";
    public static final String CONFLICT = "1007";
    public static final String TIMEOUT = "1008";
    public static final String RATE_LIMIT = "1009";

    // ==================== 用户相关错误码 ====================
    public static final String USER_NOT_FOUND = "2001";
    public static final String USER_ALREADY_EXISTS = "2002";
    public static final String USER_DISABLED = "2003";
    public static final String USER_LOCKED = "2004";
    public static final String USER_PASSWORD_ERROR = "2005";
    public static final String USER_EMAIL_EXISTS = "2006";
    public static final String USER_PHONE_EXISTS = "2007";
    public static final String USER_USERNAME_EXISTS = "2008";

    // ==================== 认证相关错误码 ====================
    public static final String TOKEN_INVALID = "3001";
    public static final String TOKEN_EXPIRED = "3002";
    public static final String TOKEN_MISSING = "3003";
    public static final String LOGIN_FAILED = "3004";
    public static final String LOGOUT_FAILED = "3005";
    public static final String CAPTCHA_ERROR = "3006";
    public static final String CAPTCHA_EXPIRED = "3007";

    // ==================== 权限相关错误码 ====================
    public static final String PERMISSION_DENIED = "4001";
    public static final String ROLE_NOT_FOUND = "4002";
    public static final String ROLE_ALREADY_EXISTS = "4003";
    public static final String PERMISSION_NOT_FOUND = "4004";
    public static final String PERMISSION_ALREADY_EXISTS = "4005";

    // ==================== 文件相关错误码 ====================
    public static final String FILE_NOT_FOUND = "5001";
    public static final String FILE_UPLOAD_FAILED = "5002";
    public static final String FILE_DOWNLOAD_FAILED = "5003";
    public static final String FILE_DELETE_FAILED = "5004";
    public static final String FILE_TYPE_NOT_ALLOWED = "5005";
    public static final String FILE_SIZE_EXCEEDED = "5006";

    // ==================== 数据库相关错误码 ====================
    public static final String DATABASE_ERROR = "6001";
    public static final String DATA_NOT_FOUND = "6002";
    public static final String DATA_ALREADY_EXISTS = "6003";
    public static final String DATA_CONSTRAINT_VIOLATION = "6004";
    public static final String DATA_INTEGRITY_VIOLATION = "6005";

    // ==================== 网络相关错误码 ====================
    public static final String NETWORK_ERROR = "7001";
    public static final String CONNECTION_TIMEOUT = "7002";
    public static final String READ_TIMEOUT = "7003";
    public static final String CONNECTION_REFUSED = "7004";
    public static final String DNS_ERROR = "7005";

    // ==================== 第三方服务错误码 ====================
    public static final String THIRD_PARTY_ERROR = "8001";
    public static final String EMAIL_SERVICE_ERROR = "8002";
    public static final String SMS_SERVICE_ERROR = "8003";
    public static final String PAYMENT_SERVICE_ERROR = "8004";
    public static final String STORAGE_SERVICE_ERROR = "8005";
}
