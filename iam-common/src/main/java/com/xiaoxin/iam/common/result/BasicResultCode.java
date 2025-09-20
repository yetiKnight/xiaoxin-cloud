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

package com.xiaoxin.iam.common.result;

/**
 * 基础响应状态码枚举
 * 只包含基础的HTTP状态码，用于通用的成功/失败状态
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public enum BasicResultCode implements IResult {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    
    /**
     * 失败
     */
    FAILED(500, "操作失败"),
    
    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),
    
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    
    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),
    
    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),
    
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 网关超时
     */
    GATEWAY_TIMEOUT(504, "网关超时"),

    /**
     * HTTP版本不支持
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP版本不支持"),

    /**
     * 变体也协商
     */
    VARIANT_ALSO_NEGOTIATES(506, "变体也协商"),

    /**
     * 存储空间不足
     */
    INSUFFICIENT_STORAGE(507, "存储空间不足"),

    /**
     * 循环检测
     */
    LOOP_DETECTED(508, "循环检测"),

    /**
     * 带宽限制
     */
    BANDWIDTH_LIMIT_EXCEEDED(509, "带宽限制"),

    /**
     * 未扩展
     */
    NOT_EXTENDED(510, "未扩展"),

    /**
     * 网络认证要求
     */
    NETWORK_AUTHENTICATION_REQUIRED(511, "网络认证要求");

    private final Integer code;
    private final String message;

    BasicResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 根据错误码获取枚举
     */
    public static BasicResultCode getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (BasicResultCode basicResultCode : values()) {
            if (basicResultCode.getCode().equals(code)) {
                return basicResultCode;
            }
        }
        return null;
    }

    /**
     * 判断是否为成功状态码
     */
    public boolean isSuccess() {
        return SUCCESS.getCode().equals(this.code);
    }

    /**
     * 判断是否为失败状态码
     */
    public boolean isFailed() {
        return !isSuccess();
    }

    /**
     * 判断是否为客户端错误（4xx）
     */
    public boolean isClientError() {
        return code >= 400 && code < 500;
    }

    /**
     * 判断是否为服务器错误（5xx）
     */
    public boolean isServerError() {
        return code >= 500 && code < 600;
    }

    /**
     * 判断是否为重定向（3xx）
     */
    public boolean isRedirect() {
        return code >= 300 && code < 400;
    }

    /**
     * 判断是否为信息响应（1xx）
     */
    public boolean isInformational() {
        return code >= 100 && code < 200;
    }

    /**
     * 判断是否为成功响应（2xx）
     */
    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

}
