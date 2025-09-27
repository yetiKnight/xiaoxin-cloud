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
        String fullPath = getFullPath(template);
        log.info("Feign拦截器处理请求: url={}, fullPath={}, method={}", url, fullPath, template.method());
        
        // 只有内部API调用才添加OAuth2令牌
        if (!isInternalApi(fullPath)) {
            log.info("非内部API调用，跳过OAuth2认证: {}", fullPath);
            return;
        }

        if (!clientProperties.isEnabled()) {
            log.warn("OAuth2客户端未启用，但请求内部API: {}", url);
            return;
        }

        log.info("开始为内部API调用获取OAuth2令牌: fullPath={}, clientId={}", 
                fullPath, clientProperties.getClientId());

        try {
            String accessToken = getAccessToken();
            if (StringUtils.hasText(accessToken)) {
                template.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                log.info("成功为内部API调用添加OAuth2令牌: fullPath={}, tokenPrefix={}", 
                        fullPath, accessToken.substring(0, Math.min(10, accessToken.length())) + "...");
            } else {
                log.error("获取OAuth2访问令牌失败，API调用可能失败: fullPath={}", fullPath);
            }
        } catch (Exception e) {
            log.error("OAuth2认证过程中发生异常，API调用: fullPath={}, error={}", fullPath, e.getMessage(), e);
        }
    }

    /**
     * 获取完整路径
     * 从RequestTemplate中获取包含path的完整路径
     */
    private String getFullPath(RequestTemplate template) {
        String url = template.url();
        
        // 尝试从RequestTemplate获取完整路径信息
        try {
            // 获取Feign Target信息
            Object feignTarget = template.feignTarget();
            if (feignTarget != null) {
                String targetStr = feignTarget.toString();
                log.debug("FeignTarget信息: {}", targetStr);
                
                // 从Target中提取URL信息，通常格式为: HardCodedTarget(type=CoreServiceClient, name=iam-core-service, url=http://iam-core-service/api/v1/internal)
                if (targetStr.contains("url=http://") && targetStr.contains("/api/")) {
                    String urlPart = targetStr.substring(targetStr.indexOf("url=http://"));
                    if (urlPart.contains(")")) {
                        urlPart = urlPart.substring(0, urlPart.indexOf(")"));
                    }
                    // 提取路径部分 (去掉协议和主机名)
                    if (urlPart.contains("://") && urlPart.indexOf("/", urlPart.indexOf("://") + 3) > 0) {
                        String path = urlPart.substring(urlPart.indexOf("/", urlPart.indexOf("://") + 3));
                        String fullPath = path + url;
                        log.debug("从FeignTarget提取的完整路径: {}", fullPath);
                        return fullPath;
                    }
                }
            }
        } catch (Exception e) {
            log.debug("无法从FeignTarget获取path信息: {}", e.getMessage());
        }
        
        // 如果URL已经包含完整路径，直接返回
        if (url.startsWith("/api/")) {
            return url;
        }
        
        // 默认假设是内部API调用，拼接内部API前缀
        return INTERNAL_API_PREFIX + url;
    }

    /**
     * 判断是否为内部API
     */
    private boolean isInternalApi(String fullPath) {
        return StringUtils.hasText(fullPath) && fullPath.contains(INTERNAL_API_PREFIX);
    }

    /**
     * 获取访问令牌（带缓存）
     */
    private String getAccessToken() {
        String cacheKey = generateCacheKey();
        TokenCache cache = TOKEN_CACHE.get(cacheKey);

        log.info("检查OAuth2令牌缓存: cacheKey={}, hasCache={}", cacheKey, cache != null);

        // 检查缓存是否有效
        if (cache != null && cache.isValid()) {
            log.info("使用缓存的OAuth2令牌，剩余有效时间: {}秒", 
                    ChronoUnit.SECONDS.between(LocalDateTime.now(), cache.expiresAt));
            return cache.getAccessToken();
        }

        // 缓存无效，重新获取令牌
        synchronized (this) {
            // 双重检查锁定
            cache = TOKEN_CACHE.get(cacheKey);
            if (cache != null && cache.isValid()) {
                return cache.getAccessToken();
            }

            log.info("缓存无效，开始获取新的OAuth2访问令牌: clientId={}, tokenUri={}", 
                    clientProperties.getClientId(), clientProperties.getTokenUri());
            String newToken = requestAccessToken();
            if (StringUtils.hasText(newToken)) {
                // 缓存新令牌
                LocalDateTime expiresAt = LocalDateTime.now().plus(clientProperties.getTokenCacheExpire(), ChronoUnit.SECONDS);
                TOKEN_CACHE.put(cacheKey, new TokenCache(newToken, expiresAt));
                log.info("OAuth2令牌已缓存，过期时间: {}, tokenPrefix: {}", 
                        expiresAt, newToken.substring(0, Math.min(10, newToken.length())) + "...");
                return newToken;
            } else {
                log.error("获取OAuth2访问令牌失败，返回null");
            }
        }

        return null;
    }

    /**
     * 向授权服务器请求访问令牌
     */
    private String requestAccessToken() {
        try {
            log.info("开始请求OAuth2访问令牌，参数: clientId={}, tokenUri={}, scope={}", 
                    clientProperties.getClientId(), clientProperties.getTokenUri(), clientProperties.getFormattedScope());
            
            // 构建请求参数
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            params.add("client_id", clientProperties.getClientId());
            params.add("client_secret", clientProperties.getClientSecret());
            if (StringUtils.hasText(clientProperties.getScope())) {
                String formattedScope = clientProperties.getFormattedScope();
                params.add("scope", formattedScope);
                log.info("请求scope: {} (原始: {})", formattedScope, clientProperties.getScope());
            }

            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            log.info("发送OAuth2令牌请求到: {}", clientProperties.getTokenUri());
            
            // 发送请求
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    clientProperties.getTokenUri(), 
                    request, 
                    Map.class
            );

            log.info("OAuth2令牌请求响应: statusCode={}, hasBody={}", 
                    response.getStatusCode(), response.getBody() != null);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                log.info("OAuth2响应内容: {}", responseBody.keySet());
                
                String accessToken = (String) responseBody.get("access_token");
                if (StringUtils.hasText(accessToken)) {
                    log.info("成功获取OAuth2访问令牌，长度: {}", accessToken.length());
                    return accessToken;
                } else {
                    log.error("OAuth2响应中没有access_token字段: {}", responseBody);
                }
            } else {
                log.error("OAuth2令牌请求失败，状态码: {}, 响应: {}", 
                         response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("请求OAuth2访问令牌时发生异常: exceptionType={}, message={}", 
                     e.getClass().getSimpleName(), e.getMessage(), e);
        }

        return null;
    }

    /**
     * 生成缓存键
     */
    private String generateCacheKey() {
        return String.format("oauth2:token:%s:%s", 
                           clientProperties.getClientId(), 
                           clientProperties.getFormattedScope());
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
