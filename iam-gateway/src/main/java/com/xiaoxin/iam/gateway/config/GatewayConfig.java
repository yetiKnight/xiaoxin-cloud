package com.xiaoxin.iam.gateway.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 网关配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class GatewayConfig {

    /**
     * 基于IP的限流Key解析器
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        log.info("配置基于IP的限流Key解析器");
        return exchange -> {
            var remoteAddress = exchange.getRequest().getRemoteAddress();
            return Mono.just(remoteAddress != null ? 
                remoteAddress.getAddress().getHostAddress() : "unknown");
        };
    }

    /**
     * 基于用户的限流Key解析器
     */
    @Bean("userKeyResolver")
    public KeyResolver userKeyResolver() {
        log.info("配置基于用户的限流Key解析器");
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
            return Mono.just(userId != null ? userId : "anonymous");
        };
    }

    /**
     * 基于API的限流Key解析器
     */
    @Bean("apiKeyResolver")
    public KeyResolver apiKeyResolver() {
        log.info("配置基于API的限流Key解析器");
        return exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            // 提取API路径，去除版本号
            String apiPath = path.replaceAll("/api/v\\d+", "");
            return Mono.just(apiPath);
        };
    }

    /**
     * Caffeine缓存管理器
     */
    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .expireAfterAccess(Duration.ofMinutes(10))
            .recordStats());
        log.info("配置Caffeine缓存管理器");
        return cacheManager;
    }

    /**
     * 自定义路由配置（代码方式，作为配置文件的补充）
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        log.info("配置自定义路由");
        return builder.routes()
            // 动态路由示例：根据请求头路由到不同版本的服务
            .route("version-routing", r -> r
                .header("X-API-Version", "v2")
                .and()
                .path("/api/v1/auth/**")
                .uri("lb://iam-auth-service-v2")
            )
            // 路径重写示例
            .route("legacy-api", r -> r
                .path("/legacy/auth/**")
                .filters(f -> f
                    .rewritePath("/legacy/auth/(?<segment>.*)", "/api/v1/auth/${segment}")
                    .addRequestHeader("X-Legacy-Request", "true")
                )
                .uri("lb://iam-auth-service")
            )
            .build();
    }
}
