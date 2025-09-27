package com.xiaoxin.iam.audit.service;

/**
 * 登录日志服务接口
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public interface LoginLogService {

    /**
     * 记录登录成功日志
     *
     * @param username     用户名
     * @param loginIp      登录IP
     * @param loginLocation 登录地点
     * @param browser      浏览器类型
     * @param os           操作系统
     */
    void recordLoginSuccess(String username, String loginIp, String loginLocation, String browser, String os);

    /**
     * 记录登录失败日志
     *
     * @param username     用户名
     * @param loginIp      登录IP
     * @param loginLocation 登录地点
     * @param browser      浏览器类型
     * @param os           操作系统
     * @param errorMsg     错误消息
     */
    void recordLoginFailure(String username, String loginIp, String loginLocation, String browser, String os, String errorMsg);
}
