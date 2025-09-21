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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * HTTP客户端配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnClass(OkHttpClient.class)
public class HttpClientConfig {

    private final WebProperties webProperties;

    /**
     * OkHttp客户端配置
     */
    @Bean
    @ConditionalOnMissingBean
    public OkHttpClient okHttpClient() {
        WebProperties.HttpClient httpClient = webProperties.getHttpClient();
        
        log.info("IAM平台OkHttp客户端配置已启用");
        
        return new OkHttpClient.Builder()
                .connectTimeout(httpClient.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(httpClient.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(httpClient.getWriteTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(
                        httpClient.getMaxConnections(),
                        httpClient.getKeepAliveDuration().toMillis(),
                        TimeUnit.MILLISECONDS))
                .retryOnConnectionFailure(true)
                .build();
    }
}
