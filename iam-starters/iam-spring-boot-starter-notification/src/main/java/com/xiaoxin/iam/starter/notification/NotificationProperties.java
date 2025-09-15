package com.xiaoxin.iam.starter.notification;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 通知配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.notification")
public class NotificationProperties {

    /**
     * 是否启用通知
     */
    private boolean enabled = true;

    /**
     * 邮件配置
     */
    private Mail mail = new Mail();

    /**
     * 短信配置
     */
    private Sms sms = new Sms();

    /**
     * 微信配置
     */
    private Wechat wechat = new Wechat();

    @Data
    public static class Mail {
        /**
         * 是否启用邮件通知
         */
        private boolean enabled = true;

        /**
         * 邮件模板路径
         */
        private String templatePath = "classpath:templates/mail/";
    }

    @Data
    public static class Sms {
        /**
         * 是否启用短信通知
         */
        private boolean enabled = true;

        /**
         * 短信签名
         */
        private String signName = "IAM平台";
    }

    @Data
    public static class Wechat {
        /**
         * 是否启用微信通知
         */
        private boolean enabled = false;

        /**
         * 微信应用ID
         */
        private String appId;

        /**
         * 微信应用密钥
         */
        private String appSecret;
    }
}
