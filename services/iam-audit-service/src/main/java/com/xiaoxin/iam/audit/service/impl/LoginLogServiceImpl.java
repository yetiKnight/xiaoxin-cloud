package com.xiaoxin.iam.audit.service.impl;

import com.xiaoxin.iam.audit.entity.LoginLog;
import com.xiaoxin.iam.audit.mapper.LoginLogMapper;
import com.xiaoxin.iam.audit.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 登录日志服务实现类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public void recordLoginSuccess(String username, String loginIp, String loginLocation, String browser, String os) {
        try {
            LoginLog loginLog = LoginLog.builder()
                    .userName(username)
                    .ipaddr(loginIp)
                    .loginLocation(loginLocation)
                    .browser(browser)
                    .os(os)
                    .status("0") // 成功状态
                    .msg("登录成功")
                    .loginTime(LocalDateTime.now())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .delFlag("0")
                    .build();
            
            loginLogMapper.insert(loginLog);
            log.info("记录登录成功日志: username={}, ip={}", username, loginIp);
        } catch (Exception e) {
            log.error("记录登录成功日志失败: username={}, ip={}", username, loginIp, e);
        }
    }

    @Override
    public void recordLoginFailure(String username, String loginIp, String loginLocation, String browser, String os, String errorMsg) {
        try {
            LoginLog loginLog = LoginLog.builder()
                    .userName(username)
                    .ipaddr(loginIp)
                    .loginLocation(loginLocation)
                    .browser(browser)
                    .os(os)
                    .status("1") // 失败状态
                    .msg(errorMsg)
                    .loginTime(LocalDateTime.now())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .delFlag("0")
                    .build();
            
            loginLogMapper.insert(loginLog);
            log.info("记录登录失败日志: username={}, ip={}, error={}", username, loginIp, errorMsg);
        } catch (Exception e) {
            log.error("记录登录失败日志失败: username={}, ip={}", username, loginIp, e);
        }
    }
}
