package com.xiaoxin.iam.gateway.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.xiaoxin.iam.gateway.config.SecurityProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT工具类
 * 
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final SecurityProperties securityProperties;

    /**
     * 验证JWT Token
     * 
     * @param token JWT Token
     * @return 验证结果
     */
    public JwtValidationResult validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return JwtValidationResult.invalid("Token为空");
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(
                securityProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
            
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            // 检查Token是否过期
            if (claims.getExpiration().before(new Date())) {
                log.warn("JWT Token已过期: {}", claims.getExpiration());
                return JwtValidationResult.expired();
            }

            // 提取用户信息
            String userId = claims.getSubject();
            String username = claims.get("username", String.class);
            String roles = claims.get("roles", String.class);
            String permissions = claims.get("permissions", String.class);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("username", username);
            userInfo.put("roles", roles);
            userInfo.put("permissions", permissions);

            log.debug("JWT Token验证成功，用户: {}", username);
            return JwtValidationResult.valid(userInfo);

        } catch (ExpiredJwtException e) {
            log.warn("JWT Token已过期: {}", e.getMessage());
            return JwtValidationResult.expired();
        } catch (UnsupportedJwtException e) {
            log.error("不支持的JWT Token: {}", e.getMessage());
            return JwtValidationResult.invalid("不支持的Token格式");
        } catch (MalformedJwtException e) {
            log.error("JWT Token格式错误: {}", e.getMessage());
            return JwtValidationResult.invalid("Token格式错误");
        } catch (SignatureException e) {
            log.error("JWT Token签名验证失败: {}", e.getMessage());
            return JwtValidationResult.invalid("Token签名无效");
        } catch (IllegalArgumentException e) {
            log.error("JWT Token参数错误: {}", e.getMessage());
            return JwtValidationResult.invalid("Token参数错误");
        } catch (Exception e) {
            log.error("JWT Token验证失败: {}", e.getMessage(), e);
            return JwtValidationResult.invalid("Token验证失败");
        }
    }

    /**
     * JWT验证结果
     */
    public static class JwtValidationResult {
        private final boolean valid;
        private final boolean expired;
        private final String message;
        private final Map<String, Object> userInfo;

        private JwtValidationResult(boolean valid, boolean expired, String message, Map<String, Object> userInfo) {
            this.valid = valid;
            this.expired = expired;
            this.message = message;
            this.userInfo = userInfo;
        }

        public static JwtValidationResult valid(Map<String, Object> userInfo) {
            return new JwtValidationResult(true, false, "验证成功", userInfo);
        }

        public static JwtValidationResult expired() {
            return new JwtValidationResult(false, true, "Token已过期", null);
        }

        public static JwtValidationResult invalid(String message) {
            return new JwtValidationResult(false, false, message, null);
        }

        public boolean isValid() {
            return valid;
        }

        public boolean isExpired() {
            return expired;
        }

        public String getMessage() {
            return message;
        }

        public Map<String, Object> getUserInfo() {
            return userInfo;
        }

        public String getUserId() {
            return userInfo != null ? (String) userInfo.get("userId") : null;
        }

        public String getUsername() {
            return userInfo != null ? (String) userInfo.get("username") : null;
        }

        public String getRoles() {
            return userInfo != null ? (String) userInfo.get("roles") : null;
        }

        public String getPermissions() {
            return userInfo != null ? (String) userInfo.get("permissions") : null;
        }
    }
}
