package com.xiaoxin.iam.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Configuration
public class GatewayConfig {

    /**
     * 路由配置
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 认证服务路由
                .route("iam-auth-service", r -> r.path("/auth/**")
                        .uri("lb://iam-auth-service"))
                // 用户服务路由
                .route("iam-user-service", r -> r.path("/user/**")
                        .uri("lb://iam-user-service"))
                // 权限服务路由
                .route("iam-permission-service", r -> r.path("/permission/**")
                        .uri("lb://iam-permission-service"))
                // 组织服务路由
                .route("iam-organization-service", r -> r.path("/organization/**")
                        .uri("lb://iam-organization-service"))
                // 审计服务路由
                .route("iam-audit-service", r -> r.path("/audit/**")
                        .uri("lb://iam-audit-service"))
                // 通知服务路由
                .route("iam-notification-service", r -> r.path("/notification/**")
                        .uri("lb://iam-notification-service"))
                // 配置服务路由
                .route("iam-config-service", r -> r.path("/config/**")
                        .uri("lb://iam-config-service"))
                // 前端服务路由
                .route("iam-frontend", r -> r.path("/**")
                        .uri("lb://iam-frontend"))
                .build();
    }
}
