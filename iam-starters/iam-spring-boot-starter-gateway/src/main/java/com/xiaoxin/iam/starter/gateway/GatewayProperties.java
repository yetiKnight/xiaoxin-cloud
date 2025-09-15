package com.xiaoxin.iam.starter.gateway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网关配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "iam.gateway")
public class GatewayProperties {

    /**
     * 是否启用网关
     */
    private boolean enabled = true;

    /**
     * 路由配置
     */
    private Route route = new Route();

    /**
     * 限流配置
     */
    private RateLimit rateLimit = new RateLimit();

    @Data
    public static class Route {
        /**
         * 是否启用动态路由
         */
        private boolean dynamic = true;

        /**
         * 路由配置路径
         */
        private String configPath = "classpath:gateway/routes.yml";
    }

    @Data
    public static class RateLimit {
        /**
         * 是否启用限流
         */
        private boolean enabled = true;

        /**
         * 限流规则
         */
        private String rules = "classpath:gateway/rate-limit.yml";
    }
}
