package com.xiaoxin.iam.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 提供JWT Token的生成、验证、解析等功能
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * JWT密钥
     */
    @Value("${iam.security.jwt.secret:xiaoxin-iam-jwt-secret-key-2024}")
    private String secret;

    /**
     * JWT过期时间（毫秒）
     */
    @Value("${iam.security.jwt.access-token-expiration:7200000}")
    private Long expiration;

    /**
     * JWT签发者
     */
    @Value("${iam.security.jwt.issuer:xiaoxin-iam}")
    private String issuer;

    /**
     * JWT受众
     */
    @Value("${iam.security.jwt.audience:xiaoxin-iam-client}")
    private String audience;

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成JWT Token
     *
     * @param claims 声明信息
     * @return JWT Token
     */
    public String generateToken(Map<String, Object> claims) {
        return generateToken(claims, expiration);
    }

    /**
     * 生成JWT Token
     *
     * @param claims 声明信息
     * @param expiration 过期时间（毫秒）
     * @return JWT Token
     */
    public String generateToken(Map<String, Object> claims, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .issuer(issuer)
                .audience().add(audience).and()
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 生成用户登录Token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param roles 用户角色列表
     * @return JWT Token
     */
    public String generateUserToken(Long userId, String username, String... roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roles", roles);
        claims.put("type", "access");
        return generateToken(claims);
    }

    /**
     * 生成刷新Token
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 刷新Token
     */
    public String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", "refresh");
        // 刷新Token过期时间更长，默认7天
        return generateToken(claims, 7 * 24 * 60 * 60 * 1000L);
    }

    /**
     * 从Token中获取声明信息
     *
     * @param token JWT Token
     * @return 声明信息
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("JWT Token已过期: {}", e.getMessage());
            throw new RuntimeException("Token已过期", e);
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的JWT Token: {}", e.getMessage());
            throw new RuntimeException("不支持的Token格式", e);
        } catch (MalformedJwtException e) {
            log.warn("JWT Token格式错误: {}", e.getMessage());
            throw new RuntimeException("Token格式错误", e);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.warn("JWT Token签名验证失败: {}", e.getMessage());
            throw new RuntimeException("Token签名验证失败", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT Token参数错误: {}", e.getMessage());
            throw new RuntimeException("Token参数错误", e);
        }
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username", String.class);
    }

    /**
     * 从Token中获取角色列表
     *
     * @param token JWT Token
     * @return 角色列表
     */
    @SuppressWarnings("unchecked")
    public String[] getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object roles = claims.get("roles");
        if (roles instanceof String[]) {
            return (String[]) roles;
        }
        return new String[0];
    }

    /**
     * 验证Token是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            log.debug("Token验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证Token是否过期
     *
     * @param token JWT Token
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取Token过期时间
     *
     * @param token JWT Token
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 刷新Token
     *
     * @param token 原Token
     * @return 新Token
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Long userId = claims.get("userId", Long.class);
            String username = claims.get("username", String.class);
            Object roles = claims.get("roles");
            
            Map<String, Object> newClaims = new HashMap<>();
            newClaims.put("userId", userId);
            newClaims.put("username", username);
            if (roles instanceof String[]) {
                newClaims.put("roles", roles);
            }
            newClaims.put("type", "access");
            
            return generateToken(newClaims);
        } catch (Exception e) {
            log.error("刷新Token失败: {}", e.getMessage());
            throw new RuntimeException("刷新Token失败", e);
        }
    }

    /**
     * 从请求头中提取Token
     *
     * @param authorizationHeader Authorization请求头
     * @return Token字符串
     */
    public String extractTokenFromHeader(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            return null;
        }
        
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        
        return authorizationHeader;
    }

    /**
     * 获取Token剩余有效时间（秒）
     *
     * @param token JWT Token
     * @return 剩余有效时间（秒）
     */
    public long getTokenRemainingTime(String token) {
        try {
            Date expiration = getExpirationFromToken(token);
            long remainingTime = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            return Math.max(0, remainingTime);
        } catch (Exception e) {
            return 0;
        }
    }
}
