package com.xiaoxin.iam.auth.controller;

import com.xiaoxin.iam.auth.service.OAuth2ClientCredentialsService;
import com.xiaoxin.iam.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth2令牌控制器
 * 提供客户端凭据模式的令牌获取接口
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/oauth2")
@Tag(name = "OAuth2令牌管理", description = "OAuth2令牌相关接口")
public class OAuth2TokenController {

    @Autowired
    private OAuth2ClientCredentialsService clientCredentialsService;

    /**
     * 客户端凭据模式获取访问令牌
     * 用于服务间调用认证
     */
    @PostMapping("/token")
    @Operation(summary = "获取访问令牌", description = "使用客户端凭据模式获取访问令牌")
    public Result<OAuth2AccessTokenResponse> getToken(
            @Parameter(description = "授权类型，固定值：client_credentials")
            @RequestParam("grant_type") String grantType,
            
            @Parameter(description = "客户端ID")
            @RequestParam("client_id") String clientId,
            
            @Parameter(description = "客户端密钥")
            @RequestParam("client_secret") String clientSecret,
            
            @Parameter(description = "请求的作用域，多个用空格分隔")
            @RequestParam(value = "scope", required = false, defaultValue = "internal:read internal:write") String scope) {

        log.info("OAuth2令牌请求: grantType={}, clientId={}, scope={}", grantType, clientId, scope);

        // 验证授权类型
        if (!"client_credentials".equals(grantType)) {
            return Result.failed(400, "不支持的授权类型: " + grantType);
        }

        // 获取访问令牌
        return clientCredentialsService.getAccessToken(clientId, clientSecret, scope);
    }

    /**
     * 验证访问令牌
     */
    @PostMapping("/introspect")
    @Operation(summary = "验证令牌", description = "验证访问令牌的有效性")
    public Result<TokenIntrospectionResponse> introspectToken(
            @Parameter(description = "访问令牌")
            @RequestParam("token") String token) {

        log.debug("令牌验证请求: token={}", token.substring(0, Math.min(token.length(), 10)) + "...");

        var result = clientCredentialsService.validateToken(token);
        if (!result.isSuccess()) {
            return Result.failed(result.getCode(), result.getMessage());
        }

        var authorization = result.getData();
        var accessToken = authorization.getAccessToken();

        TokenIntrospectionResponse response = TokenIntrospectionResponse.builder()
                .active(true)
                .clientId(authorization.getPrincipalName())
                .scope(String.join(" ", accessToken.getToken().getScopes()))
                .tokenType(accessToken.getToken().getTokenType().getValue())
                .exp(accessToken.getToken().getExpiresAt().getEpochSecond())
                .iat(accessToken.getToken().getIssuedAt().getEpochSecond())
                .build();

        return Result.success(response);
    }

    /**
     * 撤销访问令牌
     */
    @PostMapping("/revoke")
    @Operation(summary = "撤销令牌", description = "撤销访问令牌")
    public Result<Void> revokeToken(
            @Parameter(description = "访问令牌")
            @RequestParam("token") String token,
            
            @Parameter(description = "客户端ID")
            @RequestParam("client_id") String clientId,
            
            @Parameter(description = "客户端密钥")
            @RequestParam("client_secret") String clientSecret) {

        log.info("令牌撤销请求: clientId={}", clientId);

        // 验证客户端身份（简化实现）
        // 实际应该验证客户端是否有权撤销该令牌

        return clientCredentialsService.revokeToken(token);
    }

    /**
     * 令牌内省响应
     */
    @lombok.Data
    @lombok.Builder
    public static class TokenIntrospectionResponse {
        private boolean active;
        private String clientId;
        private String scope;
        private String tokenType;
        private long exp;
        private long iat;
    }
}
