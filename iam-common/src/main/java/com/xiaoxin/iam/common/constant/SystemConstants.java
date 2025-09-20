package com.xiaoxin.iam.common.constant;

/**
 * 系统相关常量定义
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public final class SystemConstants {

    private SystemConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 系统信息 ====================
    public static final String SYSTEM_NAME = "xiaoxin-iam";
    public static final String SYSTEM_VERSION = "1.0.0";
    public static final String SYSTEM_DESCRIPTION = "小新身份认证与访问管理系统";
    public static final String SYSTEM_AUTHOR = "xiaoxin";
    public static final String SYSTEM_EMAIL = "xiaoxin@example.com";
    public static final String SYSTEM_WEBSITE = "https://www.xiaoxin.com";

    // ==================== 环境相关 ====================
    public static final String ENV_DEV = "dev";
    public static final String ENV_TEST = "test";
    public static final String ENV_STAGING = "staging";
    public static final String ENV_PROD = "prod";

    // ==================== 配置文件 ====================
    public static final String CONFIG_FILE_APPLICATION = "application.yml";
    public static final String CONFIG_FILE_APPLICATION_PROPERTIES = "application.properties";
    public static final String CONFIG_FILE_BOOTSTRAP = "bootstrap.yml";
    public static final String CONFIG_FILE_BOOTSTRAP_PROPERTIES = "bootstrap.properties";

    // ==================== 日志相关 ====================
    public static final String LOG_LEVEL_DEBUG = "DEBUG";
    public static final String LOG_LEVEL_INFO = "INFO";
    public static final String LOG_LEVEL_WARN = "WARN";
    public static final String LOG_LEVEL_ERROR = "ERROR";
    public static final String LOG_LEVEL_FATAL = "FATAL";
    public static final String LOG_LEVEL_TRACE = "TRACE";

    public static final String LOG_PATTERN_CONSOLE = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";
    public static final String LOG_PATTERN_FILE = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

    // ==================== 线程池相关 ====================
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    public static final int QUEUE_CAPACITY = 1000;
    public static final long KEEP_ALIVE_TIME = 60L;

    // ==================== 分页相关 ====================
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 1000;
    public static final int DEFAULT_PAGE_NUM = 1;
    public static final String DEFAULT_SORT_FIELD = "id";
    public static final String DEFAULT_SORT_ORDER = "desc";

    // ==================== 文件相关 ====================
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_TIMEZONE = "Asia/Shanghai";
    public static final String DEFAULT_LOCALE = "zh_CN";

    // ==================== 数据库相关 ====================
    public static final String DB_DEFAULT_SCHEMA = "public";
    public static final String DB_DEFAULT_CATALOG = "public";
    public static final int DB_DEFAULT_TIMEOUT = 30;
    public static final int DB_MAX_RETRY = 3;

    // ==================== 缓存相关 ====================
    public static final String CACHE_DEFAULT_NAMESPACE = "xiaoxin:iam";
    public static final long CACHE_DEFAULT_TTL = 3600L;
    public static final int CACHE_DEFAULT_MAX_SIZE = 10000;

    // ==================== 安全相关 ====================
    public static final String SECURITY_DEFAULT_ALGORITHM = "AES";
    public static final int SECURITY_DEFAULT_KEY_LENGTH = 128;
    public static final int SECURITY_DEFAULT_SALT_LENGTH = 16;
    public static final int SECURITY_DEFAULT_ITERATIONS = 10000;

    // ==================== 限流相关 ====================
    public static final int RATE_LIMIT_DEFAULT_CAPACITY = 100;
    public static final int RATE_LIMIT_DEFAULT_REFILL_RATE = 10;
    public static final int RATE_LIMIT_DEFAULT_WINDOW_SIZE = 60;

    // ==================== 监控相关 ====================
    public static final String METRICS_PREFIX = "xiaoxin.iam";
    public static final String HEALTH_CHECK_PATH = "/actuator/health";
    public static final String METRICS_PATH = "/actuator/metrics";
    public static final String INFO_PATH = "/actuator/info";

    // ==================== API相关 ====================
    public static final String API_PREFIX = "/api/v1";
    public static final String API_DOCS_PATH = "/api-docs";
    public static final String SWAGGER_UI_PATH = "/swagger-ui.html";
    public static final String OPENAPI_PATH = "/v3/api-docs";

    // ==================== 消息相关 ====================
    public static final String MESSAGE_TOPIC_USER = "user.topic";
    public static final String MESSAGE_TOPIC_ROLE = "role.topic";
    public static final String MESSAGE_TOPIC_PERMISSION = "permission.topic";
    public static final String MESSAGE_TOPIC_AUDIT = "audit.topic";

    // ==================== 任务相关 ====================
    public static final String TASK_GROUP_DEFAULT = "default";
    public static final String TASK_GROUP_SYSTEM = "system";
    public static final String TASK_GROUP_BUSINESS = "business";
    public static final int TASK_DEFAULT_TIMEOUT = 300;

    // ==================== 事件相关 ====================
    public static final String EVENT_USER_LOGIN = "user.login";
    public static final String EVENT_USER_LOGOUT = "user.logout";
    public static final String EVENT_USER_REGISTER = "user.register";
    public static final String EVENT_USER_UPDATE = "user.update";
    public static final String EVENT_USER_DELETE = "user.delete";

    // ==================== 状态相关 ====================
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_EXPIRED = "expired";
    public static final String STATUS_CANCELLED = "cancelled";

    // ==================== 类型相关 ====================
    public static final String TYPE_SYSTEM = "system";
    public static final String TYPE_USER = "user";
    public static final String TYPE_ADMIN = "admin";
    public static final String TYPE_GUEST = "guest";
    public static final String TYPE_API = "api";
    public static final String TYPE_WEB = "web";
    public static final String TYPE_MOBILE = "mobile";

    // ==================== 级别相关 ====================
    public static final String LEVEL_DEBUG = "debug";
    public static final String LEVEL_INFO = "info";
    public static final String LEVEL_WARN = "warn";
    public static final String LEVEL_ERROR = "error";
    public static final String LEVEL_FATAL = "fatal";

    // ==================== 操作相关 ====================
    public static final String OPERATION_CREATE = "create";
    public static final String OPERATION_READ = "read";
    public static final String OPERATION_UPDATE = "update";
    public static final String OPERATION_DELETE = "delete";
    public static final String OPERATION_EXPORT = "export";
    public static final String OPERATION_IMPORT = "import";
    public static final String OPERATION_LOGIN = "login";
    public static final String OPERATION_LOGOUT = "logout";

    // ==================== 资源相关 ====================
    public static final String RESOURCE_MENU = "menu";
    public static final String RESOURCE_BUTTON = "button";
    public static final String RESOURCE_API = "api";
    public static final String RESOURCE_DATA = "data";
    public static final String RESOURCE_FILE = "file";
    public static final String RESOURCE_IMAGE = "image";
    public static final String RESOURCE_DOCUMENT = "document";

    // ==================== 模块相关 ====================
    public static final String MODULE_USER = "user";
    public static final String MODULE_ROLE = "role";
    public static final String MODULE_PERMISSION = "permission";
    public static final String MODULE_MENU = "menu";
    public static final String MODULE_DICT = "dict";
    public static final String MODULE_CONFIG = "config";
    public static final String MODULE_LOG = "log";
    public static final String MODULE_MONITOR = "monitor";
    public static final String MODULE_SYSTEM = "system";
}
