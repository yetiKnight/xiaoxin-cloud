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

import java.util.function.Supplier;

/**
 * 结果构建器
 * 提供链式调用的方式构建Result对象
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class ResultBuilder<T> {

    private Integer code;
    private String message;
    private T data;
    private boolean isSuccess = true;

    private ResultBuilder() {
    }

    /**
     * 创建构建器
     */
    public static <T> ResultBuilder<T> builder() {
        return new ResultBuilder<>();
    }

    /**
     * 创建成功构建器
     */
    public static <T> ResultBuilder<T> success() {
        return new ResultBuilder<T>().setSuccess();
    }

    /**
     * 创建失败构建器
     */
    public static <T> ResultBuilder<T> failed() {
        return new ResultBuilder<T>().setFailed();
    }

    /**
     * 设置成功状态
     */
    public ResultBuilder<T> setSuccess() {
        this.isSuccess = true;
        this.code = BasicResultCode.SUCCESS.getCode();
        this.message = BasicResultCode.SUCCESS.getMessage();
        return this;
    }

    /**
     * 设置失败状态
     */
    public ResultBuilder<T> setFailed() {
        this.isSuccess = false;
        this.code = BasicResultCode.FAILED.getCode();
        this.message = BasicResultCode.FAILED.getMessage();
        return this;
    }

    /**
     * 设置错误码
     */
    public ResultBuilder<T> code(Integer code) {
        this.code = code;
        return this;
    }

    /**
     * 设置消息
     */
    public ResultBuilder<T> message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置数据
     */
    public ResultBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置结果枚举
     */
    public ResultBuilder<T> result(IResult result) {
        this.code = result.getCode();
        this.message = result.getMessage();
        this.isSuccess = BasicResultCode.SUCCESS.getCode().equals(result.getCode());
        return this;
    }

    /**
     * 根据条件设置成功或失败
     */
    public ResultBuilder<T> condition(boolean condition, String successMessage, String failureMessage) {
        if (condition) {
            return setSuccess().message(successMessage);
        } else {
            return setFailed().message(failureMessage);
        }
    }

    /**
     * 根据条件设置成功或失败（带数据）
     */
    public ResultBuilder<T> condition(boolean condition, String successMessage, String failureMessage, T data) {
        if (condition) {
            return setSuccess().message(successMessage).data(data);
        } else {
            return setFailed().message(failureMessage);
        }
    }

    /**
     * 根据条件设置成功或失败（带结果枚举）
     */
    public ResultBuilder<T> condition(boolean condition, IResult successResult, IResult failureResult) {
        if (condition) {
            return result(successResult);
        } else {
            return result(failureResult);
        }
    }

    /**
     * 根据条件设置成功或失败（带结果枚举和数据）
     */
    public ResultBuilder<T> condition(boolean condition, IResult successResult, IResult failureResult, T data) {
        if (condition) {
            return result(successResult).data(data);
        } else {
            return result(failureResult);
        }
    }

    /**
     * 安全执行操作并设置结果
     */
    public ResultBuilder<T> safeExecute(Supplier<T> operation, String errorMessage) {
        try {
            T result = operation.get();
            return setSuccess().data(result);
        } catch (Exception e) {
            return setFailed().message(errorMessage + ": " + e.getMessage());
        }
    }

    /**
     * 安全执行操作并设置结果（带自定义错误处理）
     */
    public ResultBuilder<T> safeExecute(Supplier<T> operation, String errorMessage, 
                                       java.util.function.Function<Exception, String> errorHandler) {
        try {
            T result = operation.get();
            return setSuccess().data(result);
        } catch (Exception e) {
            String message = errorHandler != null ? errorHandler.apply(e) : errorMessage + ": " + e.getMessage();
            return setFailed().message(message);
        }
    }

    /**
     * 如果条件为真则设置成功，否则设置失败
     */
    public ResultBuilder<T> ifTrue(boolean condition, String successMessage, String failureMessage) {
        return condition(condition, successMessage, failureMessage);
    }

    /**
     * 如果条件为真则设置成功，否则设置失败（带数据）
     */
    public ResultBuilder<T> ifTrue(boolean condition, String successMessage, String failureMessage, T data) {
        return condition(condition, successMessage, failureMessage, data);
    }

    /**
     * 如果数据不为空则设置成功，否则设置失败
     */
    public ResultBuilder<T> ifNotNull(T data, String successMessage, String failureMessage) {
        return condition(data != null, successMessage, failureMessage, data);
    }

    /**
     * 如果数据不为空则设置成功，否则设置失败（带默认数据）
     */
    public ResultBuilder<T> ifNotNull(T data, String successMessage, String failureMessage, T defaultData) {
        if (data != null) {
            return setSuccess().message(successMessage).data(data);
        } else {
            return setFailed().message(failureMessage).data(defaultData);
        }
    }

    /**
     * 如果字符串不为空则设置成功，否则设置失败
     */
    public ResultBuilder<T> ifNotBlank(String str, String successMessage, String failureMessage) {
        return condition(str != null && !str.trim().isEmpty(), successMessage, failureMessage);
    }

    /**
     * 如果字符串不为空则设置成功，否则设置失败（带数据）
     */
    public ResultBuilder<T> ifNotBlank(String str, String successMessage, String failureMessage, T data) {
        return condition(str != null && !str.trim().isEmpty(), successMessage, failureMessage, data);
    }

    /**
     * 如果集合不为空则设置成功，否则设置失败
     */
    public ResultBuilder<T> ifNotEmpty(java.util.Collection<?> collection, String successMessage, String failureMessage) {
        return condition(collection != null && !collection.isEmpty(), successMessage, failureMessage);
    }

    /**
     * 如果集合不为空则设置成功，否则设置失败（带数据）
     */
    public ResultBuilder<T> ifNotEmpty(java.util.Collection<?> collection, String successMessage, String failureMessage, T data) {
        return condition(collection != null && !collection.isEmpty(), successMessage, failureMessage, data);
    }

    /**
     * 构建Result对象
     */
    public Result<T> build() {
        return new Result<>(code, message, data);
    }

    /**
     * 构建Result对象（带默认值）
     */
    public Result<T> build(T defaultData) {
        if (data == null) {
            data = defaultData;
        }
        return new Result<>(code, message, data);
    }

    /**
     * 构建Result对象（带数据转换）
     */
    public <R> Result<R> build(java.util.function.Function<T, R> mapper) {
        R mappedData = data != null ? mapper.apply(data) : null;
        return new Result<>(code, message, mappedData);
    }

    /**
     * 构建Result对象（带数据转换和默认值）
     */
    public <R> Result<R> build(java.util.function.Function<T, R> mapper, R defaultData) {
        R mappedData = data != null ? mapper.apply(data) : defaultData;
        return new Result<>(code, message, mappedData);
    }

    /**
     * 获取当前状态
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * 获取当前错误码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取当前消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 获取当前数据
     */
    public T getData() {
        return data;
    }
}
