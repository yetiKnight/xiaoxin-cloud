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

package com.xiaoxin.iam.starter.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * 安全配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.security")
public class SecurityProperties {

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * OAuth2配置
     */
    private OAuth2 oauth2 = new OAuth2();

    /**
     * 密码策略配置
     */
    private Password password = new Password();

    /**
     * 验证码配置
     */
    private Captcha captcha = new Captcha();

    /**
     * 多因子认证配置
     */
    private Mfa mfa = new Mfa();

    /**
     * 访问控制配置
     */
    private AccessControl accessControl = new AccessControl();

    /**
     * 会话管理配置
     */
    private Session session = new Session();

    /**
     * JWT配置类
     */
    @Data
    public static class Jwt {
        /**
         * 是否启用JWT
         */
        private boolean enabled = true;

        /**
         * JWT密钥
         */
        private String secret = "xiaoxin-iam-jwt-secret-key-2024";

        /**
         * 访问令牌过期时间
         */
        private Duration accessTokenExpiration = Duration.ofHours(2);

        /**
         * 刷新令牌过期时间
         */
        private Duration refreshTokenExpiration = Duration.ofDays(7);

        /**
         * 签发者
         */
        private String issuer = "xiaoxin-iam";

        /**
         * 受众
         */
        private String audience = "xiaoxin-iam-client";
    }

    /**
     * OAuth2配置类
     */
    @Data
    public static class OAuth2 {
        /**
         * 是否启用OAuth2
         */
        private boolean enabled = true;

        /**
         * 授权码过期时间
         */
        private Duration authorizationCodeExpiration = Duration.ofMinutes(10);

        /**
         * 访问令牌过期时间
         */
        private Duration accessTokenExpiration = Duration.ofHours(2);

        /**
         * 刷新令牌过期时间
         */
        private Duration refreshTokenExpiration = Duration.ofDays(30);

        /**
         * 是否启用PKCE
         */
        private boolean enablePkce = true;
    }

    /**
     * 密码策略配置类
     */
    @Data
    public static class Password {
        /**
         * 最小长度
         */
        private int minLength = 8;

        /**
         * 最大长度
         */
        private int maxLength = 128;

        /**
         * 是否要求大写字母
         */
        private boolean requireUppercase = true;

        /**
         * 是否要求小写字母
         */
        private boolean requireLowercase = true;

        /**
         * 是否要求数字
         */
        private boolean requireDigits = true;

        /**
         * 是否要求特殊字符
         */
        private boolean requireSpecialChars = true;

        /**
         * 特殊字符列表
         */
        private String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        /**
         * 密码历史限制
         */
        private int historyLimit = 5;

        /**
         * 密码过期天数
         */
        private int expireDays = 90;

        /**
         * 最大失败尝试次数
         */
        private int maxFailureAttempts = 5;

        /**
         * 账户锁定时间
         */
        private Duration lockoutDuration = Duration.ofMinutes(30);
    }

    /**
     * 验证码配置类
     */
    @Data
    public static class Captcha {
        /**
         * 是否启用验证码
         */
        private boolean enabled = true;

        /**
         * 验证码类型
         */
        private CaptchaType type = CaptchaType.IMAGE;

        /**
         * 验证码长度
         */
        private int length = 4;

        /**
         * 验证码过期时间
         */
        private Duration expiration = Duration.ofMinutes(5);

        /**
         * 图片宽度
         */
        private int width = 150;

        /**
         * 图片高度
         */
        private int height = 50;

        /**
         * 字体大小
         */
        private int fontSize = 40;

        /**
         * 验证码类型枚举
         */
        public enum CaptchaType {
            IMAGE, SMS, EMAIL
        }
    }

    /**
     * 多因子认证配置类
     */
    @Data
    public static class Mfa {
        /**
         * 是否启用MFA
         */
        private boolean enabled = false;

        /**
         * TOTP配置
         */
        private Totp totp = new Totp();

        /**
         * SMS配置
         */
        private Sms sms = new Sms();

        /**
         * TOTP配置类
         */
        @Data
        public static class Totp {
            /**
             * 是否启用TOTP
             */
            private boolean enabled = true;

            /**
             * 发行者名称
             */
            private String issuer = "XiaoXin IAM";

            /**
             * 时间窗口(秒)
             */
            private int timeWindow = 30;

            /**
             * 代码位数
             */
            private int codeDigits = 6;
        }

        /**
         * SMS配置类
         */
        @Data
        public static class Sms {
            /**
             * 是否启用SMS
             */
            private boolean enabled = false;

            /**
             * 验证码长度
             */
            private int codeLength = 6;

            /**
             * 验证码过期时间
             */
            private Duration expiration = Duration.ofMinutes(5);
        }
    }

    /**
     * 访问控制配置类
     */
    @Data
    public static class AccessControl {
        /**
         * 是否启用IP白名单
         */
        private boolean enableIpWhitelist = false;

        /**
         * IP白名单
         */
        private List<String> ipWhitelist = List.of();

        /**
         * 是否启用频率限制
         */
        private boolean enableRateLimit = true;

        /**
         * 频率限制配置
         */
        private RateLimit rateLimit = new RateLimit();

        /**
         * 频率限制配置类
         */
        @Data
        public static class RateLimit {
            /**
             * 时间窗口
             */
            private Duration timeWindow = Duration.ofMinutes(1);

            /**
             * 最大请求数
             */
            private int maxRequests = 100;

            /**
             * 登录最大失败次数
             */
            private int maxLoginFailures = 5;

            /**
             * 登录失败锁定时间
             */
            private Duration loginLockoutDuration = Duration.ofMinutes(15);
        }
    }

    /**
     * 会话管理配置类
     */
    @Data
    public static class Session {
        /**
         * 会话超时时间
         */
        private Duration timeout = Duration.ofHours(8);

        /**
         * 最大并发会话数
         */
        private int maxConcurrentSessions = 1;

        /**
         * 是否阻止新会话
         */
        private boolean preventNewSession = true;

        /**
         * 是否踢出已存在会话
         */
        private boolean kickoutExistingSession = false;
    }
}
