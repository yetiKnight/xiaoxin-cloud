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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一响应结果
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private LocalDateTime timestamp;

    public Result() {
        this.timestamp = LocalDateTime.now();
    }

    public Result(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<>(BasicResultCode.SUCCESS.getCode(), BasicResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(BasicResultCode.SUCCESS.getCode(), BasicResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功响应（带消息和数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(BasicResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> failed() {
        return new Result<>(BasicResultCode.FAILED.getCode(), BasicResultCode.FAILED.getMessage(), null);
    }

    /**
     * 失败响应（带消息）
     */
    public static <T> Result<T> failed(String message) {
        return new Result<>(BasicResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败响应（带错误码和消息）
     */
    public static <T> Result<T> failed(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败响应（带结果枚举）
     */
    public static <T> Result<T> failed(IResult result) {
        return new Result<>(result.getCode(), result.getMessage(), null);
    }

    /**
     * 失败响应（带结果枚举和数据）
     */
    public static <T> Result<T> failed(IResult result, T data) {
        return new Result<>(result.getCode(), result.getMessage(), data);
    }

    /**
     * 失败响应（带错误码、消息和数据）
     */
    public static <T> Result<T> failed(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /**
     * 根据条件返回成功或失败结果
     */
    public static <T> Result<T> of(boolean success, String message) {
        return success ? success(message, null) : failed(message);
    }

    /**
     * 根据条件返回成功或失败结果（带数据）
     */
    public static <T> Result<T> of(boolean success, String message, T data) {
        return success ? success(message, data) : failed(message);
    }

    /**
     * 根据条件返回成功或失败结果（带错误码）
     */
    public static <T> Result<T> of(boolean success, Integer code, String message) {
        return success ? success(message, null) : failed(code, message);
    }

    /**
     * 根据条件返回成功或失败结果（带错误码和数据）
     */
    public static <T> Result<T> of(boolean success, Integer code, String message, T data) {
        return success ? success(message, data) : failed(code, message, data);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return BasicResultCode.SUCCESS.getCode().equals(this.code);
    }

    /**
     * 判断是否失败
     */
    public boolean isFailed() {
        return !isSuccess();
    }

    /**
     * 判断是否有数据
     */
    public boolean hasData() {
        return data != null;
    }

    /**
     * 获取数据，如果为空则返回默认值
     */
    public T getDataOrDefault(T defaultValue) {
        return data != null ? data : defaultValue;
    }

    /**
     * 如果成功则执行操作
     */
    public Result<T> ifSuccess(java.util.function.Consumer<T> action) {
        if (isSuccess() && data != null) {
            action.accept(data);
        }
        return this;
    }

    /**
     * 如果失败则执行操作
     */
    public Result<T> ifFailed(java.util.function.Consumer<Result<T>> action) {
        if (isFailed()) {
            action.accept(this);
        }
        return this;
    }

    /**
     * 转换数据
     */
    public <R> Result<R> map(java.util.function.Function<T, R> mapper) {
        if (isSuccess() && data != null) {
            try {
                R newData = mapper.apply(data);
                return success(message, newData);
            } catch (Exception e) {
                return failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "数据转换失败: " + e.getMessage());
            }
        }
        return failed(code, message);
    }

    /**
     * 扁平化转换
     */
    public <R> Result<R> flatMap(java.util.function.Function<T, Result<R>> mapper) {
        if (isSuccess() && data != null) {
            try {
                return mapper.apply(data);
            } catch (Exception e) {
                return failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "数据转换失败: " + e.getMessage());
            }
        }
        return failed(code, message);
    }

    // Getter and Setter methods
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

}