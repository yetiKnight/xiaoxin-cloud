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
 * 认证异常
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class AuthException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public AuthException(String message) {
        super(ResultCode.UNAUTHORIZED.getCode(), message);
    }

    public AuthException(Integer code, String message) {
        super(code, message);
    }

    public AuthException(String message, String detail) {
        super(ResultCode.UNAUTHORIZED.getCode(), message, detail);
    }

    public AuthException(Integer code, String message, String detail) {
        super(code, message, detail);
    }

    public AuthException(String message, Throwable cause) {
        super(ResultCode.UNAUTHORIZED.getCode(), message, cause);
    }

    /**
     * Token无效异常
     */
    public static AuthException tokenInvalid() {
        return new AuthException(ResultCode.TOKEN_INVALID.getCode(), ResultCode.TOKEN_INVALID.getMessage());
    }

    /**
     * Token过期异常
     */
    public static AuthException tokenExpired() {
        return new AuthException(ResultCode.TOKEN_EXPIRED.getCode(), ResultCode.TOKEN_EXPIRED.getMessage());
    }

    /**
     * Token缺失异常
     */
    public static AuthException tokenMissing() {
        return new AuthException(ResultCode.TOKEN_MISSING.getCode(), ResultCode.TOKEN_MISSING.getMessage());
    }

    /**
     * 登录失败异常
     */
    public static AuthException loginFailed() {
        return new AuthException(ResultCode.LOGIN_FAILED.getCode(), ResultCode.LOGIN_FAILED.getMessage());
    }

    /**
     * 登录失败异常（带详情）
     */
    public static AuthException loginFailed(String detail) {
        return new AuthException(ResultCode.LOGIN_FAILED.getCode(), ResultCode.LOGIN_FAILED.getMessage(), detail);
    }

    /**
     * 用户不存在异常
     */
    public static AuthException userNotFound() {
        return new AuthException(ResultCode.USER_NOT_FOUND.getCode(), ResultCode.USER_NOT_FOUND.getMessage());
    }

    /**
     * 用户已禁用异常
     */
    public static AuthException userDisabled() {
        return new AuthException(ResultCode.USER_DISABLED.getCode(), ResultCode.USER_DISABLED.getMessage());
    }

    /**
     * 用户已锁定异常
     */
    public static AuthException userLocked() {
        return new AuthException(ResultCode.USER_LOCKED.getCode(), ResultCode.USER_LOCKED.getMessage());
    }

    /**
     * 密码错误异常
     */
    public static AuthException passwordError() {
        return new AuthException(ResultCode.PASSWORD_ERROR.getCode(), ResultCode.PASSWORD_ERROR.getMessage());
    }

    /**
     * 密码过期异常
     */
    public static AuthException passwordExpired() {
        return new AuthException(ResultCode.PASSWORD_EXPIRED.getCode(), ResultCode.PASSWORD_EXPIRED.getMessage());
    }
}
