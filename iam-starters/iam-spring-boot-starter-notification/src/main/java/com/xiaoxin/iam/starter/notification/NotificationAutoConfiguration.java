package com.xiaoxin.iam.starter.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通知相关自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class NotificationAutoConfiguration {

    /**
     * 通知配置
     */
    @Bean
    @ConditionalOnMissingBean
    public NotificationProperties notificationProperties() {
        log.info("IAM平台通知配置已启用");
        return new NotificationProperties();
    }
}
