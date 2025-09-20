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

/**
 * 数据异常
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class DataException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public DataException(String message) {
        super(ResultCode.DATABASE_ERROR.getCode(), message);
    }

    public DataException(Integer code, String message) {
        super(code, message);
    }

    public DataException(String message, String detail) {
        super(ResultCode.DATABASE_ERROR.getCode(), message, detail);
    }

    public DataException(Integer code, String message, String detail) {
        super(code, message, detail);
    }

    public DataException(String message, Throwable cause) {
        super(ResultCode.DATABASE_ERROR.getCode(), message, cause);
    }

    /**
     * 数据库操作失败异常
     */
    public static DataException databaseError() {
        return new DataException(ResultCode.DATABASE_ERROR.getCode(), ResultCode.DATABASE_ERROR.getMessage());
    }

    /**
     * 数据库操作失败异常（带详情）
     */
    public static DataException databaseError(String detail) {
        return new DataException(ResultCode.DATABASE_ERROR.getCode(), ResultCode.DATABASE_ERROR.getMessage(), detail);
    }

    /**
     * 数据不存在异常
     */
    public static DataException dataNotFound() {
        return new DataException(ResultCode.DATA_NOT_FOUND.getCode(), ResultCode.DATA_NOT_FOUND.getMessage());
    }

    /**
     * 数据不存在异常（带详情）
     */
    public static DataException dataNotFound(String detail) {
        return new DataException(ResultCode.DATA_NOT_FOUND.getCode(), ResultCode.DATA_NOT_FOUND.getMessage(), detail);
    }

    /**
     * 数据已存在异常
     */
    public static DataException dataAlreadyExists() {
        return new DataException(ResultCode.DATA_ALREADY_EXISTS.getCode(), ResultCode.DATA_ALREADY_EXISTS.getMessage());
    }

    /**
     * 数据已存在异常（带详情）
     */
    public static DataException dataAlreadyExists(String detail) {
        return new DataException(ResultCode.DATA_ALREADY_EXISTS.getCode(), ResultCode.DATA_ALREADY_EXISTS.getMessage(), detail);
    }

    /**
     * 用户不存在异常
     */
    public static DataException userNotFound() {
        return new DataException(ResultCode.USER_NOT_FOUND.getCode(), ResultCode.USER_NOT_FOUND.getMessage());
    }

    /**
     * 用户已存在异常
     */
    public static DataException userAlreadyExists() {
        return new DataException(ResultCode.USER_ALREADY_EXISTS.getCode(), ResultCode.USER_ALREADY_EXISTS.getMessage());
    }

    /**
     * 部门不存在异常
     */
    public static DataException departmentNotFound() {
        return new DataException(ResultCode.DEPARTMENT_NOT_FOUND.getCode(), ResultCode.DEPARTMENT_NOT_FOUND.getMessage());
    }

    /**
     * 部门已存在异常
     */
    public static DataException departmentAlreadyExists() {
        return new DataException(ResultCode.DEPARTMENT_ALREADY_EXISTS.getCode(), ResultCode.DEPARTMENT_ALREADY_EXISTS.getMessage());
    }

    /**
     * 岗位不存在异常
     */
    public static DataException positionNotFound() {
        return new DataException(ResultCode.POSITION_NOT_FOUND.getCode(), ResultCode.POSITION_NOT_FOUND.getMessage());
    }

    /**
     * 配置不存在异常
     */
    public static DataException configNotFound() {
        return new DataException(ResultCode.CONFIG_NOT_FOUND.getCode(), ResultCode.CONFIG_NOT_FOUND.getMessage());
    }

    /**
     * 字典不存在异常
     */
    public static DataException dictNotFound() {
        return new DataException(ResultCode.DICT_NOT_FOUND.getCode(), ResultCode.DICT_NOT_FOUND.getMessage());
    }
}
