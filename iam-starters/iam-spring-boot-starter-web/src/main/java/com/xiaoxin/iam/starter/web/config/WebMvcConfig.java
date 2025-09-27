/*
 * Copyright 2024 xiaoxin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoxin.iam.starter.web.config;

import com.xiaoxin.iam.starter.web.interceptor.RequestIdInterceptor;
import com.xiaoxin.iam.starter.web.interceptor.SecurityInterceptor;
import com.xiaoxin.iam.starter.web.interceptor.TimingInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnProperty(prefix = "iam.web", name = "enabled", havingValue = "true", matchIfMissing = true)
public class WebMvcConfig implements WebMvcConfigurer {

    private final WebProperties webProperties;

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("IAM平台WebMvc拦截器配置已启用");
        
        // 请求ID拦截器
        if (webProperties.getRequestLog().isEnableRequestId()) {
            registry.addInterceptor(new RequestIdInterceptor(webProperties))
                    .addPathPatterns("/**")
                    .excludePathPatterns(webProperties.getRequestLog().getExcludePathPatterns());
        }
        
        // 安全拦截器
        registry.addInterceptor(new SecurityInterceptor(webProperties))
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**", "/static/**", "/css/**", "/js/**", "/images/**");
        
        // 性能监控拦截器
        if (webProperties.getRequestLog().isLogPerformanceMetrics()) {
            registry.addInterceptor(new TimingInterceptor(webProperties))
                    .addPathPatterns("/**")
                    .excludePathPatterns(webProperties.getRequestLog().getExcludePathPatterns());
        }
    }

    /**
     * 配置静态资源处理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("IAM平台静态资源处理配置已启用");
        
        // Swagger UI资源
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
        
        // Knife4j资源
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        
        // 静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
