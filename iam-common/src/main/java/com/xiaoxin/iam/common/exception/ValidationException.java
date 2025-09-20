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

import com.xiaoxin.iam.common.result.ResultCode;

import java.util.List;
import java.util.Map;

/**
 * 参数验证异常
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class ValidationException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 验证错误字段
     */
    private List<String> fields;

    /**
     * 验证错误详情
     */
    private Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(ResultCode.PARAM_ERROR.getCode(), message);
    }

    public ValidationException(Integer code, String message) {
        super(code, message);
    }

    public ValidationException(String message, String detail) {
        super(ResultCode.PARAM_ERROR.getCode(), message, detail);
    }

    public ValidationException(Integer code, String message, String detail) {
        super(code, message, detail);
    }

    public ValidationException(String message, List<String> fields) {
        super(ResultCode.PARAM_ERROR.getCode(), message);
        this.fields = fields;
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(ResultCode.PARAM_ERROR.getCode(), message);
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(String message, String detail, List<String> fields) {
        super(ResultCode.PARAM_ERROR.getCode(), message, detail);
        this.fields = fields;
    }

    public ValidationException(String message, String detail, Map<String, String> fieldErrors) {
        super(ResultCode.PARAM_ERROR.getCode(), message, detail);
        this.fieldErrors = fieldErrors;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    /**
     * 参数错误异常
     */
    public static ValidationException paramError() {
        return new ValidationException(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMessage());
    }

    /**
     * 参数错误异常（带详情）
     */
    public static ValidationException paramError(String detail) {
        return new ValidationException(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMessage(), detail);
    }

    /**
     * 参数错误异常（带字段）
     */
    public static ValidationException paramError(String message, List<String> fields) {
        return new ValidationException(message, fields);
    }

    /**
     * 参数错误异常（带字段错误）
     */
    public static ValidationException paramError(String message, Map<String, String> fieldErrors) {
        return new ValidationException(message, fieldErrors);
    }

    /**
     * 必填参数缺失异常
     */
    public static ValidationException requiredParamMissing(String paramName) {
        return new ValidationException(ResultCode.PARAM_ERROR.getCode(), "必填参数缺失: " + paramName);
    }

    /**
     * 参数格式错误异常
     */
    public static ValidationException paramFormatError(String paramName, String expectedFormat) {
        return new ValidationException(ResultCode.PARAM_ERROR.getCode(), "参数格式错误: " + paramName + ", 期望格式: " + expectedFormat);
    }

    /**
     * 参数值超出范围异常
     */
    public static ValidationException paramOutOfRange(String paramName, String range) {
        return new ValidationException(ResultCode.PARAM_ERROR.getCode(), "参数值超出范围: " + paramName + ", 有效范围: " + range);
    }

    /**
     * 参数长度超出限制异常
     */
    public static ValidationException paramLengthExceeded(String paramName, int maxLength) {
        return new ValidationException(ResultCode.PARAM_ERROR.getCode(), "参数长度超出限制: " + paramName + ", 最大长度: " + maxLength);
    }
}
