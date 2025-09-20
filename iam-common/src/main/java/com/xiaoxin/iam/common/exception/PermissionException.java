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
 * 权限异常
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class PermissionException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public PermissionException(String message) {
        super(ResultCode.FORBIDDEN.getCode(), message);
    }

    public PermissionException(Integer code, String message) {
        super(code, message);
    }

    public PermissionException(String message, String detail) {
        super(ResultCode.FORBIDDEN.getCode(), message, detail);
    }

    public PermissionException(Integer code, String message, String detail) {
        super(code, message, detail);
    }

    public PermissionException(String message, Throwable cause) {
        super(ResultCode.FORBIDDEN.getCode(), message, cause);
    }

    /**
     * 权限不足异常
     */
    public static PermissionException denied() {
        return new PermissionException(ResultCode.PERMISSION_DENIED.getCode(), ResultCode.PERMISSION_DENIED.getMessage());
    }

    /**
     * 权限不足异常（带详情）
     */
    public static PermissionException denied(String detail) {
        return new PermissionException(ResultCode.PERMISSION_DENIED.getCode(), ResultCode.PERMISSION_DENIED.getMessage(), detail);
    }

    /**
     * 角色不存在异常
     */
    public static PermissionException roleNotFound() {
        return new PermissionException(ResultCode.ROLE_NOT_FOUND.getCode(), ResultCode.ROLE_NOT_FOUND.getMessage());
    }

    /**
     * 权限不存在异常
     */
    public static PermissionException permissionNotFound() {
        return new PermissionException(ResultCode.PERMISSION_NOT_FOUND.getCode(), ResultCode.PERMISSION_NOT_FOUND.getMessage());
    }

    /**
     * 访问被拒绝异常
     */
    public static PermissionException accessDenied() {
        return new PermissionException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage());
    }

    /**
     * 访问被拒绝异常（带详情）
     */
    public static PermissionException accessDenied(String detail) {
        return new PermissionException(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), detail);
    }
}
