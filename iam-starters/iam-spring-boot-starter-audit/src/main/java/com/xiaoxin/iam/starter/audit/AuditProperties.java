package com.xiaoxin.iam.starter.audit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 审计配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.audit")
public class AuditProperties {

    /**
     * 是否启用审计
     */
    private boolean enabled = true;

    /**
     * 审计日志级别
     */
    private String level = "INFO";

    /**
     * 是否记录操作日志
     */
    private boolean recordOperation = true;

    /**
     * 是否记录登录日志
     */
    private boolean recordLogin = true;

    /**
     * 日志保留天数
     */
    private int retentionDays = 90;
}
