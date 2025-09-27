package com.xiaoxin.iam.core.constant;

/**
 * 核心服务常量类
 * 定义核心服务相关的常量
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class CoreConstants {

    /**
     * 内部API路径常量
     */
    public static final String INTERNAL_API_PREFIX = "/api/v1/internal";
    
    /**
     * 用户相关内部API路径
     */
    public static final String INTERNAL_USERS_PATH = INTERNAL_API_PREFIX + "/users";
    public static final String INTERNAL_USER_BY_USERNAME_PATH = INTERNAL_USERS_PATH + "/username";
    public static final String INTERNAL_USER_LOGIN_INFO_PATH = INTERNAL_USERS_PATH + "/login-info";
    
    /**
     * 角色相关内部API路径
     */
    public static final String INTERNAL_ROLES_PATH = INTERNAL_API_PREFIX + "/roles";
    
    /**
     * 权限相关内部API路径
     */
    public static final String INTERNAL_PERMISSIONS_PATH = INTERNAL_API_PREFIX + "/permissions";
    
    /**
     * 部门相关内部API路径
     */
    public static final String INTERNAL_DEPTS_PATH = INTERNAL_API_PREFIX + "/depts";
    
    /**
     * 菜单相关内部API路径
     */
    public static final String INTERNAL_MENUS_PATH = INTERNAL_API_PREFIX + "/menus";
    
    /**
     * 系统接口路径常量
     */
    public static final String ACTUATOR_PATH = "/actuator/**";
    public static final String DOC_PATH = "/doc.html";
    public static final String SWAGGER_UI_PATH = "/swagger-ui/**";
    public static final String API_DOCS_PATH = "/v3/api-docs/**";
    
    /**
     * 安全相关常量
     */
    public static final String SERVICE_AUTH_HEADER = "X-Service-Auth";
    public static final String SERVICE_AUTH_TOKEN = "iam-service-token";
    
    /**
     * 权限常量
     */
    public static final String USER_READ_PERMISSION = "user:read";
    public static final String USER_CREATE_PERMISSION = "user:create";
    public static final String USER_UPDATE_PERMISSION = "user:update";
    public static final String USER_DELETE_PERMISSION = "user:delete";
    
    public static final String ROLE_READ_PERMISSION = "role:read";
    public static final String ROLE_CREATE_PERMISSION = "role:create";
    public static final String ROLE_UPDATE_PERMISSION = "role:update";
    public static final String ROLE_DELETE_PERMISSION = "role:delete";
    
    public static final String PERMISSION_READ_PERMISSION = "permission:read";
    public static final String PERMISSION_CREATE_PERMISSION = "permission:create";
    public static final String PERMISSION_UPDATE_PERMISSION = "permission:update";
    public static final String PERMISSION_DELETE_PERMISSION = "permission:delete";
    
    public static final String DEPT_READ_PERMISSION = "dept:read";
    public static final String DEPT_CREATE_PERMISSION = "dept:create";
    public static final String DEPT_UPDATE_PERMISSION = "dept:update";
    public static final String DEPT_DELETE_PERMISSION = "dept:delete";
    
    public static final String MENU_READ_PERMISSION = "menu:read";
    public static final String MENU_CREATE_PERMISSION = "menu:create";
    public static final String MENU_UPDATE_PERMISSION = "menu:update";
    public static final String MENU_DELETE_PERMISSION = "menu:delete";
    
    /**
     * 数据权限常量
     */
    public static final String DATA_SCOPE_ALL = "1";        // 全部数据权限
    public static final String DATA_SCOPE_CUSTOM = "2";     // 自定义数据权限
    public static final String DATA_SCOPE_DEPT = "3";       // 部门数据权限
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4"; // 部门及以下数据权限
    public static final String DATA_SCOPE_SELF = "5";       // 仅本人数据权限
    
    /**
     * 用户状态常量
     */
    public static final String USER_STATUS_NORMAL = "0";    // 正常
    public static final String USER_STATUS_DISABLED = "1";  // 停用
    public static final String USER_STATUS_LOCKED = "2";    // 锁定
    
    /**
     * 角色状态常量
     */
    public static final String ROLE_STATUS_NORMAL = "0";    // 正常
    public static final String ROLE_STATUS_DISABLED = "1";  // 停用
    
    /**
     * 权限类型常量
     */
    public static final String PERMISSION_TYPE_FUNCTION = "1";  // 功能权限
    public static final String PERMISSION_TYPE_DATA = "2";      // 数据权限
    public static final String PERMISSION_TYPE_FIELD = "3";     // 字段权限
    
    /**
     * 资源类型常量
     */
    public static final String RESOURCE_TYPE_API = "API";       // API接口
    public static final String RESOURCE_TYPE_MENU = "MENU";     // 菜单
    public static final String RESOURCE_TYPE_BUTTON = "BUTTON"; // 按钮
    public static final String RESOURCE_TYPE_DATA = "DATA";     // 数据
    
    /**
     * HTTP方法常量
     */
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String HTTP_METHOD_PATCH = "PATCH";
    
    /**
     * 性别常量
     */
    public static final String SEX_MALE = "0";     // 男
    public static final String SEX_FEMALE = "1";   // 女
    public static final String SEX_UNKNOWN = "2";  // 未知
    
    /**
     * 默认值常量
     */
    public static final String DEFAULT_PASSWORD = "123456";
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;
    
    /**
     * 缓存键常量
     */
    public static final String CACHE_USER_PREFIX = "iam:core:user:";
    public static final String CACHE_ROLE_PREFIX = "iam:core:role:";
    public static final String CACHE_PERMISSION_PREFIX = "iam:core:permission:";
    public static final String CACHE_DEPT_PREFIX = "iam:core:dept:";
    public static final String CACHE_MENU_PREFIX = "iam:core:menu:";
    
    /**
     * 缓存过期时间常量（秒）
     */
    public static final int CACHE_EXPIRE_TIME_30_MIN = 30 * 60;
    public static final int CACHE_EXPIRE_TIME_1_HOUR = 60 * 60;
    public static final int CACHE_EXPIRE_TIME_2_HOUR = 2 * 60 * 60;
    public static final int CACHE_EXPIRE_TIME_1_DAY = 24 * 60 * 60;
}
