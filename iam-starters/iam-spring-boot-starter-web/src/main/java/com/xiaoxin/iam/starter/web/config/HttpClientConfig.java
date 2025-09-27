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
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.Timeout;
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
public class HttpClientConfig {

    private final WebProperties webProperties;

    /**
     * OkHttp客户端配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(OkHttpClient.class)
    public OkHttpClient okHttpClient() {
        WebProperties.HttpClient httpClient = webProperties.getHttpClient();
        
        log.info("IAM平台OkHttp客户端配置已启用");
        
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(httpClient.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .readTimeout(httpClient.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(httpClient.getWriteTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(httpClient.isRetryOnConnectionFailure());
        
        // 连接池配置
        if (httpClient.isEnableConnectionPool()) {
            builder.connectionPool(new ConnectionPool(
                    httpClient.getMaxConnections(),
                    httpClient.getKeepAliveDuration().toMillis(),
                    TimeUnit.MILLISECONDS));
        }
        
        // 用户代理
        if (httpClient.getUserAgent() != null) {
            builder.addInterceptor(chain -> {
                okhttp3.Request originalRequest = chain.request();
                okhttp3.Request newRequest = originalRequest.newBuilder()
                        .header("User-Agent", httpClient.getUserAgent())
                        .build();
                return chain.proceed(newRequest);
            });
        }
        
        // 压缩支持
        if (httpClient.isEnableCompression()) {
            builder.addInterceptor(chain -> {
                okhttp3.Request originalRequest = chain.request();
                okhttp3.Request newRequest = originalRequest.newBuilder()
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .build();
                return chain.proceed(newRequest);
            });
        }
        
        return builder.build();
    }

    /**
     * Apache HttpClient配置
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(CloseableHttpClient.class)
    public CloseableHttpClient apacheHttpClient() {
        WebProperties.HttpClient httpClient = webProperties.getHttpClient();
        
        log.info("IAM平台Apache HttpClient配置已启用");
        
        return HttpClients.custom()
                .setConnectionManager(
                        PoolingHttpClientConnectionManagerBuilder.create()
                                .setMaxConnTotal(httpClient.getMaxConnections())
                                .setMaxConnPerRoute(httpClient.getMaxConnectionsPerHost())
                                .setDefaultSocketConfig(SocketConfig.custom()
                                        .setSoTimeout(Timeout.ofMilliseconds(httpClient.getReadTimeout().toMillis()))
                                        .setTcpNoDelay(true)
                                        .build())
                                .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                                .build())
                .setDefaultRequestConfig(
                        org.apache.hc.client5.http.config.RequestConfig.custom()
                                .setConnectionRequestTimeout(Timeout.ofMilliseconds(httpClient.getConnectTimeout().toMillis()))
                                .setResponseTimeout(Timeout.ofMilliseconds(httpClient.getReadTimeout().toMillis()))
                                .build())
                .setUserAgent(httpClient.getUserAgent())
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(
                        httpClient.getMaxRetries(),
                        Timeout.ofMilliseconds(httpClient.getRetryInterval().toMillis())))
                .build();
    }
}
