package com.xiaoxin.iam.starter.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 审计相关自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class AuditAutoConfiguration {

    /**
     * 审计配置
     */
    @Bean
    @ConditionalOnMissingBean
    public AuditProperties auditProperties() {
        log.info("IAM平台审计配置已启用");
        return new AuditProperties();
    }
}
