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
     * CORS配置类
     */
    @Data
    public static class Cors {
        /**
         * 是否启用CORS
         */
        private boolean enabled = true;

        /**
         * 允许的源
         */
        private List<String> allowedOriginPatterns = List.of("*");

        /**
         * 允许的方法
         */
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");

        /**
         * 允许的头
         */
        private List<String> allowedHeaders = List.of("*");

        /**
         * 暴露的头
         */
        private List<String> exposedHeaders = List.of("Content-Disposition");

        /**
         * 是否允许凭证
         */
        private boolean allowCredentials = true;

        /**
         * 预检请求缓存时间
         */
        private Duration maxAge = Duration.ofHours(1);
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
    }
}
