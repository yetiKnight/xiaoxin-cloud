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

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 结果工具类
 * 提供Result相关的工具方法
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class ResultUtils {

    private ResultUtils() {
        // 工具类不允许实例化
    }

    /**
     * 安全执行操作并返回结果
     */
    public static <T> Result<T> safeExecute(Supplier<T> operation) {
        return safeExecute(operation, "操作执行失败");
    }

    /**
     * 安全执行操作并返回结果（带错误消息）
     */
    public static <T> Result<T> safeExecute(Supplier<T> operation, String errorMessage) {
        try {
            T result = operation.get();
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed(errorMessage + ": " + e.getMessage());
        }
    }

    /**
     * 安全执行操作并返回结果（带错误处理）
     */
    public static <T> Result<T> safeExecute(Supplier<T> operation, String errorMessage, 
                                           Function<Exception, String> errorHandler) {
        try {
            T result = operation.get();
            return Result.success(result);
        } catch (Exception e) {
            String message = errorHandler != null ? errorHandler.apply(e) : errorMessage + ": " + e.getMessage();
            return Result.failed(message);
        }
    }

    /**
     * 安全执行操作并返回结果（带结果枚举）
     */
    public static <T> Result<T> safeExecute(Supplier<T> operation, IResult successResult, IResult failureResult) {
        try {
            T result = operation.get();
            return Result.success(successResult.getMessage(), result);
        } catch (Exception e) {
            return Result.failed(failureResult.getCode(), failureResult.getMessage() + ": " + e.getMessage());
        }
    }

    /**
     * 根据条件返回结果
     */
    public static <T> Result<T> conditional(boolean condition, T successData, String failureMessage) {
        return condition ? Result.success(successData) : Result.failed(failureMessage);
    }

    /**
     * 根据条件返回结果（带成功消息）
     */
    public static <T> Result<T> conditional(boolean condition, String successMessage, T successData, String failureMessage) {
        return condition ? Result.success(successMessage, successData) : Result.failed(failureMessage);
    }

    /**
     * 根据条件返回结果（带结果枚举）
     */
    public static <T> Result<T> conditional(boolean condition, IResult successResult, IResult failureResult, T data) {
        return condition ? Result.success(successResult.getMessage(), data) : Result.failed(failureResult);
    }

    /**
     * 根据数据是否为空返回结果
     */
    public static <T> Result<T> ifNotNull(T data, String successMessage, String failureMessage) {
        return data != null ? Result.success(successMessage, data) : Result.failed(failureMessage);
    }

    /**
     * 根据数据是否为空返回结果（带默认数据）
     */
    public static <T> Result<T> ifNotNull(T data, String successMessage, String failureMessage, T defaultData) {
        if (data != null) {
            return Result.success(successMessage, data);
        } else {
            return Result.failed(failureMessage);
        }
    }

    /**
     * 根据字符串是否为空返回结果
     */
    public static <T> Result<T> ifNotBlank(String str, String successMessage, String failureMessage, T data) {
        return (str != null && !str.trim().isEmpty()) ? Result.success(successMessage, data) : Result.failed(failureMessage);
    }

    /**
     * 根据集合是否为空返回结果
     */
    public static <T> Result<T> ifNotEmpty(Collection<?> collection, String successMessage, String failureMessage, T data) {
        return (collection != null && !collection.isEmpty()) ? Result.success(successMessage, data) : Result.failed(failureMessage);
    }

    /**
     * 根据谓词条件返回结果
     */
    public static <T> Result<T> ifTrue(Predicate<T> predicate, T data, String successMessage, String failureMessage) {
        return predicate.test(data) ? Result.success(successMessage, data) : Result.failed(failureMessage);
    }

    /**
     * 批量处理结果
     */
    public static <T> Result<List<T>> batchProcess(List<Supplier<T>> operations) {
        return batchProcess(operations, "批量处理失败");
    }

    /**
     * 批量处理结果（带错误消息）
     */
    public static <T> Result<List<T>> batchProcess(List<Supplier<T>> operations, String errorMessage) {
        try {
            List<T> results = operations.stream()
                    .map(Supplier::get)
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(results);
        } catch (Exception e) {
            return Result.failed(errorMessage + ": " + e.getMessage());
        }
    }

    /**
     * 批量处理结果（带错误处理）
     */
    public static <T> Result<List<T>> batchProcess(List<Supplier<T>> operations, String errorMessage, 
                                                  Function<Exception, String> errorHandler) {
        try {
            List<T> results = operations.stream()
                    .map(Supplier::get)
                    .collect(java.util.stream.Collectors.toList());
            return Result.success(results);
        } catch (Exception e) {
            String message = errorHandler != null ? errorHandler.apply(e) : errorMessage + ": " + e.getMessage();
            return Result.failed(message);
        }
    }

    /**
     * 转换结果数据
     */
    public static <T, R> Result<R> map(Result<T> result, Function<T, R> mapper) {
        if (result.isSuccess() && result.hasData()) {
            try {
                R mappedData = mapper.apply(result.getData());
                return Result.success(result.getMessage(), mappedData);
            } catch (Exception e) {
                return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "数据转换失败: " + e.getMessage());
            }
        }
        return Result.failed(result.getCode(), result.getMessage());
    }

    /**
     * 扁平化转换结果数据
     */
    public static <T, R> Result<R> flatMap(Result<T> result, Function<T, Result<R>> mapper) {
        if (result.isSuccess() && result.hasData()) {
            try {
                return mapper.apply(result.getData());
            } catch (Exception e) {
                return Result.failed(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "数据转换失败: " + e.getMessage());
            }
        }
        return Result.failed(result.getCode(), result.getMessage());
    }

    /**
     * 过滤结果数据
     */
    public static <T> Result<T> filter(Result<T> result, Predicate<T> predicate, String failureMessage) {
        if (result.isSuccess() && result.hasData() && predicate.test(result.getData())) {
            return result;
        } else if (result.isSuccess() && result.hasData()) {
            return Result.failed(failureMessage);
        } else {
            return result;
        }
    }

    /**
     * 合并多个结果
     */
    @SafeVarargs
    public static <T> Result<List<T>> merge(Result<T>... results) {
        List<T> data = java.util.Arrays.stream(results)
                .filter(Result::isSuccess)
                .filter(Result::hasData)
                .map(Result::getData)
                .collect(java.util.stream.Collectors.toList());
        
        if (data.isEmpty()) {
            return Result.failed("所有操作都失败了");
        }
        
        return Result.success(data);
    }

    /**
     * 合并多个结果（带错误处理）
     */
    @SafeVarargs
    public static <T> Result<List<T>> merge(String successMessage, String failureMessage, Result<T>... results) {
        List<T> data = java.util.Arrays.stream(results)
                .filter(Result::isSuccess)
                .filter(Result::hasData)
                .map(Result::getData)
                .collect(java.util.stream.Collectors.toList());
        
        if (data.isEmpty()) {
            return Result.failed(failureMessage);
        }
        
        return Result.success(successMessage, data);
    }

    /**
     * 获取第一个成功的结果
     */
    @SafeVarargs
    public static <T> Result<T> firstSuccess(Result<T>... results) {
        for (Result<T> result : results) {
            if (result.isSuccess()) {
                return result;
            }
        }
        return Result.failed("所有操作都失败了");
    }

    /**
     * 获取第一个成功的结果（带默认结果）
     */
    @SafeVarargs
    public static <T> Result<T> firstSuccess(Result<T> defaultResult, Result<T>... results) {
        for (Result<T> result : results) {
            if (result.isSuccess()) {
                return result;
            }
        }
        return defaultResult;
    }

    /**
     * 检查结果是否成功
     */
    public static boolean isSuccess(Result<?> result) {
        return result != null && result.isSuccess();
    }

    /**
     * 检查结果是否失败
     */
    public static boolean isFailed(Result<?> result) {
        return result == null || result.isFailed();
    }

    /**
     * 检查结果是否有数据
     */
    public static boolean hasData(Result<?> result) {
        return result != null && result.hasData();
    }

    /**
     * 获取结果数据或默认值
     */
    public static <T> T getDataOrDefault(Result<T> result, T defaultValue) {
        return result != null ? result.getDataOrDefault(defaultValue) : defaultValue;
    }

    /**
     * 获取结果消息或默认值
     */
    public static String getMessageOrDefault(Result<?> result, String defaultMessage) {
        return result != null && result.getMessage() != null ? result.getMessage() : defaultMessage;
    }

    /**
     * 获取结果错误码或默认值
     */
    public static Integer getCodeOrDefault(Result<?> result, Integer defaultCode) {
        return result != null && result.getCode() != null ? result.getCode() : defaultCode;
    }

    /**
     * 创建成功结果
     */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 创建成功结果（带消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return Result.success(message, data);
    }

    /**
     * 创建失败结果
     */
    public static <T> Result<T> failed(String message) {
        return Result.failed(message);
    }

    /**
     * 创建失败结果（带错误码）
     */
    public static <T> Result<T> failed(Integer code, String message) {
        return Result.failed(code, message);
    }

    /**
     * 创建失败结果（带结果枚举）
     */
    public static <T> Result<T> failed(IResult result) {
        return Result.failed(result);
    }

}
