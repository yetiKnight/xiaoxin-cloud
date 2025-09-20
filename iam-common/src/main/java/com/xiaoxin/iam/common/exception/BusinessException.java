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

package com.xiaoxin.iam.common.exception;

import com.xiaoxin.iam.common.result.IResult;
import com.xiaoxin.iam.common.result.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 业务异常
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误详情
     */
    private String detail;

    /**
     * 扩展数据
     */
    private Map<String, Object> data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public BusinessException() {
        super();
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(IResult result) {
        super(result.getMessage());
        this.code = result.getCode();
        this.message = result.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(IResult result, Throwable cause) {
        super(result.getMessage(), cause);
        this.code = result.getCode();
        this.message = result.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(String message, String detail) {
        super(message);
        this.message = message;
        this.detail = detail;
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(Integer code, String message, String detail) {
        super(message);
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.timestamp = System.currentTimeMillis();
    }

    public BusinessException(Integer code, String message, String detail, Map<String, Object> data) {
        super(message);
        this.code = code;
        this.message = message;
        this.detail = detail;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 创建业务异常
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException of(Integer code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException of(IResult result) {
        return new BusinessException(result);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException of(String message, String detail) {
        return new BusinessException(message, detail);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException of(Integer code, String message, String detail) {
        return new BusinessException(code, message, detail);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException of(Integer code, String message, String detail, Map<String, Object> data) {
        return new BusinessException(code, message, detail, data);
    }

    /**
     * 检查是否为业务异常
     */
    public static boolean isBusinessException(Throwable throwable) {
        return throwable instanceof BusinessException;
    }

    /**
     * 获取异常信息
     */
    public String getFullMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(code).append("] ").append(message);
        if (detail != null && !detail.isEmpty()) {
            sb.append(" - ").append(detail);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFullMessage();
    }
}