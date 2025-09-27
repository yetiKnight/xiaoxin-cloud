package com.xiaoxin.iam.auth.constant;

/**
 * 认证服务常量定义
 * 包含认证服务相关的常量值
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public final class AuthConstants {

    private AuthConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ==================== 认证接口路径常量 ====================
    
    /**
     * 登录接口路径
     */
    public static final String LOGIN_PATH = "/api/v1/auth/login";
    
    /**
     * 登出接口路径
     */
    public static final String LOGOUT_PATH = "/api/v1/auth/logout";
    
    /**
     * 刷新Token接口路径
     */
    public static final String REFRESH_PATH = "/api/v1/auth/refresh";
    
    /**
     * 用户注册接口路径
     */
    public static final String REGISTER_PATH = "/api/v1/auth/register";
    
    /**
     * 验证码接口路径
     */
    public static final String CAPTCHA_PATH = "/api/v1/auth/captcha";
    
    /**
     * OAuth2相关接口路径前缀
     */
    public static final String OAUTH2_PATH_PREFIX = "/api/v1/auth/oauth2/**";
    
    /**
     * OAuth2回调接口路径前缀
     */
    public static final String OAUTH2_CALLBACK_PATH_PREFIX = "/api/v1/auth/callback/**";

    // ==================== 系统接口路径常量 ====================
    
    /**
     * 健康检查接口路径前缀
     */
    public static final String ACTUATOR_PATH_PREFIX = "/actuator/**";
    
    /**
     * API文档接口路径
     */
    public static final String[] DOCUMENTATION_PATHS = {
        "/doc.html", 
        "/swagger-ui/**", 
        "/v3/api-docs/**"
    };

    // ==================== 安全相关常量 ====================
    
    /**
     * BCrypt密码编码器强度
     */
    public static final int BCRYPT_STRENGTH = 12;
    
    /**
     * JWT Token前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    
    /**
     * JWT Token Header名称
     */
    public static final String JWT_TOKEN_HEADER = "Authorization";

    // ==================== 认证相关常量 ====================
    
    /**
     * 默认密码长度
     */
    public static final int DEFAULT_PASSWORD_LENGTH = 8;
    
    /**
     * 最小密码长度
     */
    public static final int MIN_PASSWORD_LENGTH = 6;
    
    /**
     * 最大密码长度
     */
    public static final int MAX_PASSWORD_LENGTH = 20;
    
    /**
     * 验证码长度
     */
    public static final int CAPTCHA_LENGTH = 4;
    
    /**
     * 验证码过期时间（秒）
     */
    public static final int CAPTCHA_EXPIRE_SECONDS = 300;
    
    /**
     * 登录失败最大尝试次数
     */
    public static final int MAX_LOGIN_ATTEMPTS = 5;
    
    /**
     * 账户锁定时间（分钟）
     */
    public static final int ACCOUNT_LOCK_TIME_MINUTES = 30;
}
