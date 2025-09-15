package com.xiaoxin.iam.starter.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关相关自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class GatewayAutoConfiguration {

    /**
     * 网关配置
     */
    @Bean
    @ConditionalOnMissingBean
    public GatewayProperties gatewayProperties() {
        log.info("IAM平台网关配置已启用");
        return new GatewayProperties();
    }
}
