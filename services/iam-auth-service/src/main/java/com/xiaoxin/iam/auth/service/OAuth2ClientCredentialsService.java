package com.xiaoxin.iam.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2客户端凭据模式认证服务
 * 用于服务间调用的认证
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ClientCredentialsService {

    private final RegisteredClientRepository registeredClientRepository;

    private final OAuth2AuthorizationService authorizationService;

    private final PasswordEncoder passwordEncoder;

    /**
     * 客户端凭据模式获取访问令牌
     *
     * @param clientId 客户端ID
     * @param clientSecret 客户端密钥
     * @param scope 请求的作用域
     * @return OAuth2访问令牌响应
     */
    public Result<OAuth2AccessTokenResponse> getAccessToken(String clientId, String clientSecret, String scope) {
        try {
            log.info("客户端凭据模式认证请求: clientId={}, scope={}", clientId, scope);

            // 1. 验证客户端
            RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
            if (registeredClient == null) {
                log.warn("客户端不存在: {}", clientId);
                return Result.failed(401, "客户端不存在");
            }

            // 2. 验证客户端密钥
            if (!passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
                log.warn("客户端密钥验证失败: {}", clientId);
                return Result.failed(401, "客户端密钥错误");
            }

            // 3. 检查授权类型
            if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
                log.warn("客户端不支持client_credentials授权类型: {}", clientId);
                return Result.failed(400, "客户端不支持client_credentials授权类型");
            }

            // 4. 验证作用域
            Set<String> requestedScopes = parseScopes(scope);
            Set<String> authorizedScopes = registeredClient.getScopes();
            if (!authorizedScopes.containsAll(requestedScopes)) {
                log.warn("请求的作用域超出客户端授权范围: clientId={}, requested={}, authorized={}", 
                        clientId, requestedScopes, authorizedScopes);
                return Result.failed(400, "请求的作用域超出授权范围");
            }

            // 5. 生成访问令牌
            OAuth2AccessToken accessToken = generateAccessToken(registeredClient, requestedScopes);

            // 6. 保存授权信息
            OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .principalName(clientId)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .authorizedScopes(requestedScopes)
                    .accessToken(accessToken)
                    .build();

            authorizationService.save(authorization);

            // 7. 构建响应
            OAuth2AccessTokenResponse response = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                    .tokenType(accessToken.getTokenType())
                    .expiresIn(Duration.between(Instant.now(), accessToken.getExpiresAt()).getSeconds())
                    .scopes(requestedScopes)
                    .build();

            log.info("客户端凭据模式认证成功: clientId={}", clientId);
            return Result.success(response);

        } catch (Exception e) {
            log.error("客户端凭据模式认证失败: clientId={}, error={}", clientId, e.getMessage(), e);
            return Result.failed(500, "认证失败: " + e.getMessage());
        }
    }

    /**
     * 验证访问令牌
     *
     * @param token 访问令牌
     * @return 验证结果
     */
    public Result<OAuth2Authorization> validateToken(String token) {
        try {
            OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            if (authorization == null) {
                return Result.failed(401, "无效的访问令牌");
            }

            OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
            if (accessToken == null || accessToken.getToken().getExpiresAt() == null || 
                accessToken.getToken().getExpiresAt().isBefore(Instant.now())) {
                return Result.failed(401, "访问令牌已过期");
            }

            return Result.success(authorization);
        } catch (Exception e) {
            log.error("令牌验证失败: token={}, error={}", token, e.getMessage(), e);
            return Result.failed(500, "令牌验证失败");
        }
    }

    /**
     * 撤销访问令牌
     *
     * @param token 访问令牌
     * @return 撤销结果
     */
    public Result<Void> revokeToken(String token) {
        try {
            OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            if (authorization != null) {
                authorizationService.remove(authorization);
                log.info("访问令牌已撤销: clientId={}", authorization.getPrincipalName());
            }
            return Result.success();
        } catch (Exception e) {
            log.error("令牌撤销失败: token={}, error={}", token, e.getMessage(), e);
            return Result.failed(500, "令牌撤销失败");
        }
    }

    /**
     * 生成访问令牌
     */
    private OAuth2AccessToken generateAccessToken(RegisteredClient registeredClient, Set<String> scopes) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofHours(1)); // 1小时有效期

        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generateTokenValue(),
                issuedAt,
                expiresAt,
                scopes
        );
    }

    /**
     * 生成令牌值
     */
    private String generateTokenValue() {
        // 这里应该使用安全的令牌生成器
        // 简化实现，实际应该集成JWT或其他安全令牌生成机制
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

/**
 * 解析作用域参数
 * 严格按照OAuth2规范，仅支持空格分隔的作用域字符串
 * 
 * @param scope 作用域字符串
 * @return 作用域集合
 */
private Set<String> parseScopes(String scope) {
    if (StringUtils.isBlank(scope)) {
        return Set.of();
    }
    
    // 按照OAuth2规范，仅支持空格分隔
    return StringUtils.splitToSet(scope.trim(), " ")
            .stream()
            .filter(org.springframework.util.StringUtils::hasText)
            .map(String::trim)
            .collect(Collectors.toSet());
}
}
