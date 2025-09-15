package com.xiaoxin.iam.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResult {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

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
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(405, "方法不允许"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "请求超时"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(1000, "业务异常"),

    /**
     * 用户相关错误
     */
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_ALREADY_EXISTS(2002, "用户已存在"),
    USER_DISABLED(2003, "用户已禁用"),
    USER_LOCKED(2004, "用户已锁定"),
    PASSWORD_ERROR(2005, "密码错误"),
    PASSWORD_EXPIRED(2006, "密码已过期"),

    /**
     * 认证相关错误
     */
    TOKEN_INVALID(3001, "Token无效"),
    TOKEN_EXPIRED(3002, "Token已过期"),
    TOKEN_MISSING(3003, "Token缺失"),
    LOGIN_FAILED(3004, "登录失败"),
    LOGOUT_SUCCESS(3005, "退出成功"),

    /**
     * 权限相关错误
     */
    PERMISSION_DENIED(4001, "权限不足"),
    ROLE_NOT_FOUND(4002, "角色不存在"),
    PERMISSION_NOT_FOUND(4003, "权限不存在"),

    /**
     * 组织架构相关错误
     */
    DEPARTMENT_NOT_FOUND(5001, "部门不存在"),
    DEPARTMENT_ALREADY_EXISTS(5002, "部门已存在"),
    POSITION_NOT_FOUND(5003, "岗位不存在"),

    /**
     * 系统配置相关错误
     */
    CONFIG_NOT_FOUND(6001, "配置不存在"),
    DICT_NOT_FOUND(6002, "字典不存在"),

    /**
     * 文件相关错误
     */
    FILE_UPLOAD_FAILED(7001, "文件上传失败"),
    FILE_NOT_FOUND(7002, "文件不存在"),
    FILE_TYPE_NOT_SUPPORTED(7003, "文件类型不支持"),

    /**
     * 数据库相关错误
     */
    DATABASE_ERROR(8001, "数据库操作失败"),
    DATA_NOT_FOUND(8002, "数据不存在"),
    DATA_ALREADY_EXISTS(8003, "数据已存在"),

    /**
     * 第三方服务相关错误
     */
    THIRD_PARTY_SERVICE_ERROR(9001, "第三方服务异常"),
    SMS_SEND_FAILED(9002, "短信发送失败"),
    EMAIL_SEND_FAILED(9003, "邮件发送失败");

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String message;
}
