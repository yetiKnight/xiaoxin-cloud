package com.xiaoxin.iam.starter.security.oauth2;

import com.xiaoxin.iam.starter.security.config.OAuth2Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * OAuth2客户端自动配置类
 * 
 * <p>提供OAuth2客户端相关的自动配置：</p>
 * <ul>
 *     <li>OAuth2配置属性</li>
 *     <li>Feign客户端认证拦截器</li>
 *     <li>RestTemplate配置</li>
 * </ul>
 *
 * @author xiaoxin
 * @date 2024-09-26
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass({RestTemplate.class})
@EnableConfigurationProperties(OAuth2Properties.class)
public class OAuth2ClientAutoConfiguration {

    /**
     * OAuth2客户端专用RestTemplate
     * 
     * <p>配置超时时间，用于OAuth2令牌请求</p>
     */
    @Bean
    @ConditionalOnProperty(prefix = "oauth2.client", name = "enabled", havingValue = "true")
    public RestTemplate oauth2RestTemplate(OAuth2Properties oauth2Properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(oauth2Properties.getClient().getConnectTimeout());
        factory.setReadTimeout(oauth2Properties.getClient().getReadTimeout());
        
        RestTemplate restTemplate = new RestTemplate(factory);
        
        log.info("OAuth2客户端RestTemplate已配置，连接超时: {}ms, 读取超时: {}ms", 
                oauth2Properties.getClient().getConnectTimeout(),
                oauth2Properties.getClient().getReadTimeout());
        
        return restTemplate;
    }

    /**
     * OAuth2客户端凭据Feign拦截器
     * 
     * <p>自动为Feign调用添加OAuth2认证</p>
     */
    @Bean
    @ConditionalOnProperty(prefix = "oauth2.client", name = "enabled", havingValue = "true")
    @ConditionalOnClass(name = "feign.RequestInterceptor")
    public OAuth2ClientCredentialsInterceptor oauth2ClientCredentialsInterceptor(
            OAuth2Properties oauth2Properties,
            RestTemplate oauth2RestTemplate) {
        
        OAuth2ClientCredentialsInterceptor interceptor = new OAuth2ClientCredentialsInterceptor(
                oauth2Properties.getClient(), 
                oauth2RestTemplate
        );
        
        log.info("OAuth2客户端凭据拦截器已启用，客户端ID: {}, 令牌端点: {}", 
                oauth2Properties.getClient().getClientId(),
                oauth2Properties.getClient().getTokenUri());
        
        return interceptor;
    }

    /**
     * Feign配置类
     * 
     * <p>注册OAuth2拦截器到Feign客户端</p>
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "feign.RequestInterceptor")
    @ConditionalOnProperty(prefix = "oauth2.client", name = "enabled", havingValue = "true")
    public static class FeignOAuth2Configuration {

        /**
         * 注册OAuth2拦截器为全局Feign拦截器
         */
        @Bean
        public feign.RequestInterceptor oauth2RequestInterceptor(
                OAuth2ClientCredentialsInterceptor oauth2ClientCredentialsInterceptor) {
            
            log.info("OAuth2 Feign拦截器已注册为全局拦截器");
            return oauth2ClientCredentialsInterceptor;
        }
    }
}
