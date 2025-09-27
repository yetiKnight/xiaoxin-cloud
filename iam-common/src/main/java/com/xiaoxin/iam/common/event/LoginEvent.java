package com.xiaoxin.iam.common.event;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 登录事件
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginEvent {

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态（SUCCESS/FAILURE）
     */
    private String status;

    /**
     * 错误消息（失败时）
     */
    private String errorMsg;

    /**
     * 事件时间
     */
    private Long timestamp;

    /**
     * 创建登录成功事件
     */
    public static LoginEvent createSuccessEvent(String username, String loginIp, String loginLocation, String browser, String os) {
        return LoginEvent.builder()
                .eventType("LOGIN_SUCCESS")
                .username(username)
                .loginIp(loginIp)
                .loginLocation(loginLocation)
                .browser(browser)
                .os(os)
                .status("SUCCESS")
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 创建登录失败事件
     */
    public static LoginEvent createFailureEvent(String username, String loginIp, String loginLocation, String browser, String os, String errorMsg) {
        return LoginEvent.builder()
                .eventType("LOGIN_FAILURE")
                .username(username)
                .loginIp(loginIp)
                .loginLocation(loginLocation)
                .browser(browser)
                .os(os)
                .status("FAILURE")
                .errorMsg(errorMsg)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
