package com.xiaoxin.iam.audit.listener;

import com.xiaoxin.iam.audit.service.LoginLogService;
import com.xiaoxin.iam.common.event.LoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 登录事件监听器
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
public class LoginEventListener {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 监听登录事件
     */
    @EventListener
    public void handleLoginEvent(LoginEvent loginEvent) {
        try {
            if ("LOGIN_SUCCESS".equals(loginEvent.getEventType())) {
                loginLogService.recordLoginSuccess(
                        loginEvent.getUsername(),
                        loginEvent.getLoginIp(),
                        loginEvent.getLoginLocation(),
                        loginEvent.getBrowser(),
                        loginEvent.getOs()
                );
            } else if ("LOGIN_FAILURE".equals(loginEvent.getEventType())) {
                loginLogService.recordLoginFailure(
                        loginEvent.getUsername(),
                        loginEvent.getLoginIp(),
                        loginEvent.getLoginLocation(),
                        loginEvent.getBrowser(),
                        loginEvent.getOs(),
                        loginEvent.getErrorMsg()
                );
            }
        } catch (Exception e) {
            log.error("处理登录事件失败: {}", loginEvent, e);
        }
    }
}
