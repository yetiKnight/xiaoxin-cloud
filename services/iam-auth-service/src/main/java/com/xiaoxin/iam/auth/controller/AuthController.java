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
import org.springframework.web.bind.annotation.*;


/**
 * 认证控制器
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
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
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，使令牌失效")
    public Result<Void> logout(@RequestHeader("Authorization") String authorization) {
        log.info("用户登出请求");
        
        // 提取令牌
        String token = authorization.startsWith("Bearer ") ? 
            authorization.substring(7) : authorization;
        
        authService.logout(token);
        
        log.info("用户登出成功");
        return Result.success();
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public Result<String> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        log.info("刷新令牌请求");
        
        String newAccessToken = authService.refreshToken(refreshToken);
        
        log.info("令牌刷新成功");
        return Result.success(newAccessToken);
    }

    /**
     * 验证令牌
     */
    @GetMapping("/validate")
    @Operation(summary = "验证令牌", description = "验证访问令牌是否有效")
    public Result<Boolean> validateToken(@RequestHeader("Authorization") String authorization) {
        log.debug("验证令牌请求");
        
        // 提取令牌
        String token = authorization.startsWith("Bearer ") ? 
            authorization.substring(7) : authorization;
        
        boolean isValid = authService.validateToken(token);
        
        return Result.success(isValid);
    }
}
