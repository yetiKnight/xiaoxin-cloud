package com.xiaoxin.iam.auth.controller;

import com.xiaoxin.iam.auth.dto.LoginRequest;
import com.xiaoxin.iam.auth.dto.LoginResponse;
import com.xiaoxin.iam.auth.service.impl.AuthServiceImpl;
import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.common.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 登录控制器
 * 处理用户名密码登录请求
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "登录管理", description = "用户登录相关接口")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final AuthServiceImpl authService;
    private final AuthenticationManager authenticationManager;

    public LoginController(AuthServiceImpl authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户名密码登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, 
                                      HttpServletRequest request) {
        log.info("用户登录请求: {}", loginRequest.getUsername());
        
        // 设置登录IP和用户代理
        loginRequest.setLoginIp(IpUtils.getClientIp(request));
        loginRequest.setUserAgent(request.getHeader("User-Agent"));
        
        LoginResponse response = authService.login(loginRequest);
        
        log.info("用户登录成功: {}", loginRequest.getUsername());
        return Result.success(response);
    }

    /**
     * 自定义登录端点（用于OAuth2流程中的用户认证）
     */
    @PostMapping("/authenticate")
    @Operation(summary = "用户认证", description = "用于OAuth2流程中的用户认证")
    public Result<?> authenticate(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        log.info("用户认证请求: {}", username);
        
        try {
            // 使用Spring Security进行认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            log.info("用户认证成功: {}", username);
            return Result.success(Map.of("message", "认证成功"));
        } catch (Exception e) {
            log.error("用户认证失败: username={}", username, e);
            return Result.failed(500, "认证失败: " + e.getMessage());
        }
    }
}