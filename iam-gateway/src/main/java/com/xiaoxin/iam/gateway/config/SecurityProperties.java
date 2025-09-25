package com.xiaoxin.iam.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 安全配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "iam.security")
public class SecurityProperties {

    /**
     * JWT相关配置
     */
    private Jwt jwt = new Jwt();

    /**
     * CORS相关配置
     */
    private Cors cors = new Cors();

    /**
     * 安全头配置
     */
    private SecurityHeaders securityHeaders = new SecurityHeaders();

    /**
     * 认证相关配置
     */
    private Auth auth = new Auth();

    @Data
    public static class Jwt {
        /**
         * JWT签名密钥
         */
        private String secret = "xiaoxin-iam-platform-jwt-secret-key-2024";

        /**
         * Token过期时间（秒）
         */
        private Long expiration = 7200L;

        /**
         * 刷新Token过期时间（秒）
         */
        private Long refreshExpiration = 604800L;

        /**
         * 发行者
         */
        private String issuer = "xiaoxin-iam";

        /**
         * Token前缀
         */
        private String tokenPrefix = "Bearer ";

        /**
         * 是否启用Token刷新
         */
        private Boolean enableRefresh = true;
    }

    @Data
    public static class Cors {
        /**
         * 是否启用CORS
         */
        private Boolean enabled = true;

        /**
         * 允许的源
         */
        private List<String> allowedOrigins = new ArrayList<String>() {{
            add("http://localhost:3000");
            add("http://localhost:8088");
            add("https://iam.xiaoxin.com");
        }};

        /**
         * 允许的方法
         */
        private List<String> allowedMethods = new ArrayList<String>() {{
            add("GET");
            add("POST");
            add("PUT");
            add("DELETE");
            add("OPTIONS");
            add("PATCH");
        }};

        /**
         * 允许的头
         */
        private List<String> allowedHeaders = new ArrayList<String>() {{
            add("*");
        }};

        /**
         * 暴露的头
         */
        private List<String> exposedHeaders = new ArrayList<String>() {{
            add("X-Request-Id");
            add("X-Response-Time");
            add("X-Gateway-Version");
        }};

        /**
         * 是否允许携带凭证
         */
        private Boolean allowCredentials = true;

        /**
         * 预检请求缓存时间（秒）
         */
        private Long maxAge = 3600L;
    }

    @Data
    public static class SecurityHeaders {
        /**
         * 是否启用安全头
         */
        private Boolean enabled = true;

        /**
         * Content Security Policy
         */
        private String contentSecurityPolicy = "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' data:; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none'";

        /**
         * Strict Transport Security最大年龄
         */
        private Long hstsMaxAge = 31536000L;

        /**
         * 是否包含子域名
         */
        private Boolean hstsIncludeSubdomains = true;

        /**
         * 是否预加载
         */
        private Boolean hstsPreload = true;

        /**
         * Referrer Policy
         */
        private String referrerPolicy = "strict-origin-when-cross-origin";

        /**
         * Permissions Policy
         */
        private String permissionsPolicy = "camera=(), microphone=(), geolocation=(), payment=()";
    }

    @Data
    public static class Auth {
        /**
         * 是否启用认证
         */
        private Boolean enabled = true;

        /**
         * 认证白名单
         */
        private List<String> whitelist = new ArrayList<String>() {{
            add("/api/v1/auth/login");
            add("/api/v1/auth/logout");
            add("/api/v1/auth/register");
            add("/api/v1/auth/refresh");
            add("/api/v1/auth/captcha");
            add("/api/v1/auth/oauth2/**");
            add("/actuator/health");
            add("/actuator/info");
            add("/favicon.ico");
            add("/assets/**");
            add("/static/**");
            add("/");
            add("/login");
            add("/register");
            add("/error");
        }};

        /**
         * 管理员路径（需要管理员权限）
         */
        private List<String> adminPaths = new ArrayList<String>() {{
            add("/api/v1/system/**");
            add("/api/v1/audit/**");
            add("/actuator/**");
        }};

        /**
         * Token在Header中的名称
         */
        private String tokenHeader = "Authorization";

        /**
         * Token在查询参数中的名称
         */
        private String tokenParam = "token";

        /**
         * 是否启用IP白名单
         */
        private Boolean enableIpWhitelist = false;

        /**
         * IP白名单
         */
        private List<String> ipWhitelist = new ArrayList<>();

        /**
         * 是否启用频率限制
         */
        private Boolean enableRateLimit = true;

        /**
         * 默认频率限制（每秒请求数）
         */
        private Integer defaultRateLimit = 100;
    }
}
