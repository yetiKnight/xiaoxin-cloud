package com.xiaoxin.iam.common.constant;

/**
 * 通用常量
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public class CommonConstants {

    /**
     * 成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "登录成功";

    /**
     * 退出成功
     */
    public static final String LOGOUT_SUCCESS = "退出成功";

    /**
     * 注册成功
     */
    public static final String REGISTER_SUCCESS = "注册成功";

    /**
     * 删除成功
     */
    public static final String DELETE_SUCCESS = "删除成功";

    /**
     * 更新成功
     */
    public static final String UPDATE_SUCCESS = "更新成功";

    /**
     * 保存成功
     */
    public static final String SAVE_SUCCESS = "保存成功";

    /**
     * 操作成功
     */
    public static final String OPERATION_SUCCESS = "操作成功";

    /**
     * 操作失败
     */
    public static final String OPERATION_FAIL = "操作失败";

    /**
     * 默认页码
     */
    public static final Integer DEFAULT_PAGE_NUM = 1;

    /**
     * 默认页大小
     */
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大页大小
     */
    public static final Integer MAX_PAGE_SIZE = 1000;

    /**
     * 默认排序字段
     */
    public static final String DEFAULT_ORDER_BY = "create_time";

    /**
     * 默认排序方向
     */
    public static final String DEFAULT_ORDER_DIRECTION = "desc";

    /**
     * 升序
     */
    public static final String ASC = "asc";

    /**
     * 降序
     */
    public static final String DESC = "desc";

    /**
     * 是
     */
    public static final String YES = "1";

    /**
     * 否
     */
    public static final String NO = "0";

    /**
     * 启用
     */
    public static final String ENABLED = "1";

    /**
     * 禁用
     */
    public static final String DISABLED = "0";

    /**
     * 正常
     */
    public static final String NORMAL = "0";

    /**
     * 异常
     */
    public static final String EXCEPTION = "1";

    /**
     * 锁定
     */
    public static final String LOCKED = "1";

    /**
     * 未锁定
     */
    public static final String UNLOCKED = "0";

    /**
     * 删除标记
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";

    /**
     * 菜单类型
     */
    public static final String MENU_TYPE_DIRECTORY = "M";
    public static final String MENU_TYPE_MENU = "C";
    public static final String MENU_TYPE_BUTTON = "F";

    /**
     * 权限类型
     */
    public static final String PERMISSION_TYPE_MENU = "menu";
    public static final String PERMISSION_TYPE_BUTTON = "button";
    public static final String PERMISSION_TYPE_API = "api";

    /**
     * 数据权限类型
     */
    public static final String DATA_SCOPE_ALL = "1";
    public static final String DATA_SCOPE_CUSTOM = "2";
    public static final String DATA_SCOPE_DEPT = "3";
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 用户类型
     */
    public static final String USER_TYPE_SYSTEM = "00";
    public static final String USER_TYPE_REGISTER = "01";

    /**
     * 性别
     */
    public static final String SEX_MALE = "0";
    public static final String SEX_FEMALE = "1";
    public static final String SEX_UNKNOWN = "2";

    /**
     * 状态
     */
    public static final String STATUS_NORMAL = "0";
    public static final String STATUS_DISABLE = "1";

    /**
     * 租户ID
     */
    public static final String TENANT_ID = "tenant_id";

    /**
     * 用户ID
     */
    public static final String USER_ID = "user_id";

    /**
     * 用户名
     */
    public static final String USERNAME = "username";

    /**
     * 部门ID
     */
    public static final String DEPT_ID = "dept_id";

    /**
     * 角色ID
     */
    public static final String ROLE_ID = "role_id";

    /**
     * 权限标识
     */
    public static final String PERMISSION = "permission";

    /**
     * 请求头
     */
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    /**
     * 缓存键前缀
     */
    public static final String CACHE_PREFIX = "iam:";
    public static final String CACHE_USER_PREFIX = CACHE_PREFIX + "user:";
    public static final String CACHE_ROLE_PREFIX = CACHE_PREFIX + "role:";
    public static final String CACHE_PERMISSION_PREFIX = CACHE_PREFIX + "permission:";
    public static final String CACHE_DEPT_PREFIX = CACHE_PREFIX + "dept:";
    public static final String CACHE_DICT_PREFIX = CACHE_PREFIX + "dict:";
    public static final String CACHE_CONFIG_PREFIX = CACHE_PREFIX + "config:";

    /**
     * 缓存过期时间（秒）
     */
    public static final long CACHE_EXPIRE_TIME = 3600L;
    public static final long CACHE_USER_EXPIRE_TIME = 1800L;
    public static final long CACHE_ROLE_EXPIRE_TIME = 3600L;
    public static final long CACHE_PERMISSION_EXPIRE_TIME = 3600L;
    public static final long CACHE_DEPT_EXPIRE_TIME = 3600L;
    public static final long CACHE_DICT_EXPIRE_TIME = 7200L;
    public static final long CACHE_CONFIG_EXPIRE_TIME = 7200L;

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "/avatar/default.jpg";

    /**
     * 文件上传路径
     */
    public static final String UPLOAD_PATH = "/upload/";

    /**
     * 临时文件路径
     */
    public static final String TEMP_PATH = "/temp/";

    /**
     * 日志类型
     */
    public static final String LOG_TYPE_LOGIN = "login";
    public static final String LOG_TYPE_LOGOUT = "logout";
    public static final String LOG_TYPE_OPERATION = "operation";
    public static final String LOG_TYPE_ERROR = "error";

    /**
     * 操作类型
     */
    public static final String OPERATION_TYPE_INSERT = "insert";
    public static final String OPERATION_TYPE_UPDATE = "update";
    public static final String OPERATION_TYPE_DELETE = "delete";
    public static final String OPERATION_TYPE_SELECT = "select";
    public static final String OPERATION_TYPE_EXPORT = "export";
    public static final String OPERATION_TYPE_IMPORT = "import";

    /**
     * 消息类型
     */
    public static final String MESSAGE_TYPE_SYSTEM = "system";
    public static final String MESSAGE_TYPE_NOTICE = "notice";
    public static final String MESSAGE_TYPE_WARNING = "warning";
    public static final String MESSAGE_TYPE_ERROR = "error";

    /**
     * 通知类型
     */
    public static final String NOTIFICATION_TYPE_EMAIL = "email";
    public static final String NOTIFICATION_TYPE_SMS = "sms";
    public static final String NOTIFICATION_TYPE_WEB = "web";
    public static final String NOTIFICATION_TYPE_WECHAT = "wechat";
}
