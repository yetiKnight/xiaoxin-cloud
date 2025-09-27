package com.xiaoxin.iam.auth.controller;

import com.xiaoxin.iam.auth.service.OAuth2ClientCredentialsService;
import com.xiaoxin.iam.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth2调试控制器
 * 用于调试和验证OAuth2客户端凭据模式
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/debug/oauth2")
@RequiredArgsConstructor
public class OAuth2DebugController {

    private final OAuth2ClientCredentialsService oauth2ClientCredentialsService;

    /**
     * 测试获取访问令牌
     */
    @PostMapping("/token")
    public Result<OAuth2AccessTokenResponse> getAccessToken(
            @RequestParam String clientId,
            @RequestParam String clientSecret,
            @RequestParam(required = false) String scope) {
        
        log.info("调试请求获取OAuth2令牌: clientId={}, scope={}", clientId, scope);
        
        return oauth2ClientCredentialsService.getAccessToken(clientId, clientSecret, scope);
    }

    /**
     * 测试默认客户端令牌获取
     */
    @PostMapping("/token/default")
    public Result<OAuth2AccessTokenResponse> getDefaultToken() {
        log.info("调试请求获取默认OAuth2令牌");
        
        return oauth2ClientCredentialsService.getAccessToken(
                "iam-auth-service", 
                "auth-service-secret", 
                "internal.read internal.write user.read user.write"
        );
    }

    /**
     * 验证令牌
     */
    @PostMapping("/token/validate")
    public Result<?> validateToken(@RequestParam String token) {
        log.info("调试验证OAuth2令牌: tokenPrefix={}", 
                token.substring(0, Math.min(10, token.length())) + "...");
        
        return oauth2ClientCredentialsService.validateToken(token);
    }
}
