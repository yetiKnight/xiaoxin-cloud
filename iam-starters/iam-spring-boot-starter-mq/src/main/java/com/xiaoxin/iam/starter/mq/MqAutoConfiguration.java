package com.xiaoxin.iam.starter.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列相关自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class MqAutoConfiguration {

    /**
     * 消息队列配置
     */
    @Bean
    @ConditionalOnMissingBean
    public MqProperties mqProperties() {
        log.info("IAM平台消息队列配置已启用");
        return new MqProperties();
    }
}
