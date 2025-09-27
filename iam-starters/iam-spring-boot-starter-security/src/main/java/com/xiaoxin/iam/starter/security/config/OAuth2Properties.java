package com.xiaoxin.iam.starter.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OAuth2配置属性
 *
 * @author xiaoxin
 * @date 2024-09-26
 */
@Data
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {

    /**
     * OAuth2客户端配置
     */
    private Client client = new Client();

    /**
     * OAuth2授权服务器配置
     */
    private AuthorizationServer authorizationServer = new AuthorizationServer();

    /**
     * OAuth2客户端配置
     */
    @Data
    public static class Client {
        /**
         * 是否启用OAuth2客户端
         */
        private boolean enabled = false;

        /**
         * 客户端ID
         */
        private String clientId;

        /**
         * 客户端密钥
         */
        private String clientSecret;

        /**
         * 令牌端点URL
         */
        private String tokenUri;

        /**
         * 作用域
         */
        private String scope;

        /**
         * 获取格式化的作用域字符串
         * 将逗号分隔的作用域转换为OAuth2标准的空格分隔格式
         * 
         * @return OAuth2标准格式的作用域字符串
         */
        public String getFormattedScope() {
            if (scope == null || scope.trim().isEmpty()) {
                return scope;
            }
            // 将逗号分隔转换为空格分隔，符合OAuth2 RFC 6749标准
            return scope.replace(",", " ").replaceAll("\\s+", " ").trim();
        }

        /**
         * 连接超时时间（毫秒）
         */
        private int connectTimeout = 5000;

        /**
         * 读取超时时间（毫秒）
         */
        private int readTimeout = 10000;

        /**
         * 令牌缓存过期时间（秒）
         */
        private int tokenCacheExpire = 3300; // 55分钟，比令牌实际过期时间提前5分钟

        /**
         * 最大重试次数
         */
        private int maxRetries = 3;
    }

    /**
     * OAuth2授权服务器配置
     */
    @Data
    public static class AuthorizationServer {
        /**
         * 发行者URL
         */
        private String issuer;

        /**
         * 客户端配置
         */
        private ClientRegistration client = new ClientRegistration();

        /**
         * 客户端注册配置
         */
        @Data
        public static class ClientRegistration {
            /**
             * 客户端配置映射
             */
            private java.util.Map<String, ClientDetails> clients = new java.util.HashMap<>();

            /**
             * 客户端详情
             */
            @Data
            public static class ClientDetails {
                /**
                 * 注册配置
                 */
                private Registration registration = new Registration();

                /**
                 * 是否需要授权同意
                 */
                private boolean requireAuthorizationConsent = false;

                /**
                 * 注册详情
                 */
                @Data
                public static class Registration {
                    /**
                     * 客户端ID
                     */
                    private String clientId;

                    /**
                     * 客户端密钥
                     */
                    private String clientSecret;

                    /**
                     * 客户端认证方法
                     */
                    private java.util.List<String> clientAuthenticationMethods = java.util.List.of("client_secret_basic", "client_secret_post");

                    /**
                     * 授权类型
                     */
                    private java.util.List<String> authorizationGrantTypes = java.util.List.of("client_credentials");

                    /**
                     * 重定向URI
                     */
                    private java.util.List<String> redirectUris = new java.util.ArrayList<>();

                    /**
                     * 作用域
                     */
                    private java.util.List<String> scopes = new java.util.ArrayList<>();
                }
            }
        }
    }
}
