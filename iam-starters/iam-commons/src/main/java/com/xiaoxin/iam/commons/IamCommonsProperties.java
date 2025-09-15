package com.xiaoxin.iam.commons;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * IAM平台公共组件配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.commons")
public class IamCommonsProperties {

    /**
     * 是否启用公共组件
     */
    private boolean enabled = true;

    /**
     * 应用名称
     */
    private String applicationName = "iam-platform";

    /**
     * 应用版本
     */
    private String version = "1.0.0";

    /**
     * 环境配置
     */
    private String environment = "dev";

    /**
     * 是否启用调试模式
     */
    private boolean debug = false;

    /**
     * 是否启用性能监控
     */
    private boolean performanceMonitoring = true;

    /**
     * 是否启用健康检查
     */
    private boolean healthCheck = true;
}
