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
 * 系统异常
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
public class SystemException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public SystemException(String message) {
        super(ResultCode.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public SystemException(Integer code, String message) {
        super(code, message);
    }

    public SystemException(String message, String detail) {
        super(ResultCode.INTERNAL_SERVER_ERROR.getCode(), message, detail);
    }

    public SystemException(Integer code, String message, String detail) {
        super(code, message, detail);
    }

    public SystemException(String message, Throwable cause) {
        super(ResultCode.INTERNAL_SERVER_ERROR.getCode(), message, cause);
    }

    public SystemException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }

    /**
     * 系统内部错误异常
     */
    public static SystemException internalError() {
        return new SystemException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), ResultCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    /**
     * 系统内部错误异常（带详情）
     */
    public static SystemException internalError(String detail) {
        return new SystemException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), ResultCode.INTERNAL_SERVER_ERROR.getMessage(), detail);
    }

    /**
     * 服务不可用异常
     */
    public static SystemException serviceUnavailable() {
        return new SystemException(ResultCode.SERVICE_UNAVAILABLE.getCode(), ResultCode.SERVICE_UNAVAILABLE.getMessage());
    }

    /**
     * 服务不可用异常（带详情）
     */
    public static SystemException serviceUnavailable(String detail) {
        return new SystemException(ResultCode.SERVICE_UNAVAILABLE.getCode(), ResultCode.SERVICE_UNAVAILABLE.getMessage(), detail);
    }

    /**
     * 第三方服务异常
     */
    public static SystemException thirdPartyServiceError() {
        return new SystemException(ResultCode.THIRD_PARTY_SERVICE_ERROR.getCode(), ResultCode.THIRD_PARTY_SERVICE_ERROR.getMessage());
    }

    /**
     * 第三方服务异常（带详情）
     */
    public static SystemException thirdPartyServiceError(String detail) {
        return new SystemException(ResultCode.THIRD_PARTY_SERVICE_ERROR.getCode(), ResultCode.THIRD_PARTY_SERVICE_ERROR.getMessage(), detail);
    }

    /**
     * 短信发送失败异常
     */
    public static SystemException smsSendFailed() {
        return new SystemException(ResultCode.SMS_SEND_FAILED.getCode(), ResultCode.SMS_SEND_FAILED.getMessage());
    }

    /**
     * 邮件发送失败异常
     */
    public static SystemException emailSendFailed() {
        return new SystemException(ResultCode.EMAIL_SEND_FAILED.getCode(), ResultCode.EMAIL_SEND_FAILED.getMessage());
    }

    /**
     * 文件上传失败异常
     */
    public static SystemException fileUploadFailed() {
        return new SystemException(ResultCode.FILE_UPLOAD_FAILED.getCode(), ResultCode.FILE_UPLOAD_FAILED.getMessage());
    }

    /**
     * 文件不存在异常
     */
    public static SystemException fileNotFound() {
        return new SystemException(ResultCode.FILE_NOT_FOUND.getCode(), ResultCode.FILE_NOT_FOUND.getMessage());
    }

    /**
     * 文件类型不支持异常
     */
    public static SystemException fileTypeNotSupported() {
        return new SystemException(ResultCode.FILE_TYPE_NOT_SUPPORTED.getCode(), ResultCode.FILE_TYPE_NOT_SUPPORTED.getMessage());
    }
}
