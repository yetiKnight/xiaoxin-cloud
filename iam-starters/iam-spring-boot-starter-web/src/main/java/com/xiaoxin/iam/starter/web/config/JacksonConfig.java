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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Jackson配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class JacksonConfig {

    /**
     * Jackson ObjectMapper配置
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        log.info("IAM平台Jackson配置已启用");
        
        return Jackson2ObjectMapperBuilder.json()
                // 时间模块
                .modules(new JavaTimeModule())
                // 日期格式
                .simpleDateFormat("yyyy-MM-dd HH:mm:ss")
                // 时区设置
                .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                // 序列化配置
                .featuresToDisable(
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        SerializationFeature.FAIL_ON_EMPTY_BEANS
                )
                // 反序列化配置
                .featuresToDisable(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES
                )
                .build();
    }

    /**
     * 默认日期格式
     */
    @Bean
    @ConditionalOnMissingBean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
