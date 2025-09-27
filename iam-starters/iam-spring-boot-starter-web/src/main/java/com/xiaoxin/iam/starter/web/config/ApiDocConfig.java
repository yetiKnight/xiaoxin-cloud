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
        
        OpenAPI openAPI = new OpenAPI()
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
                                .url(apiDoc.getLicense().getUrl())));
        
        // 添加服务器信息
        if (apiDoc.getServers() != null && !apiDoc.getServers().isEmpty()) {
            apiDoc.getServers().forEach(server -> {
                openAPI.addServersItem(new io.swagger.v3.oas.models.servers.Server()
                        .url(server.getUrl())
                        .description(server.getDescription()));
            });
        }
        
        // 添加标签信息
        if (apiDoc.getTags() != null && !apiDoc.getTags().isEmpty()) {
            apiDoc.getTags().forEach(tag -> {
                openAPI.addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
                        .name(tag.getName())
                        .description(tag.getDescription()));
            });
        }
        
        // 添加安全配置
        addSecuritySchemes(openAPI, apiDoc.getSecurity());
        
        return openAPI;
    }

    /**
     * 添加安全配置
     */
    private void addSecuritySchemes(OpenAPI openAPI, WebProperties.ApiDoc.Security security) {
        if (security.isEnableJwt()) {
            openAPI.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
            openAPI.schemaRequirement("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme(security.getJwt().getScheme())
                    .bearerFormat(security.getJwt().getBearerFormat())
                    .description(security.getJwt().getDescription()));
        }
        
        if (security.isEnableApiKey()) {
            openAPI.addSecurityItem(new SecurityRequirement().addList("apiKeyAuth"));
            openAPI.schemaRequirement("apiKeyAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(security.getApiKey().getIn().equals("header") ? 
                            SecurityScheme.In.HEADER : SecurityScheme.In.QUERY)
                    .name(security.getApiKey().getKeyName())
                    .description(security.getApiKey().getDescription()));
        }
        
        if (security.isEnableOAuth2()) {
            openAPI.addSecurityItem(new SecurityRequirement().addList("oauth2Auth"));
            openAPI.schemaRequirement("oauth2Auth", new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .description(security.getOauth2().getDescription())
                    .flows(new io.swagger.v3.oas.models.security.OAuthFlows()
                            .authorizationCode(new io.swagger.v3.oas.models.security.OAuthFlow()
                                    .authorizationUrl(security.getOauth2().getAuthorizationUrl())
                                    .tokenUrl(security.getOauth2().getTokenUrl())
                                    .scopes(new io.swagger.v3.oas.models.security.Scopes()
                                            .addString("read", "读取权限")
                                            .addString("write", "写入权限")))));
        }
    }
}
