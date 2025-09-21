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

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * API文档配置
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(WebProperties.class)
@ConditionalOnProperty(prefix = "iam.web.api-doc", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ApiDocConfig {

    private final WebProperties webProperties;

    /**
     * OpenAPI配置
     */
    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPI() {
        WebProperties.ApiDoc apiDoc = webProperties.getApiDoc();
        
        log.info("IAM平台API文档配置已启用: {}", apiDoc.getTitle());
        
        return new OpenAPI()
                .info(new Info()
                        .title(apiDoc.getTitle())
                        .description(apiDoc.getDescription())
                        .version(apiDoc.getVersion())
                        .contact(new Contact()
                                .name(apiDoc.getContact().getName())
                                .email(apiDoc.getContact().getEmail())
                                .url(apiDoc.getContact().getUrl()))
                        .license(new License()
                                .name(apiDoc.getLicense().getName())
                                .url(apiDoc.getLicense().getUrl())))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .schemaRequirement("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT认证令牌"));
    }
}
