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

package com.xiaoxin.iam.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * IAM平台配置属性
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "iam")
public class IamProperties {

    /**
     * 是否启用IAM平台功能
     */
    private boolean enabled = true;

    /**
     * 应用名称
     */
    private String applicationName = "iam-platform";

    /**
     * 版本信息
     */
    private String version = "1.0.0";

    /**
     * 是否启用统一响应格式
     */
    private boolean enableUnifiedResponse = true;

    /**
     * 是否启用全局异常处理
     */
    private boolean enableGlobalExceptionHandler = true;

    /**
     * 是否启用审计日志
     */
    private boolean enableAuditLog = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnableUnifiedResponse() {
        return enableUnifiedResponse;
    }

    public void setEnableUnifiedResponse(boolean enableUnifiedResponse) {
        this.enableUnifiedResponse = enableUnifiedResponse;
    }

    public boolean isEnableGlobalExceptionHandler() {
        return enableGlobalExceptionHandler;
    }

    public void setEnableGlobalExceptionHandler(boolean enableGlobalExceptionHandler) {
        this.enableGlobalExceptionHandler = enableGlobalExceptionHandler;
    }

    public boolean isEnableAuditLog() {
        return enableAuditLog;
    }

    public void setEnableAuditLog(boolean enableAuditLog) {
        this.enableAuditLog = enableAuditLog;
    }

}
