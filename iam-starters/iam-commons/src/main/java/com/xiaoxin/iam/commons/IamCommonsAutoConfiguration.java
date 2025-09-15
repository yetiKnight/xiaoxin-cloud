package com.xiaoxin.iam.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IAM平台公共组件自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class IamCommonsAutoConfiguration {

    /**
     * IAM平台公共组件配置
     */
    @Bean
    @ConditionalOnMissingBean
    public IamCommonsProperties iamCommonsProperties() {
        log.info("IAM平台公共组件配置已启用");
        return new IamCommonsProperties();
    }
}
