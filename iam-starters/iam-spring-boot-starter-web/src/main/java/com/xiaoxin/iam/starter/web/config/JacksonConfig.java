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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Jackson配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnProperty(prefix = "iam.web.jackson", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JacksonConfig {

    private final WebProperties webProperties;

    /**
     * Jackson ObjectMapper配置
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        log.info("IAM平台Jackson配置已启用");
        
        WebProperties.Jackson jackson = webProperties.getJackson();
        
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json()
                // 时间模块
                .modules(new JavaTimeModule())
                // 日期格式
                .simpleDateFormat(jackson.getDateFormat())
                // 时区设置
                .timeZone(TimeZone.getTimeZone(jackson.getTimeZone()));
        
        // 序列化配置
        if (!jackson.isWriteDatesAsTimestamps()) {
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        }
        
        if (!jackson.isFailOnEmptyBeans()) {
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        }
        
        if (jackson.isIndentOutput()) {
            builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        }
        
        if (!jackson.isIncludeNulls()) {
            // WRITE_NULL_MAP_VALUES 已废弃，使用 JsonInclude.Include.NON_NULL 替代
            builder.serializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        }
        
        // 反序列化配置
        if (!jackson.isFailOnUnknownProperties()) {
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
        
        if (!jackson.getDeserialization().isFailOnNullForPrimitives()) {
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        }
        
        if (!jackson.getDeserialization().isFailOnInvalidSubtype()) {
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        }
        
        // FAIL_ON_EMPTY_BEANS 在 DeserializationFeature 中不存在，已移除
        
        // 属性命名策略
        if (jackson.isEnablePropertyNamingStrategy()) {
            switch (jackson.getPropertyNamingStrategy().toUpperCase()) {
                case "SNAKE_CASE":
                    builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
                    break;
                case "LOWER_CASE":
                    builder.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CASE);
                    break;
                case "UPPER_CAMEL_CASE":
                    builder.propertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
                    break;
                case "KEBAB_CASE":
                    builder.propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
                    break;
                default:
                    builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
                    break;
            }
        }
        
        // 循环引用检测
        if (jackson.isEnableCircularReferenceDetection()) {
            builder.featuresToEnable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL);
        }
        
        return builder.build();
    }

    /**
     * 默认日期格式
     */
    @Bean
    @ConditionalOnMissingBean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat(webProperties.getJackson().getDateFormat());
    }
}
