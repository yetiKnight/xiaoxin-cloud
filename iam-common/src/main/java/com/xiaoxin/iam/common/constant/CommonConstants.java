package com.xiaoxin.iam.common.constant;

/**
 * 通用常量定义
 * 包含系统中常用的常量值
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public final class CommonConstants {

    private CommonConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 基础编码常量 ====================
    public static final String SYSTEM_ENCODING = "UTF-8";
    public static final String DEFAULT_LANGUAGE = "zh-CN";

    // ==================== 日期时间相关常量 ====================
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_MS_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_COMPACT_FORMAT = "yyyyMMdd";
    public static final String TIME_COMPACT_FORMAT = "HHmmss";
    public static final String DATETIME_COMPACT_FORMAT = "yyyyMMddHHmmss";
    public static final String DATETIME_MS_COMPACT_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO_DATETIME_MS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    // ==================== 常用正则表达式常量（已迁移到 RegexConstants） ====================

    // ==================== HTTP相关常量 ====================
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_MOVED_PERMANENTLY = 301;
    public static final int HTTP_FOUND = 302;
    public static final int HTTP_NOT_MODIFIED = 304;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_METHOD_NOT_ALLOWED = 405;
    public static final int HTTP_REQUEST_TIMEOUT = 408;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_REQUEST_ENTITY_TOO_LARGE = 413;
    public static final int HTTP_REQUEST_URI_TOO_LONG = 414;
    public static final int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final int HTTP_NOT_IMPLEMENTED = 501;
    public static final int HTTP_BAD_GATEWAY = 502;
    public static final int HTTP_SERVICE_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    public static final String CONTENT_TYPE_STREAM = "application/octet-stream";

    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_COOKIE = "Cookie";
    public static final String HEADER_REFERER = "Referer";
    public static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String HEADER_X_REAL_IP = "X-Real-IP";
    public static final String HEADER_X_REQUESTED_WITH = "X-Requested-With";

    // ==================== 数据库相关常量 ====================
    public static final String DB_TYPE_MYSQL = "mysql";
    public static final String DB_TYPE_POSTGRESQL = "postgresql";
    public static final String DB_TYPE_ORACLE = "oracle";
    public static final String DB_TYPE_SQLSERVER = "sqlserver";
    public static final String DB_TYPE_H2 = "h2";
    public static final String DB_TYPE_SQLITE = "sqlite";

    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";

    public static final int LOGIC_DELETE_NO = 0;
    public static final int LOGIC_DELETE_YES = 1;
    public static final int STATUS_ENABLE = 1;
    public static final int STATUS_DISABLE = 0;
    public static final int STATUS_DELETE = -1;

    // ==================== 缓存相关常量（已迁移到 CacheConstants） ====================

    // ==================== 业务相关常量 ====================
    public static final String USER_TYPE_ADMIN = "admin";
    public static final String USER_TYPE_USER = "user";
    public static final String USER_TYPE_GUEST = "guest";

    public static final String USER_STATUS_NORMAL = "normal";
    public static final String USER_STATUS_LOCKED = "locked";
    public static final String USER_STATUS_DISABLED = "disabled";
    public static final String USER_STATUS_PENDING = "pending";

    public static final String ROLE_TYPE_SYSTEM = "system";
    public static final String ROLE_TYPE_BUSINESS = "business";
    public static final String ROLE_TYPE_CUSTOM = "custom";

    public static final String PERMISSION_TYPE_MENU = "menu";
    public static final String PERMISSION_TYPE_BUTTON = "button";
    public static final String PERMISSION_TYPE_API = "api";
    public static final String PERMISSION_TYPE_DATA = "data";

    public static final String MENU_TYPE_DIRECTORY = "directory";
    public static final String MENU_TYPE_MENU = "menu";
    public static final String MENU_TYPE_BUTTON = "button";
    public static final String MENU_TYPE_EXTERNAL = "external";

    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";
    public static final String GENDER_UNKNOWN = "unknown";

    public static final String YES = "Y";
    public static final String NO = "N";
    public static final int YES_INT = 1;
    public static final int NO_INT = 0;

    // ==================== 文件相关常量 ====================
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
    public static final String[] ALLOWED_DOCUMENT_TYPES = {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt"};
    public static final String[] ALLOWED_ARCHIVE_TYPES = {"zip", "rar", "7z", "tar", "gz"};

    public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
    public static final String UPLOAD_DIR = "uploads";
    public static final String IMAGE_DIR = "images";
    public static final String DOCUMENT_DIR = "documents";
    public static final String TEMP_FILE_DIR = "temp";

    // ==================== 加密相关常量 ====================
    public static final String DEFAULT_CIPHER_ALGORITHM = "AES";
    public static final String DEFAULT_HASH_ALGORITHM = "SHA-256";
    public static final int DEFAULT_KEY_LENGTH = 128;
    public static final int DEFAULT_SALT_LENGTH = 16;
    public static final int DEFAULT_PASSWORD_LENGTH = 8;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 20;

    // ==================== 日志相关常量（已迁移到 SystemConstants） ====================

    // ==================== 其他常量 ====================
    public static final String EMPTY_STRING = "";
    public static final String SPACE_STRING = " ";
    public static final String TAB_STRING = "\t";
    public static final String NEWLINE_STRING = "\n";
    public static final String CARRIAGE_RETURN_STRING = "\r";
    public static final String DOT_STRING = ".";
    public static final String COMMA_STRING = ",";
    public static final String SEMICOLON_STRING = ";";
    public static final String COLON_STRING = ":";
    public static final String UNDERSCORE_STRING = "_";
    public static final String HYPHEN_STRING = "-";
    public static final String SLASH_STRING = "/";
    public static final String BACKSLASH_STRING = "\\";
    public static final String PIPE_STRING = "|";
    public static final String ASTERISK_STRING = "*";
    public static final String QUESTION_MARK_STRING = "?";
    public static final String EQUALS_STRING = "=";
    public static final String PLUS_STRING = "+";
    public static final String MINUS_STRING = "-";
    public static final String PERCENT_STRING = "%";
    public static final String HASH_STRING = "#";
    public static final String DOLLAR_STRING = "$";
    public static final String AT_STRING = "@";
    public static final String EXCLAMATION_STRING = "!";

    public static final String DEFAULT_DELIMITER = ",";
    public static final String DEFAULT_PATH_SEPARATOR = "/";
    
    // ==================== 系统属性相关常量 ====================
    public static final String DEFAULT_FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String DEFAULT_ENCODING = System.getProperty("file.encoding");
    public static final String DEFAULT_TIME_ZONE = System.getProperty("user.timezone");
    public static final String DEFAULT_LOCALE = System.getProperty("user.language");
    public static final String DEFAULT_COUNTRY = System.getProperty("user.country");
    public static final String DEFAULT_OS_NAME = System.getProperty("os.name");
    public static final String DEFAULT_OS_VERSION = System.getProperty("os.version");
    public static final String DEFAULT_OS_ARCH = System.getProperty("os.arch");
    public static final String DEFAULT_JAVA_VERSION = System.getProperty("java.version");
    public static final String DEFAULT_JAVA_VENDOR = System.getProperty("java.vendor");
    public static final String DEFAULT_JAVA_HOME = System.getProperty("java.home");
    public static final String DEFAULT_USER_HOME = System.getProperty("user.home");
    public static final String DEFAULT_USER_DIR = System.getProperty("user.dir");
    public static final String DEFAULT_TEMP_DIR = System.getProperty("java.io.tmpdir");
}