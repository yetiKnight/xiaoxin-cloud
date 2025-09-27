/*
 * Copyright 2024 xiaoxin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoxin.iam.starter.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * Web相关配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.web")
public class WebProperties {

    /**
     * CORS配置
     */
    private Cors cors = new Cors();

    /**
     * API文档配置
     */
    private ApiDoc apiDoc = new ApiDoc();

    /**
     * HTTP客户端配置
     */
    private HttpClient httpClient = new HttpClient();

    /**
     * 请求日志配置
     */
    private RequestLog requestLog = new RequestLog();

    /**
     * Jackson配置
     */
    private Jackson jackson = new Jackson();

    /**
     * 文件上传配置
     */
    private FileUpload fileUpload = new FileUpload();

    /**
     * CORS配置类
     */
    @Data
    public static class Cors {
        /**
         * 是否启用CORS
         */
        private boolean enabled = true;

        /**
         * 允许的源模式
         */
        private List<String> allowedOriginPatterns = List.of("*");

        /**
         * 允许的源（精确匹配）
         */
        private List<String> allowedOrigins = List.of();

        /**
         * 允许的方法
         */
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD");

        /**
         * 允许的请求头
         */
        private List<String> allowedHeaders = List.of("*");

        /**
         * 暴露的响应头
         */
        private List<String> exposedHeaders = List.of("Content-Disposition", "Content-Length", "Content-Type");

        /**
         * 是否允许凭证
         */
        private boolean allowCredentials = true;

        /**
         * 预检请求缓存时间
         */
        private Duration maxAge = Duration.ofHours(1);

        /**
         * 是否允许私有网络
         */
        private boolean allowPrivateNetwork = true;

        /**
         * 是否启用CORS日志
         */
        private boolean enableLogging = false;
    }

    /**
     * API文档配置类
     */
    @Data
    public static class ApiDoc {
        /**
         * 是否启用API文档
         */
        private boolean enabled = true;

        /**
         * 文档标题
         */
        private String title = "IAM平台API文档";

        /**
         * 文档描述
         */
        private String description = "IAM平台RESTful API接口文档";

        /**
         * 版本
         */
        private String version = "1.0.0";

        /**
         * 联系人信息
         */
        private Contact contact = new Contact();

        /**
         * 许可证信息
         */
        private License license = new License();

        /**
         * 服务器信息
         */
        private java.util.List<Server> servers = java.util.List.of(
                new Server("开发环境", "http://localhost:8080", "开发环境服务器"),
                new Server("测试环境", "https://test-api.xiaoxin.com", "测试环境服务器"),
                new Server("生产环境", "https://api.xiaoxin.com", "生产环境服务器")
        );

        /**
         * 标签信息
         */
        private java.util.List<Tag> tags = java.util.List.of(
                new Tag("用户管理", "用户相关接口"),
                new Tag("权限管理", "权限相关接口"),
                new Tag("组织管理", "组织相关接口"),
                new Tag("系统管理", "系统相关接口")
        );

        /**
         * 安全配置
         */
        private Security security = new Security();

        /**
         * 是否启用分组
         */
        private boolean enableGrouping = true;

        /**
         * 是否启用搜索
         */
        private boolean enableSearch = true;

        /**
         * 是否启用在线调试
         */
        private boolean enableTryItOut = true;

        /**
         * 是否启用请求示例
         */
        private boolean enableRequestExample = true;

        /**
         * 是否启用响应示例
         */
        private boolean enableResponseExample = true;

        /**
         * 是否启用模型展示
         */
        private boolean enableModelView = true;

        /**
         * 是否启用接口排序
         */
        private boolean enableOperationSorting = true;

        /**
         * 接口排序方式
         */
        private String operationSorting = "method";

        /**
         * 是否启用深度链接
         */
        private boolean enableDeepLinking = true;

        /**
         * 是否启用显示请求头
         */
        private boolean enableDisplayRequestHeaders = true;

        /**
         * 是否启用显示响应头
         */
        private boolean enableDisplayResponseHeaders = true;

        /**
         * 是否启用显示操作ID
         */
        private boolean enableDisplayOperationId = true;

        /**
         * 是否启用显示标签
         */
        private boolean enableDisplayTags = true;

        /**
         * 是否启用显示模型
         */
        private boolean enableDisplayModels = true;

        /**
         * 是否启用显示请求时间
         */
        private boolean enableDisplayRequestDuration = true;

        /**
         * 是否启用显示请求大小
         */
        private boolean enableDisplayRequestSize = true;

        /**
         * 是否启用显示响应大小
         */
        private boolean enableDisplayResponseSize = true;

        /**
         * 联系人信息
         */
        @Data
        public static class Contact {
            private String name = "IAM Team";
            private String email = "iam@xiaoxin.com";
            private String url = "https://www.xiaoxin.com";
        }

        /**
         * 许可证信息
         */
        @Data
        public static class License {
            private String name = "Apache 2.0";
            private String url = "https://www.apache.org/licenses/LICENSE-2.0";
        }

        /**
         * 服务器信息
         */
        @Data
        public static class Server {
            private String name;
            private String url;
            private String description;

            public Server() {}

            public Server(String name, String url, String description) {
                this.name = name;
                this.url = url;
                this.description = description;
            }
        }

        /**
         * 标签信息
         */
        @Data
        public static class Tag {
            private String name;
            private String description;

            public Tag() {}

            public Tag(String name, String description) {
                this.name = name;
                this.description = description;
            }
        }

        /**
         * 安全配置
         */
        @Data
        public static class Security {
            /**
             * 是否启用JWT认证
             */
            private boolean enableJwt = true;

            /**
             * 是否启用Basic认证
             */
            private boolean enableBasic = false;

            /**
             * 是否启用API Key认证
             */
            private boolean enableApiKey = false;

            /**
             * 是否启用OAuth2认证
             */
            private boolean enableOAuth2 = false;

            /**
             * JWT配置
             */
            private Jwt jwt = new Jwt();

            /**
             * API Key配置
             */
            private ApiKey apiKey = new ApiKey();

            /**
             * OAuth2配置
             */
            private OAuth2 oauth2 = new OAuth2();

            /**
             * JWT配置
             */
            @Data
            public static class Jwt {
                private String name = "JWT认证";
                private String description = "使用JWT令牌进行认证";
                private String scheme = "bearer";
                private String bearerFormat = "JWT";
            }

            /**
             * API Key配置
             */
            @Data
            public static class ApiKey {
                private String name = "API Key";
                private String description = "使用API Key进行认证";
                private String in = "header";
                private String keyName = "X-API-Key";
            }

            /**
             * OAuth2配置
             */
            @Data
            public static class OAuth2 {
                private String name = "OAuth2认证";
                private String description = "使用OAuth2进行认证";
                private String authorizationUrl = "https://api.xiaoxin.com/oauth/authorize";
                private String tokenUrl = "https://api.xiaoxin.com/oauth/token";
                private java.util.List<String> scopes = java.util.List.of("read", "write");
            }
        }
    }

    /**
     * HTTP客户端配置类
     */
    @Data
    public static class HttpClient {
        /**
         * 连接超时时间
         */
        private Duration connectTimeout = Duration.ofSeconds(10);

        /**
         * 读取超时时间
         */
        private Duration readTimeout = Duration.ofSeconds(30);

        /**
         * 写入超时时间
         */
        private Duration writeTimeout = Duration.ofSeconds(30);

        /**
         * 最大连接数
         */
        private int maxConnections = 200;

        /**
         * 每个主机的最大连接数
         */
        private int maxConnectionsPerHost = 50;

        /**
         * 连接存活时间
         */
        private Duration keepAliveDuration = Duration.ofMinutes(5);

        /**
         * 是否启用重试
         */
        private boolean retryOnConnectionFailure = true;

        /**
         * 最大重试次数
         */
        private int maxRetries = 3;

        /**
         * 重试间隔时间
         */
        private Duration retryInterval = Duration.ofSeconds(1);

        /**
         * 是否启用压缩
         */
        private boolean enableCompression = true;

        /**
         * 是否启用HTTP/2
         */
        private boolean enableHttp2 = true;

        /**
         * 用户代理
         */
        private String userAgent = "IAM-Platform/1.0.0";

        /**
         * 是否启用连接池
         */
        private boolean enableConnectionPool = true;

        /**
         * 连接池清理间隔
         */
        private Duration connectionPoolCleanupInterval = Duration.ofMinutes(5);

        /**
         * 是否启用SSL验证
         */
        private boolean enableSslVerification = true;

        /**
         * 是否启用Cookie管理
         */
        private boolean enableCookieManagement = true;
    }

    /**
     * 请求日志配置类
     */
    @Data
    public static class RequestLog {
        /**
         * 是否启用请求日志
         */
        private boolean enabled = true;

        /**
         * 是否记录请求头
         */
        private boolean includeHeaders = false;

        /**
         * 是否记录请求体
         */
        private boolean includeRequestPayload = false;

        /**
         * 是否记录响应体
         */
        private boolean includeResponsePayload = false;

        /**
         * 最大请求体大小
         */
        private int maxPayloadLength = 1000;

        /**
         * 是否记录慢请求
         */
        private boolean logSlowRequests = true;

        /**
         * 慢请求阈值（毫秒）
         */
        private long slowRequestThreshold = 1000;

        /**
         * 是否记录错误请求
         */
        private boolean logErrorRequests = true;

        /**
         * 是否记录性能指标
         */
        private boolean logPerformanceMetrics = true;

        /**
         * 是否记录用户信息
         */
        private boolean includeUserInfo = false;

        /**
         * 是否记录会话信息
         */
        private boolean includeSessionInfo = false;

        /**
         * 是否记录IP地址
         */
        private boolean includeIpAddress = true;

        /**
         * 是否记录用户代理
         */
        private boolean includeUserAgent = true;

        /**
         * 是否记录引用页面
         */
        private boolean includeReferer = true;

        /**
         * 是否启用异步日志
         */
        private boolean enableAsyncLogging = false;

        /**
         * 日志级别
         */
        private String logLevel = "INFO";

        /**
         * 是否启用请求ID
         */
        private boolean enableRequestId = true;

        /**
         * 请求ID头名称
         */
        private String requestIdHeaderName = "X-Request-ID";

        /**
         * 是否记录响应时间
         */
        private boolean includeResponseTime = true;

        /**
         * 是否记录内存使用
         */
        private boolean includeMemoryUsage = false;

        /**
         * 是否记录线程信息
         */
        private boolean includeThreadInfo = false;

        /**
         * 排除的路径模式
         */
        private java.util.List<String> excludePathPatterns = java.util.List.of(
                "/actuator/**",
                "/static/**",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico",
                "/doc.html",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        );

        /**
         * 包含的路径模式
         */
        private java.util.List<String> includePathPatterns = java.util.List.of("/**");
    }

    /**
     * Jackson配置类
     */
    @Data
    public static class Jackson {
        /**
         * 是否启用Jackson配置
         */
        private boolean enabled = true;

        /**
         * 日期格式
         */
        private String dateFormat = "yyyy-MM-dd HH:mm:ss";

        /**
         * 时区
         */
        private String timeZone = "Asia/Shanghai";

        /**
         * 是否将日期序列化为时间戳
         */
        private boolean writeDatesAsTimestamps = false;

        /**
         * 是否忽略未知属性
         */
        private boolean failOnUnknownProperties = false;

        /**
         * 是否忽略空Bean
         */
        private boolean failOnEmptyBeans = false;

        /**
         * 是否忽略空值
         */
        private boolean includeNulls = true;

        /**
         * 是否启用缩进
         */
        private boolean indentOutput = false;

        /**
         * 是否启用默认类型
         */
        private boolean enableDefaultTyping = false;

        /**
         * 是否启用循环引用检测
         */
        private boolean enableCircularReferenceDetection = true;

        /**
         * 是否启用属性命名策略
         */
        private boolean enablePropertyNamingStrategy = false;

        /**
         * 属性命名策略
         */
        private String propertyNamingStrategy = "SNAKE_CASE";

        /**
         * 是否启用多态类型处理
         */
        private boolean enablePolymorphicTypeHandling = false;

        /**
         * 是否启用反序列化特性
         */
        private Deserialization deserialization = new Deserialization();

        /**
         * 是否启用序列化特性
         */
        private Serialization serialization = new Serialization();

        /**
         * 反序列化配置
         */
        @Data
        public static class Deserialization {
            /**
             * 是否忽略未知属性
             */
            private boolean failOnUnknownProperties = false;

            /**
             * 是否忽略空值
             */
            private boolean failOnNullForPrimitives = false;

            /**
             * 是否忽略无效子类型
             */
            private boolean failOnInvalidSubtype = false;

            /**
             * 是否忽略空数组
             */
            private boolean failOnEmptyBeans = false;
        }

        /**
         * 序列化配置
         */
        @Data
        public static class Serialization {
            /**
             * 是否将日期序列化为时间戳
             */
            private boolean writeDatesAsTimestamps = false;

            /**
             * 是否忽略空Bean
             */
            private boolean failOnEmptyBeans = false;

            /**
             * 是否忽略空值
             */
            private boolean includeNulls = true;

            /**
             * 是否启用缩进
             */
            private boolean indentOutput = false;

            /**
             * 是否启用默认类型
             */
            private boolean enableDefaultTyping = false;
        }
    }

    /**
     * 文件上传配置类
     */
    @Data
    public static class FileUpload {
        /**
         * 是否启用文件上传
         */
        private boolean enabled = true;

        /**
         * 最大文件大小
         */
        private String maxFileSize = "10MB";

        /**
         * 最大请求大小
         */
        private String maxRequestSize = "100MB";

        /**
         * 文件存储路径
         */
        private String uploadPath = "uploads";

        /**
         * 允许的文件类型
         */
        private java.util.List<String> allowedTypes = java.util.List.of(
                "image/jpeg", "image/png", "image/gif", "image/webp",
                "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "text/plain", "text/csv"
        );

        /**
         * 允许的文件扩展名
         */
        private java.util.List<String> allowedExtensions = java.util.List.of(
                "jpg", "jpeg", "png", "gif", "webp", "pdf", "doc", "docx", "xls", "xlsx", "txt", "csv"
        );

        /**
         * 是否启用文件类型验证
         */
        private boolean enableTypeValidation = true;

        /**
         * 是否启用文件大小验证
         */
        private boolean enableSizeValidation = true;

        /**
         * 是否启用文件名验证
         */
        private boolean enableNameValidation = true;

        /**
         * 是否启用病毒扫描
         */
        private boolean enableVirusScan = false;

        /**
         * 是否启用文件压缩
         */
        private boolean enableCompression = false;

        /**
         * 压缩质量
         */
        private int compressionQuality = 80;

        /**
         * 是否启用缩略图生成
         */
        private boolean enableThumbnail = false;

        /**
         * 缩略图大小
         */
        private String thumbnailSize = "200x200";

        /**
         * 是否启用文件去重
         */
        private boolean enableDeduplication = true;

        /**
         * 是否启用文件加密
         */
        private boolean enableEncryption = false;

        /**
         * 加密算法
         */
        private String encryptionAlgorithm = "AES";

        /**
         * 是否启用文件备份
         */
        private boolean enableBackup = false;

        /**
         * 备份路径
         */
        private String backupPath = "backups";

        /**
         * 是否启用文件清理
         */
        private boolean enableCleanup = true;

        /**
         * 文件保留时间（天）
         */
        private int retentionDays = 30;
    }
}
