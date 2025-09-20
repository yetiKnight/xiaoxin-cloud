# 常量定义使用文档

本文档介绍了 iam-common 模块中提供的各种常量定义的使用方法。

## 目录

- [CommonConstants - 通用常量](#commonconstants---通用常量)
- [ErrorConstants - 错误码常量](#errorconstants---错误码常量)
- [CacheConstants - 缓存常量](#cacheconstants---缓存常量)
- [RegexConstants - 正则表达式常量](#regexconstants---正则表达式常量)
- [SystemConstants - 系统常量](#systemconstants---系统常量)

## CommonConstants - 通用常量

### 系统相关常量

```java
// 系统基本信息
String systemName = CommonConstants.SYSTEM_NAME; // "xiaoxin-iam"
String systemVersion = CommonConstants.SYSTEM_VERSION; // "1.0.0"
String systemEncoding = CommonConstants.SYSTEM_ENCODING; // "UTF-8"
String defaultCharset = CommonConstants.DEFAULT_CHARSET; // "UTF-8"
String defaultLanguage = CommonConstants.DEFAULT_LANGUAGE; // "zh-CN"
String defaultTimezone = CommonConstants.DEFAULT_TIMEZONE; // "Asia/Shanghai"
```

### 日期时间相关常量

```java
// 日期格式
String dateFormat = CommonConstants.DATE_FORMAT; // "yyyy-MM-dd"
String timeFormat = CommonConstants.TIME_FORMAT; // "HH:mm:ss"
String datetimeFormat = CommonConstants.DATETIME_FORMAT; // "yyyy-MM-dd HH:mm:ss"
String datetimeMsFormat = CommonConstants.DATETIME_MS_FORMAT; // "yyyy-MM-dd HH:mm:ss.SSS"

// 紧凑格式
String dateCompact = CommonConstants.DATE_COMPACT_FORMAT; // "yyyyMMdd"
String timeCompact = CommonConstants.TIME_COMPACT_FORMAT; // "HHmmss"
String datetimeCompact = CommonConstants.DATETIME_COMPACT_FORMAT; // "yyyyMMddHHmmss"

// ISO格式
String isoDatetime = CommonConstants.ISO_DATETIME_FORMAT; // "yyyy-MM-dd'T'HH:mm:ss"
String isoDatetimeMs = CommonConstants.ISO_DATETIME_MS_FORMAT; // "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
```

### 正则表达式常量

```java
// 邮箱验证
String emailRegex = CommonConstants.EMAIL_REGEX;
String emailStrictRegex = CommonConstants.EMAIL_STRICT_REGEX;

// 手机号验证
String phoneRegex = CommonConstants.PHONE_REGEX; // 中国大陆手机号
String phoneInternationalRegex = CommonConstants.PHONE_INTERNATIONAL_REGEX; // 国际手机号

// 身份证验证
String idCardRegex = CommonConstants.ID_CARD_REGEX; // 18位身份证
String idCard15Regex = CommonConstants.ID_CARD_15_REGEX; // 15位身份证

// URL验证
String urlRegex = CommonConstants.URL_REGEX;
String httpUrlRegex = CommonConstants.HTTP_URL_REGEX;

// IP地址验证
String ipv4Regex = CommonConstants.IPV4_REGEX;
String ipv6Regex = CommonConstants.IPV6_REGEX;

// 用户名和密码验证
String usernameRegex = CommonConstants.USERNAME_REGEX;
String passwordRegex = CommonConstants.PASSWORD_REGEX;
String strongPasswordRegex = CommonConstants.STRONG_PASSWORD_REGEX;
```

### HTTP相关常量

```java
// HTTP状态码
int ok = CommonConstants.HTTP_OK; // 200
int created = CommonConstants.HTTP_CREATED; // 201
int badRequest = CommonConstants.HTTP_BAD_REQUEST; // 400
int unauthorized = CommonConstants.HTTP_UNAUTHORIZED; // 401
int forbidden = CommonConstants.HTTP_FORBIDDEN; // 403
int notFound = CommonConstants.HTTP_NOT_FOUND; // 404
int internalServerError = CommonConstants.HTTP_INTERNAL_SERVER_ERROR; // 500

// Content-Type
String jsonContentType = CommonConstants.CONTENT_TYPE_JSON; // "application/json"
String xmlContentType = CommonConstants.CONTENT_TYPE_XML; // "application/xml"
String htmlContentType = CommonConstants.CONTENT_TYPE_HTML; // "text/html"
String textContentType = CommonConstants.CONTENT_TYPE_TEXT; // "text/plain"
String formContentType = CommonConstants.CONTENT_TYPE_FORM; // "application/x-www-form-urlencoded"
String multipartContentType = CommonConstants.CONTENT_TYPE_MULTIPART; // "multipart/form-data"

// HTTP请求头
String userAgentHeader = CommonConstants.HEADER_USER_AGENT; // "User-Agent"
String authorizationHeader = CommonConstants.HEADER_AUTHORIZATION; // "Authorization"
String contentTypeHeader = CommonConstants.HEADER_CONTENT_TYPE; // "Content-Type"
String acceptHeader = CommonConstants.HEADER_ACCEPT; // "Accept"
String cookieHeader = CommonConstants.HEADER_COOKIE; // "Cookie"
```

### 数据库相关常量

```java
// 数据库类型
String mysql = CommonConstants.DB_TYPE_MYSQL; // "mysql"
String postgresql = CommonConstants.DB_TYPE_POSTGRESQL; // "postgresql"
String oracle = CommonConstants.DB_TYPE_ORACLE; // "oracle"
String sqlserver = CommonConstants.DB_TYPE_SQLSERVER; // "sqlserver"
String h2 = CommonConstants.DB_TYPE_H2; // "h2"
String sqlite = CommonConstants.DB_TYPE_SQLITE; // "sqlite"

// 分页相关
int defaultPageSize = CommonConstants.DEFAULT_PAGE_SIZE; // 10
int maxPageSize = CommonConstants.MAX_PAGE_SIZE; // 1000
int defaultPageNum = CommonConstants.DEFAULT_PAGE_NUM; // 1
String sortAsc = CommonConstants.SORT_ASC; // "ASC"
String sortDesc = CommonConstants.SORT_DESC; // "DESC"

// 状态标识
int logicDeleteNo = CommonConstants.LOGIC_DELETE_NO; // 0
int logicDeleteYes = CommonConstants.LOGIC_DELETE_YES; // 1
int statusEnable = CommonConstants.STATUS_ENABLE; // 1
int statusDisable = CommonConstants.STATUS_DISABLE; // 0
int statusDelete = CommonConstants.STATUS_DELETE; // -1
```

### 缓存相关常量

```java
// 缓存键前缀
String userPrefix = CommonConstants.CACHE_PREFIX_USER; // "user:"
String rolePrefix = CommonConstants.CACHE_PREFIX_ROLE; // "role:"
String permissionPrefix = CommonConstants.CACHE_PREFIX_PERMISSION; // "permission:"
String menuPrefix = CommonConstants.CACHE_PREFIX_MENU; // "menu:"
String sessionPrefix = CommonConstants.CACHE_PREFIX_SESSION; // "session:"
String tokenPrefix = CommonConstants.CACHE_PREFIX_TOKEN; // "token:"
String captchaPrefix = CommonConstants.CACHE_PREFIX_CAPTCHA; // "captcha:"

// 缓存过期时间（秒）
long defaultCacheExpire = CommonConstants.DEFAULT_CACHE_EXPIRE; // 3600
long userCacheExpire = CommonConstants.USER_CACHE_EXPIRE; // 1800
long sessionCacheExpire = CommonConstants.SESSION_CACHE_EXPIRE; // 7200
long tokenCacheExpire = CommonConstants.TOKEN_CACHE_EXPIRE; // 3600
long captchaCacheExpire = CommonConstants.CAPTCHA_CACHE_EXPIRE; // 300
```

### 业务相关常量

```java
// 用户类型
String userTypeAdmin = CommonConstants.USER_TYPE_ADMIN; // "admin"
String userTypeUser = CommonConstants.USER_TYPE_USER; // "user"
String userTypeGuest = CommonConstants.USER_TYPE_GUEST; // "guest"

// 用户状态
String userStatusNormal = CommonConstants.USER_STATUS_NORMAL; // "normal"
String userStatusLocked = CommonConstants.USER_STATUS_LOCKED; // "locked"
String userStatusDisabled = CommonConstants.USER_STATUS_DISABLED; // "disabled"
String userStatusPending = CommonConstants.USER_STATUS_PENDING; // "pending"

// 角色类型
String roleTypeSystem = CommonConstants.ROLE_TYPE_SYSTEM; // "system"
String roleTypeBusiness = CommonConstants.ROLE_TYPE_BUSINESS; // "business"
String roleTypeCustom = CommonConstants.ROLE_TYPE_CUSTOM; // "custom"

// 权限类型
String permissionTypeMenu = CommonConstants.PERMISSION_TYPE_MENU; // "menu"
String permissionTypeButton = CommonConstants.PERMISSION_TYPE_BUTTON; // "button"
String permissionTypeApi = CommonConstants.PERMISSION_TYPE_API; // "api"
String permissionTypeData = CommonConstants.PERMISSION_TYPE_DATA; // "data"

// 菜单类型
String menuTypeDirectory = CommonConstants.MENU_TYPE_DIRECTORY; // "directory"
String menuTypeMenu = CommonConstants.MENU_TYPE_MENU; // "menu"
String menuTypeButton = CommonConstants.MENU_TYPE_BUTTON; // "button"
String menuTypeExternal = CommonConstants.MENU_TYPE_EXTERNAL; // "external"
```

### 文件相关常量

```java
// 文件大小限制
long maxFileSize = CommonConstants.MAX_FILE_SIZE; // 10MB
long maxImageSize = CommonConstants.MAX_IMAGE_SIZE; // 5MB

// 允许的文件类型
String[] allowedImageTypes = CommonConstants.ALLOWED_IMAGE_TYPES; // {"jpg", "jpeg", "png", "gif", "bmp", "webp"}
String[] allowedDocumentTypes = CommonConstants.ALLOWED_DOCUMENT_TYPES; // {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt"}
String[] allowedArchiveTypes = CommonConstants.ALLOWED_ARCHIVE_TYPES; // {"zip", "rar", "7z", "tar", "gz"}

// 文件目录
String tempDir = CommonConstants.TEMP_DIR; // 系统临时目录
String uploadDir = CommonConstants.UPLOAD_DIR; // "uploads"
String imageDir = CommonConstants.IMAGE_DIR; // "images"
String documentDir = CommonConstants.DOCUMENT_DIR; // "documents"
```

## ErrorConstants - 错误码常量

### 通用错误码

```java
// 成功和通用错误
String success = ErrorConstants.SUCCESS; // "0000"
String systemError = ErrorConstants.SYSTEM_ERROR; // "1000"
String paramError = ErrorConstants.PARAM_ERROR; // "1001"
String validationError = ErrorConstants.VALIDATION_ERROR; // "1002"
String businessError = ErrorConstants.BUSINESS_ERROR; // "1003"
String unauthorized = ErrorConstants.UNAUTHORIZED; // "1004"
String forbidden = ErrorConstants.FORBIDDEN; // "1005"
String notFound = ErrorConstants.NOT_FOUND; // "1006"
String conflict = ErrorConstants.CONFLICT; // "1007"
String timeout = ErrorConstants.TIMEOUT; // "1008"
String rateLimit = ErrorConstants.RATE_LIMIT; // "1009"
```

### 用户相关错误码

```java
// 用户错误
String userNotFound = ErrorConstants.USER_NOT_FOUND; // "2001"
String userAlreadyExists = ErrorConstants.USER_ALREADY_EXISTS; // "2002"
String userDisabled = ErrorConstants.USER_DISABLED; // "2003"
String userLocked = ErrorConstants.USER_LOCKED; // "2004"
String userPasswordError = ErrorConstants.USER_PASSWORD_ERROR; // "2005"
String userEmailExists = ErrorConstants.USER_EMAIL_EXISTS; // "2006"
String userPhoneExists = ErrorConstants.USER_PHONE_EXISTS; // "2007"
String userUsernameExists = ErrorConstants.USER_USERNAME_EXISTS; // "2008"
```

### 认证相关错误码

```java
// 认证错误
String tokenInvalid = ErrorConstants.TOKEN_INVALID; // "3001"
String tokenExpired = ErrorConstants.TOKEN_EXPIRED; // "3002"
String tokenMissing = ErrorConstants.TOKEN_MISSING; // "3003"
String loginFailed = ErrorConstants.LOGIN_FAILED; // "3004"
String logoutFailed = ErrorConstants.LOGOUT_FAILED; // "3005"
String captchaError = ErrorConstants.CAPTCHA_ERROR; // "3006"
String captchaExpired = ErrorConstants.CAPTCHA_EXPIRED; // "3007"
```

### 权限相关错误码

```java
// 权限错误
String permissionDenied = ErrorConstants.PERMISSION_DENIED; // "4001"
String roleNotFound = ErrorConstants.ROLE_NOT_FOUND; // "4002"
String roleAlreadyExists = ErrorConstants.ROLE_ALREADY_EXISTS; // "4003"
String permissionNotFound = ErrorConstants.PERMISSION_NOT_FOUND; // "4004"
String permissionAlreadyExists = ErrorConstants.PERMISSION_ALREADY_EXISTS; // "4005"
```

### 文件相关错误码

```java
// 文件错误
String fileNotFound = ErrorConstants.FILE_NOT_FOUND; // "5001"
String fileUploadFailed = ErrorConstants.FILE_UPLOAD_FAILED; // "5002"
String fileDownloadFailed = ErrorConstants.FILE_DOWNLOAD_FAILED; // "5003"
String fileDeleteFailed = ErrorConstants.FILE_DELETE_FAILED; // "5004"
String fileTypeNotAllowed = ErrorConstants.FILE_TYPE_NOT_ALLOWED; // "5005"
String fileSizeExceeded = ErrorConstants.FILE_SIZE_EXCEEDED; // "5006"
```

## CacheConstants - 缓存常量

### 缓存键前缀

```java
// 缓存前缀
String userPrefix = CacheConstants.PREFIX_USER; // "user:"
String rolePrefix = CacheConstants.PREFIX_ROLE; // "role:"
String permissionPrefix = CacheConstants.PREFIX_PERMISSION; // "permission:"
String menuPrefix = CacheConstants.PREFIX_MENU; // "menu:"
String sessionPrefix = CacheConstants.PREFIX_SESSION; // "session:"
String tokenPrefix = CacheConstants.PREFIX_TOKEN; // "token:"
String captchaPrefix = CacheConstants.PREFIX_CAPTCHA; // "captcha:"
String rateLimitPrefix = CacheConstants.PREFIX_RATE_LIMIT; // "rate_limit:"
```

### 缓存键模板

```java
// 用户相关缓存键
String userInfoKey = String.format(CacheConstants.USER_INFO_KEY, userId); // "user:info:123"
String userRolesKey = String.format(CacheConstants.USER_ROLES_KEY, userId); // "user:roles:123"
String userPermissionsKey = String.format(CacheConstants.USER_PERMISSIONS_KEY, userId); // "user:permissions:123"
String userMenusKey = String.format(CacheConstants.USER_MENUS_KEY, userId); // "user:menus:123"

// 角色相关缓存键
String roleInfoKey = String.format(CacheConstants.ROLE_INFO_KEY, roleId); // "role:info:456"
String rolePermissionsKey = String.format(CacheConstants.ROLE_PERMISSIONS_KEY, roleId); // "role:permissions:456"

// 权限相关缓存键
String permissionInfoKey = String.format(CacheConstants.PERMISSION_INFO_KEY, permissionId); // "permission:info:789"

// 其他缓存键
String menuTreeKey = CacheConstants.MENU_TREE_KEY; // "menu:tree"
String dictDataKey = String.format(CacheConstants.DICT_DATA_KEY, dictType); // "dict:data:user_status"
String configValueKey = String.format(CacheConstants.CONFIG_VALUE_KEY, configKey); // "config:value:system.name"
String sessionKey = String.format(CacheConstants.SESSION_KEY, sessionId); // "session:abc123"
String tokenKey = String.format(CacheConstants.TOKEN_KEY, token); // "token:xyz789"
String captchaKey = String.format(CacheConstants.CAPTCHA_KEY, captchaId); // "captcha:def456"
String rateLimitKey = String.format(CacheConstants.RATE_LIMIT_KEY, userId, api); // "rate_limit:123:/api/users"
```

### 缓存过期时间

```java
// 过期时间（秒）
long defaultExpire = CacheConstants.EXPIRE_DEFAULT; // 3600
long userExpire = CacheConstants.EXPIRE_USER; // 1800
long sessionExpire = CacheConstants.EXPIRE_SESSION; // 7200
long tokenExpire = CacheConstants.EXPIRE_TOKEN; // 3600
long captchaExpire = CacheConstants.EXPIRE_CAPTCHA; // 300
long rateLimitExpire = CacheConstants.EXPIRE_RATE_LIMIT; // 60
long lockExpire = CacheConstants.EXPIRE_LOCK; // 30
long verifyExpire = CacheConstants.EXPIRE_VERIFY; // 600
long permanentExpire = CacheConstants.EXPIRE_PERMANENT; // -1 (永不过期)
```

### 缓存配置

```java
// 缓存管理器
String redisCacheManager = CacheConstants.CACHE_MANAGER_REDIS; // "redisCacheManager"
String caffeineCacheManager = CacheConstants.CACHE_MANAGER_CAFFEINE; // "caffeineCacheManager"
String compositeCacheManager = CacheConstants.CACHE_MANAGER_COMPOSITE; // "compositeCacheManager"

// 缓存名称
String userCache = CacheConstants.CACHE_NAME_USER; // "userCache"
String roleCache = CacheConstants.CACHE_NAME_ROLE; // "roleCache"
String permissionCache = CacheConstants.CACHE_NAME_PERMISSION; // "permissionCache"
String menuCache = CacheConstants.CACHE_NAME_MENU; // "menuCache"
String sessionCache = CacheConstants.CACHE_NAME_SESSION; // "sessionCache"
String tokenCache = CacheConstants.CACHE_NAME_TOKEN; // "tokenCache"
```

## RegexConstants - 正则表达式常量

### 邮箱相关

```java
// 邮箱验证
String email = RegexConstants.EMAIL; // 标准邮箱格式
String emailStrict = RegexConstants.EMAIL_STRICT; // 严格邮箱格式
```

### 手机号相关

```java
// 手机号验证
String phoneCn = RegexConstants.PHONE_CN; // 中国大陆手机号
String phoneInternational = RegexConstants.PHONE_INTERNATIONAL; // 国际手机号
```

### 身份证相关

```java
// 身份证验证
String idCard18 = RegexConstants.ID_CARD_18; // 18位身份证
String idCard15 = RegexConstants.ID_CARD_15; // 15位身份证
```

### URL相关

```java
// URL验证
String url = RegexConstants.URL; // 通用URL
String httpUrl = RegexConstants.HTTP_URL; // HTTP/HTTPS URL
```

### IP地址相关

```java
// IP地址验证
String ipv4 = RegexConstants.IPV4; // IPv4地址
String ipv6 = RegexConstants.IPV6; // IPv6地址
```

### 用户名相关

```java
// 用户名验证
String username = RegexConstants.USERNAME; // 标准用户名（3-20位字母数字下划线）
String usernameCn = RegexConstants.USERNAME_CN; // 支持中文的用户名（2-20位）
```

### 密码相关

```java
// 密码验证
String password = RegexConstants.PASSWORD; // 标准密码（至少8位，包含字母和数字）
String passwordStrong = RegexConstants.PASSWORD_STRONG; // 强密码（至少8位，包含大小写字母、数字和特殊字符）
```

### 数字相关

```java
// 数字验证
String number = RegexConstants.NUMBER; // 正整数
String integer = RegexConstants.INTEGER; // 整数（包含负数）
String decimal = RegexConstants.DECIMAL; // 小数
String positiveInteger = RegexConstants.POSITIVE_INTEGER; // 正整数
String negativeInteger = RegexConstants.NEGATIVE_INTEGER; // 负整数
String nonNegativeInteger = RegexConstants.NON_NEGATIVE_INTEGER; // 非负整数
String nonPositiveInteger = RegexConstants.NON_POSITIVE_INTEGER; // 非正整数
```

### 字符相关

```java
// 字符验证
String chinese = RegexConstants.CHINESE; // 中文字符
String letter = RegexConstants.LETTER; // 英文字母
String alphanumeric = RegexConstants.ALPHANUMERIC; // 字母数字
String letterCn = RegexConstants.LETTER_CN; // 中文和英文字母
String alphanumericCn = RegexConstants.ALPHANUMERIC_CN; // 中文、字母和数字
```

### 日期相关

```java
// 日期格式验证
String dateYyyyMmDd = RegexConstants.DATE_YYYY_MM_DD; // yyyy-MM-dd
String dateYyyymmdd = RegexConstants.DATE_YYYYMMDD; // yyyyMMdd
String timeHhMmSs = RegexConstants.TIME_HH_MM_SS; // HH:mm:ss
String timeHhmmss = RegexConstants.TIME_HHMMSS; // HHmmss
String datetimeYyyyMmDdHhMmSs = RegexConstants.DATETIME_YYYY_MM_DD_HH_MM_SS; // yyyy-MM-dd HH:mm:ss
String datetimeYyyymmddhhmmss = RegexConstants.DATETIME_YYYYMMDDHHMMSS; // yyyyMMddHHmmss
```

### 文件相关

```java
// 文件验证
String fileName = RegexConstants.FILE_NAME; // 文件名（不包含特殊字符）
String fileExtension = RegexConstants.FILE_EXTENSION; // 文件扩展名
String imageExtension = RegexConstants.IMAGE_EXTENSION; // 图片扩展名
String documentExtension = RegexConstants.DOCUMENT_EXTENSION; // 文档扩展名
String archiveExtension = RegexConstants.ARCHIVE_EXTENSION; // 压缩包扩展名
```

### 特殊格式

```java
// 特殊格式验证
String uuid = RegexConstants.UUID; // UUID格式
String macAddress = RegexConstants.MAC_ADDRESS; // MAC地址
String hexColor = RegexConstants.HEX_COLOR; // 十六进制颜色
String postalCodeCn = RegexConstants.POSTAL_CODE_CN; // 中国邮政编码
String postalCodeUs = RegexConstants.POSTAL_CODE_US; // 美国邮政编码
```

### 业务相关

```java
// 业务格式验证
String orderNo = RegexConstants.ORDER_NO; // 订单号
String serialNo = RegexConstants.SERIAL_NO; // 序列号
String version = RegexConstants.VERSION; // 版本号
String versionFull = RegexConstants.VERSION_FULL; // 完整版本号
```

### 网络相关

```java
// 网络格式验证
String domain = RegexConstants.DOMAIN; // 域名
String port = RegexConstants.PORT; // 端口号
String hostname = RegexConstants.HOSTNAME; // 主机名
```

### 其他常用

```java
// 其他常用验证
String blank = RegexConstants.BLANK; // 空白字符串
String notBlank = RegexConstants.NOT_BLANK; // 非空白字符串
String whitespace = RegexConstants.WHITESPACE; // 空白字符
String nonWhitespace = RegexConstants.NON_WHITESPACE; // 非空白字符
String creditCard = RegexConstants.CREDIT_CARD; // 信用卡号
String currency = RegexConstants.CURRENCY; // 货币格式
String percentage = RegexConstants.PERCENTAGE; // 百分比格式
```

## SystemConstants - 系统常量

### 系统信息

```java
// 系统基本信息
String systemName = SystemConstants.SYSTEM_NAME; // "xiaoxin-iam"
String systemVersion = SystemConstants.SYSTEM_VERSION; // "1.0.0"
String systemDescription = SystemConstants.SYSTEM_DESCRIPTION; // "小新身份认证与访问管理系统"
String systemAuthor = SystemConstants.SYSTEM_AUTHOR; // "xiaoxin"
String systemEmail = SystemConstants.SYSTEM_EMAIL; // "xiaoxin@example.com"
String systemWebsite = SystemConstants.SYSTEM_WEBSITE; // "https://www.xiaoxin.com"
```

### 环境相关

```java
// 环境标识
String envDev = SystemConstants.ENV_DEV; // "dev"
String envTest = SystemConstants.ENV_TEST; // "test"
String envStaging = SystemConstants.ENV_STAGING; // "staging"
String envProd = SystemConstants.ENV_PROD; // "prod"
```

### 配置文件

```java
// 配置文件名称
String applicationYml = SystemConstants.CONFIG_FILE_APPLICATION; // "application.yml"
String applicationProperties = SystemConstants.CONFIG_FILE_APPLICATION_PROPERTIES; // "application.properties"
String bootstrapYml = SystemConstants.CONFIG_FILE_BOOTSTRAP; // "bootstrap.yml"
String bootstrapProperties = SystemConstants.CONFIG_FILE_BOOTSTRAP_PROPERTIES; // "bootstrap.properties"
```

### 日志相关

```java
// 日志级别
String logDebug = SystemConstants.LOG_LEVEL_DEBUG; // "DEBUG"
String logInfo = SystemConstants.LOG_LEVEL_INFO; // "INFO"
String logWarn = SystemConstants.LOG_LEVEL_WARN; // "WARN"
String logError = SystemConstants.LOG_LEVEL_ERROR; // "ERROR"
String logFatal = SystemConstants.LOG_LEVEL_FATAL; // "FATAL"
String logTrace = SystemConstants.LOG_LEVEL_TRACE; // "TRACE"

// 日志格式
String logPatternConsole = SystemConstants.LOG_PATTERN_CONSOLE; // 控制台日志格式
String logPatternFile = SystemConstants.LOG_PATTERN_FILE; // 文件日志格式
```

### 线程池相关

```java
// 线程池配置
int corePoolSize = SystemConstants.CORE_POOL_SIZE; // CPU核心数
int maxPoolSize = SystemConstants.MAX_POOL_SIZE; // CPU核心数 * 2
int queueCapacity = SystemConstants.QUEUE_CAPACITY; // 1000
long keepAliveTime = SystemConstants.KEEP_ALIVE_TIME; // 60L
```

### 分页相关

```java
// 分页配置
int defaultPageSize = SystemConstants.DEFAULT_PAGE_SIZE; // 10
int maxPageSize = SystemConstants.MAX_PAGE_SIZE; // 1000
int defaultPageNum = SystemConstants.DEFAULT_PAGE_NUM; // 1
String defaultSortField = SystemConstants.DEFAULT_SORT_FIELD; // "id"
String defaultSortOrder = SystemConstants.DEFAULT_SORT_ORDER; // "desc"
```

### 文件相关

```java
// 文件配置
String defaultCharset = SystemConstants.DEFAULT_CHARSET; // "UTF-8"
String defaultEncoding = SystemConstants.DEFAULT_ENCODING; // "UTF-8"
String defaultTimezone = SystemConstants.DEFAULT_TIMEZONE; // "Asia/Shanghai"
String defaultLocale = SystemConstants.DEFAULT_LOCALE; // "zh_CN"
```

### 数据库相关

```java
// 数据库配置
String defaultSchema = SystemConstants.DB_DEFAULT_SCHEMA; // "public"
String defaultCatalog = SystemConstants.DB_DEFAULT_CATALOG; // "public"
int defaultTimeout = SystemConstants.DB_DEFAULT_TIMEOUT; // 30
int maxRetry = SystemConstants.DB_MAX_RETRY; // 3
```

### 缓存相关

```java
// 缓存配置
String defaultNamespace = SystemConstants.CACHE_DEFAULT_NAMESPACE; // "xiaoxin:iam"
long defaultTtl = SystemConstants.CACHE_DEFAULT_TTL; // 3600L
int defaultMaxSize = SystemConstants.CACHE_DEFAULT_MAX_SIZE; // 10000
```

### 安全相关

```java
// 安全配置
String defaultAlgorithm = SystemConstants.SECURITY_DEFAULT_ALGORITHM; // "AES"
int defaultKeyLength = SystemConstants.SECURITY_DEFAULT_KEY_LENGTH; // 128
int defaultSaltLength = SystemConstants.SECURITY_DEFAULT_SALT_LENGTH; // 16
int defaultIterations = SystemConstants.SECURITY_DEFAULT_ITERATIONS; // 10000
```

### 限流相关

```java
// 限流配置
int defaultCapacity = SystemConstants.RATE_LIMIT_DEFAULT_CAPACITY; // 100
int defaultRefillRate = SystemConstants.RATE_LIMIT_DEFAULT_REFILL_RATE; // 10
int defaultWindowSize = SystemConstants.RATE_LIMIT_DEFAULT_WINDOW_SIZE; // 60
```

### 监控相关

```java
// 监控配置
String metricsPrefix = SystemConstants.METRICS_PREFIX; // "xiaoxin.iam"
String healthCheckPath = SystemConstants.HEALTH_CHECK_PATH; // "/actuator/health"
String metricsPath = SystemConstants.METRICS_PATH; // "/actuator/metrics"
String infoPath = SystemConstants.INFO_PATH; // "/actuator/info"
```

### API相关

```java
// API配置
String apiPrefix = SystemConstants.API_PREFIX; // "/api/v1"
String apiDocsPath = SystemConstants.API_DOCS_PATH; // "/api-docs"
String swaggerUiPath = SystemConstants.SWAGGER_UI_PATH; // "/swagger-ui.html"
String openApiPath = SystemConstants.OPENAPI_PATH; // "/v3/api-docs"
```

### 消息相关

```java
// 消息主题
String userTopic = SystemConstants.MESSAGE_TOPIC_USER; // "user.topic"
String roleTopic = SystemConstants.MESSAGE_TOPIC_ROLE; // "role.topic"
String permissionTopic = SystemConstants.MESSAGE_TOPIC_PERMISSION; // "permission.topic"
String auditTopic = SystemConstants.MESSAGE_TOPIC_AUDIT; // "audit.topic"
```

### 任务相关

```java
// 任务配置
String taskGroupDefault = SystemConstants.TASK_GROUP_DEFAULT; // "default"
String taskGroupSystem = SystemConstants.TASK_GROUP_SYSTEM; // "system"
String taskGroupBusiness = SystemConstants.TASK_GROUP_BUSINESS; // "business"
int taskDefaultTimeout = SystemConstants.TASK_DEFAULT_TIMEOUT; // 300
```

### 事件相关

```java
// 事件类型
String eventUserLogin = SystemConstants.EVENT_USER_LOGIN; // "user.login"
String eventUserLogout = SystemConstants.EVENT_USER_LOGOUT; // "user.logout"
String eventUserRegister = SystemConstants.EVENT_USER_REGISTER; // "user.register"
String eventUserUpdate = SystemConstants.EVENT_USER_UPDATE; // "user.update"
String eventUserDelete = SystemConstants.EVENT_USER_DELETE; // "user.delete"
```

### 状态相关

```java
// 状态标识
String statusActive = SystemConstants.STATUS_ACTIVE; // "active"
String statusInactive = SystemConstants.STATUS_INACTIVE; // "inactive"
String statusPending = SystemConstants.STATUS_PENDING; // "pending"
String statusApproved = SystemConstants.STATUS_APPROVED; // "approved"
String statusRejected = SystemConstants.STATUS_REJECTED; // "rejected"
String statusExpired = SystemConstants.STATUS_EXPIRED; // "expired"
String statusCancelled = SystemConstants.STATUS_CANCELLED; // "cancelled"
```

### 类型相关

```java
// 类型标识
String typeSystem = SystemConstants.TYPE_SYSTEM; // "system"
String typeUser = SystemConstants.TYPE_USER; // "user"
String typeAdmin = SystemConstants.TYPE_ADMIN; // "admin"
String typeGuest = SystemConstants.TYPE_GUEST; // "guest"
String typeApi = SystemConstants.TYPE_API; // "api"
String typeWeb = SystemConstants.TYPE_WEB; // "web"
String typeMobile = SystemConstants.TYPE_MOBILE; // "mobile"
```

### 级别相关

```java
// 级别标识
String levelDebug = SystemConstants.LEVEL_DEBUG; // "debug"
String levelInfo = SystemConstants.LEVEL_INFO; // "info"
String levelWarn = SystemConstants.LEVEL_WARN; // "warn"
String levelError = SystemConstants.LEVEL_ERROR; // "error"
String levelFatal = SystemConstants.LEVEL_FATAL; // "fatal"
```

### 操作相关

```java
// 操作类型
String operationCreate = SystemConstants.OPERATION_CREATE; // "create"
String operationRead = SystemConstants.OPERATION_READ; // "read"
String operationUpdate = SystemConstants.OPERATION_UPDATE; // "update"
String operationDelete = SystemConstants.OPERATION_DELETE; // "delete"
String operationExport = SystemConstants.OPERATION_EXPORT; // "export"
String operationImport = SystemConstants.OPERATION_IMPORT; // "import"
String operationLogin = SystemConstants.OPERATION_LOGIN; // "login"
String operationLogout = SystemConstants.OPERATION_LOGOUT; // "logout"
```

### 资源相关

```java
// 资源类型
String resourceMenu = SystemConstants.RESOURCE_MENU; // "menu"
String resourceButton = SystemConstants.RESOURCE_BUTTON; // "button"
String resourceApi = SystemConstants.RESOURCE_API; // "api"
String resourceData = SystemConstants.RESOURCE_DATA; // "data"
String resourceFile = SystemConstants.RESOURCE_FILE; // "file"
String resourceImage = SystemConstants.RESOURCE_IMAGE; // "image"
String resourceDocument = SystemConstants.RESOURCE_DOCUMENT; // "document"
```

### 模块相关

```java
// 模块标识
String moduleUser = SystemConstants.MODULE_USER; // "user"
String moduleRole = SystemConstants.MODULE_ROLE; // "role"
String modulePermission = SystemConstants.MODULE_PERMISSION; // "permission"
String moduleMenu = SystemConstants.MODULE_MENU; // "menu"
String moduleDict = SystemConstants.MODULE_DICT; // "dict"
String moduleConfig = SystemConstants.MODULE_CONFIG; // "config"
String moduleLog = SystemConstants.MODULE_LOG; // "log"
String moduleMonitor = SystemConstants.MODULE_MONITOR; // "monitor"
String moduleSystem = SystemConstants.MODULE_SYSTEM; // "system"
```

## 使用建议

### 1. 常量命名规范

- 使用全大写字母和下划线分隔
- 名称要有意义，能够清楚表达常量的用途
- 按照功能模块分组，便于维护

### 2. 常量使用原则

- 优先使用常量而不是硬编码字符串
- 在配置类中引用常量，避免重复定义
- 定期检查常量的使用情况，及时清理未使用的常量

### 3. 常量维护

- 新增常量时要考虑向后兼容性
- 修改常量时要评估影响范围
- 删除常量前要确认没有地方在使用

### 4. 性能考虑

- 常量在编译时确定，性能优于运行时计算
- 避免在常量中调用方法，除非是系统属性
- 合理使用静态导入，减少代码冗余

### 5. 扩展建议

- 根据业务需求添加新的常量类
- 为不同模块创建专门的常量类
- 考虑国际化需求，添加多语言支持

## 注意事项

1. **常量不可变**：所有常量都应该是 final 的，确保不可修改
2. **线程安全**：常量是线程安全的，可以在多线程环境中使用
3. **内存占用**：常量会占用永久代内存，要合理控制数量
4. **版本兼容**：修改常量时要考虑版本兼容性
5. **文档更新**：修改常量时要同步更新相关文档

这些常量定义为整个IAM平台提供了统一的标准和规范，能够大大提高代码的可维护性和一致性。
