package com.xiaoxin.iam.starter.web;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.xiaoxin.iam.starter.web.config.ApiDocConfig;
import com.xiaoxin.iam.starter.web.config.HttpClientConfig;
import com.xiaoxin.iam.starter.web.config.JacksonConfig;
import com.xiaoxin.iam.starter.web.config.WebProperties;
import com.xiaoxin.iam.starter.web.filter.RequestLoggingFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Web相关自动配置类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(WebProperties.class)
@EnableKnife4j
@EnableFeignClients(basePackages = "com.xiaoxin.iam")
@Import({JacksonConfig.class, ApiDocConfig.class, HttpClientConfig.class})
public class WebAutoConfiguration {

    private final WebProperties webProperties;

    /**
     * CORS配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.web.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("IAM平台CORS配置已启用");
        
        WebProperties.Cors cors = webProperties.getCors();
        
        CorsConfiguration configuration = new CorsConfiguration();
        cors.getAllowedOriginPatterns().forEach(configuration::addAllowedOriginPattern);
        cors.getAllowedHeaders().forEach(configuration::addAllowedHeader);
        cors.getAllowedMethods().forEach(configuration::addAllowedMethod);
        cors.getExposedHeaders().forEach(configuration::addExposedHeader);
        configuration.setAllowCredentials(cors.isAllowCredentials());
        configuration.setMaxAge(cors.getMaxAge().getSeconds());
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * CORS过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.web.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
    public CorsFilter corsFilter() {
        log.info("IAM平台CORS过滤器已启用");
        return new CorsFilter(corsConfigurationSource());
    }

    /**
     * 请求日志过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "iam.web.request-log", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RequestLoggingFilter requestLoggingFilter() {
        log.info("IAM平台请求日志过滤器已启用");
        return new RequestLoggingFilter(webProperties);
    }
}
