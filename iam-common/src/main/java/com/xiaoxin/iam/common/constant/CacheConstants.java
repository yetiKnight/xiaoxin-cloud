package com.xiaoxin.iam.common.constant;

/**
 * 缓存相关常量定义
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public final class CacheConstants {

    private CacheConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 缓存键前缀 ====================
    public static final String PREFIX_USER = "user:";
    public static final String PREFIX_ROLE = "role:";
    public static final String PREFIX_PERMISSION = "permission:";
    public static final String PREFIX_MENU = "menu:";
    public static final String PREFIX_DICT = "dict:";
    public static final String PREFIX_CONFIG = "config:";
    public static final String PREFIX_SESSION = "session:";
    public static final String PREFIX_TOKEN = "token:";
    public static final String PREFIX_CAPTCHA = "captcha:";
    public static final String PREFIX_RATE_LIMIT = "rate_limit:";
    public static final String PREFIX_LOCK = "lock:";
    public static final String PREFIX_VERIFY = "verify:";

    // ==================== 缓存过期时间（秒） ====================
    public static final long EXPIRE_DEFAULT = 3600; // 1小时
    public static final long EXPIRE_USER = 1800; // 30分钟
    public static final long EXPIRE_SESSION = 7200; // 2小时
    public static final long EXPIRE_TOKEN = 3600; // 1小时
    public static final long EXPIRE_CAPTCHA = 300; // 5分钟
    public static final long EXPIRE_RATE_LIMIT = 60; // 1分钟
    public static final long EXPIRE_LOCK = 30; // 30秒
    public static final long EXPIRE_VERIFY = 600; // 10分钟
    public static final long EXPIRE_PERMANENT = -1; // 永不过期

    // ==================== 缓存键模板 ====================
    public static final String USER_INFO_KEY = PREFIX_USER + "info:%s";
    public static final String USER_ROLES_KEY = PREFIX_USER + "roles:%s";
    public static final String USER_PERMISSIONS_KEY = PREFIX_USER + "permissions:%s";
    public static final String USER_MENUS_KEY = PREFIX_USER + "menus:%s";
    public static final String ROLE_INFO_KEY = PREFIX_ROLE + "info:%s";
    public static final String ROLE_PERMISSIONS_KEY = PREFIX_ROLE + "permissions:%s";
    public static final String PERMISSION_INFO_KEY = PREFIX_PERMISSION + "info:%s";
    public static final String MENU_TREE_KEY = PREFIX_MENU + "tree";
    public static final String DICT_DATA_KEY = PREFIX_DICT + "data:%s";
    public static final String CONFIG_VALUE_KEY = PREFIX_CONFIG + "value:%s";
    public static final String SESSION_KEY = PREFIX_SESSION + "%s";
    public static final String TOKEN_KEY = PREFIX_TOKEN + "%s";
    public static final String CAPTCHA_KEY = PREFIX_CAPTCHA + "%s";
    public static final String RATE_LIMIT_KEY = PREFIX_RATE_LIMIT + "%s:%s";
    public static final String LOCK_KEY = PREFIX_LOCK + "%s";
    public static final String VERIFY_KEY = PREFIX_VERIFY + "%s";

    // ==================== 缓存配置 ====================
    public static final String CACHE_MANAGER_REDIS = "redisCacheManager";
    public static final String CACHE_MANAGER_CAFFEINE = "caffeineCacheManager";
    public static final String CACHE_MANAGER_COMPOSITE = "compositeCacheManager";

    // ==================== 缓存名称 ====================
    public static final String CACHE_NAME_USER = "userCache";
    public static final String CACHE_NAME_ROLE = "roleCache";
    public static final String CACHE_NAME_PERMISSION = "permissionCache";
    public static final String CACHE_NAME_MENU = "menuCache";
    public static final String CACHE_NAME_DICT = "dictCache";
    public static final String CACHE_NAME_CONFIG = "configCache";
    public static final String CACHE_NAME_SESSION = "sessionCache";
    public static final String CACHE_NAME_TOKEN = "tokenCache";
    public static final String CACHE_NAME_CAPTCHA = "captchaCache";
    public static final String CACHE_NAME_RATE_LIMIT = "rateLimitCache";
    public static final String CACHE_NAME_LOCK = "lockCache";
    public static final String CACHE_NAME_VERIFY = "verifyCache";

    // ==================== 缓存策略 ====================
    public static final String CACHE_STRATEGY_LRU = "lru";
    public static final String CACHE_STRATEGY_LFU = "lfu";
    public static final String CACHE_STRATEGY_FIFO = "fifo";
    public static final String CACHE_STRATEGY_TTL = "ttl";

    // ==================== 缓存操作 ====================
    public static final String CACHE_OPERATION_GET = "get";
    public static final String CACHE_OPERATION_PUT = "put";
    public static final String CACHE_OPERATION_EVICT = "evict";
    public static final String CACHE_OPERATION_CLEAR = "clear";
    public static final String CACHE_OPERATION_REFRESH = "refresh";

    // ==================== 缓存统计 ====================
    public static final String CACHE_STATS_HIT_COUNT = "hitCount";
    public static final String CACHE_STATS_MISS_COUNT = "missCount";
    public static final String CACHE_STATS_HIT_RATE = "hitRate";
    public static final String CACHE_STATS_MISS_RATE = "missRate";
    public static final String CACHE_STATS_EVICTION_COUNT = "evictionCount";
    public static final String CACHE_STATS_LOAD_COUNT = "loadCount";
    public static final String CACHE_STATS_LOAD_TIME = "loadTime";
    public static final String CACHE_STATS_SIZE = "size";
}
