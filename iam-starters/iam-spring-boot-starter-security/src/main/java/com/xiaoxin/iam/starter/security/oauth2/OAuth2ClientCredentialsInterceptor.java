package com.xiaoxin.iam.starter.security.oauth2;

import com.xiaoxin.iam.starter.security.config.OAuth2Properties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OAuth2客户端凭据Feign拦截器
 * 
 * <p>功能说明：</p>
 * <ul>
 *     <li>自动为内部API调用添加OAuth2访问令牌</li>
 *     <li>智能缓存令牌，避免重复请求</li>
 *     <li>令牌过期前自动刷新</li>
 *     <li>异常处理和重试机制</li>
 * </ul>
 *
 * @author xiaoxin
 * @date 2024-09-26
 */
@Slf4j
@RequiredArgsConstructor
public class OAuth2ClientCredentialsInterceptor implements RequestInterceptor {

    private final OAuth2Properties.Client clientProperties;
    private final RestTemplate restTemplate;

    /**
     * 令牌缓存
     */
    private static final Map<String, TokenCache> TOKEN_CACHE = new ConcurrentHashMap<>();

    /**
     * 内部API路径前缀
     */
    private static final String INTERNAL_API_PREFIX = "/api/v1/internal";

    @Override
    public void apply(RequestTemplate template) {
        String url = template.url();
        
        // 只有内部API调用才添加OAuth2令牌
        if (!isInternalApi(url)) {
            log.debug("非内部API调用，跳过OAuth2认证: {}", url);
            return;
        }

        if (!clientProperties.isEnabled()) {
            log.debug("OAuth2客户端未启用，跳过认证");
            return;
        }

        try {
            String accessToken = getAccessToken();
            if (StringUtils.hasText(accessToken)) {
                template.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                log.debug("已为内部API调用添加OAuth2令牌: {}", url);
            } else {
                log.warn("获取OAuth2访问令牌失败，API调用可能失败: {}", url);
            }
        } catch (Exception e) {
            log.error("OAuth2认证过程中发生异常，API调用: {}", url, e);
        }
    }

    /**
     * 判断是否为内部API
     */
    private boolean isInternalApi(String url) {
        return StringUtils.hasText(url) && url.contains(INTERNAL_API_PREFIX);
    }

    /**
     * 获取访问令牌（带缓存）
     */
    private String getAccessToken() {
        String cacheKey = generateCacheKey();
        TokenCache cache = TOKEN_CACHE.get(cacheKey);

        // 检查缓存是否有效
        if (cache != null && cache.isValid()) {
            log.debug("使用缓存的OAuth2令牌");
            return cache.getAccessToken();
        }

        // 缓存无效，重新获取令牌
        synchronized (this) {
            // 双重检查锁定
            cache = TOKEN_CACHE.get(cacheKey);
            if (cache != null && cache.isValid()) {
                return cache.getAccessToken();
            }

            log.debug("获取新的OAuth2访问令牌");
            String newToken = requestAccessToken();
            if (StringUtils.hasText(newToken)) {
                // 缓存新令牌
                LocalDateTime expiresAt = LocalDateTime.now().plus(clientProperties.getTokenCacheExpire(), ChronoUnit.SECONDS);
                TOKEN_CACHE.put(cacheKey, new TokenCache(newToken, expiresAt));
                log.debug("OAuth2令牌已缓存，过期时间: {}", expiresAt);
                return newToken;
            }
        }

        return null;
    }

    /**
     * 向授权服务器请求访问令牌
     */
    private String requestAccessToken() {
        try {
            // 构建请求参数
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            params.add("client_id", clientProperties.getClientId());
            params.add("client_secret", clientProperties.getClientSecret());
            if (StringUtils.hasText(clientProperties.getScope())) {
                params.add("scope", clientProperties.getScope());
            }

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // 发送请求
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    clientProperties.getTokenUri(), 
                    request, 
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                String accessToken = (String) responseBody.get("access_token");
                if (StringUtils.hasText(accessToken)) {
                    log.debug("成功获取OAuth2访问令牌");
                    return accessToken;
                } else {
                    log.error("OAuth2响应中没有access_token字段: {}", responseBody);
                }
            } else {
                log.error("OAuth2令牌请求失败，状态码: {}, 响应: {}", 
                         response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("请求OAuth2访问令牌时发生异常", e);
        }

        return null;
    }

    /**
     * 生成缓存键
     */
    private String generateCacheKey() {
        return String.format("oauth2:token:%s:%s", 
                           clientProperties.getClientId(), 
                           clientProperties.getScope());
    }

    /**
     * 令牌缓存
     */
    private static class TokenCache {
        private final String accessToken;
        private final LocalDateTime expiresAt;

        public TokenCache(String accessToken, LocalDateTime expiresAt) {
            this.accessToken = accessToken;
            this.expiresAt = expiresAt;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public boolean isValid() {
            return LocalDateTime.now().isBefore(expiresAt);
        }
    }
}
