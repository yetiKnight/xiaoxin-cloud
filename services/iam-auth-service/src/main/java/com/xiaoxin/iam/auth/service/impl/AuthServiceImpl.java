package com.xiaoxin.iam.auth.service.impl;

import com.xiaoxin.iam.auth.client.CoreServiceClient;
import com.xiaoxin.iam.auth.dto.LoginRequest;
import com.xiaoxin.iam.auth.dto.LoginResponse;
import com.xiaoxin.iam.common.exception.AuthException;
import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.common.result.ResultCode;
import com.xiaoxin.iam.common.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

import com.xiaoxin.iam.common.dto.UserDTO;
import com.xiaoxin.iam.common.dto.LoginInfoUpdateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 认证服务实现类
 * 简化版本，专注于JWT令牌管理
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final JwtUtils jwtUtils;
    private final CoreServiceClient coreServiceClient;
    private final PasswordEncoder passwordEncoder;


    @Value("${iam.security.jwt.access-token-expiration:7200000}")
    private Long tokenExpiration;

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        log.info("用户登录请求: {}", username);
        
        try {
            // 1. 查询用户信息
            Result<UserDTO> userResult = coreServiceClient.getUserByUsername(username);
            if (!userResult.isSuccess() || userResult.getData() == null) {
                throw new AuthException(ResultCode.USER_NOT_FOUND.getCode(), "用户不存在");
            }
            
            UserDTO user = userResult.getData();
            
            // 2. 检查用户状态
            if (user.getStatus() == null || !"0".equals(user.getStatus())) {
                if ("1".equals(user.getStatus())) {
                    throw new AuthException(ResultCode.USER_DISABLED.getCode(), "用户已被停用");
                }
                if ("2".equals(user.getStatus())) {
                    throw new AuthException(ResultCode.USER_NOT_FOUND.getCode(), "用户不存在");
                }
            }
            
            // 3. 验证密码
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new AuthException(ResultCode.PASSWORD_ERROR.getCode(), "密码错误");
            }
            
            // 4. 生成JWT令牌
            String[] roles = getUserRoles(user);
            String accessToken = jwtUtils.generateUserToken(user.getId(), user.getUsername(), roles);
            String refreshToken = jwtUtils.generateRefreshToken(user.getId(), user.getUsername());
            
            // 5. 更新用户登录信息
            updateUserLoginInfo(user.getId(), loginRequest.getLoginIp());
            
            // 6. 构建用户信息
            LoginResponse.UserInfo userInfo = buildUserInfo(user);
            
            
            log.info("用户登录成功: {}", username);
            
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(tokenExpiration / 1000)
                    .userInfo(userInfo)
                    .build();
                    
        } catch (AuthException e) {
            log.error("用户登录失败: {}", username, e);
            throw e;
        } catch (Exception e) {
            log.error("用户登录异常: {}", username, e);
            throw new AuthException(ResultCode.ERROR.getCode(), "登录失败");
        }
    }
    
    /**
     * 获取用户角色列表
     */
    private String[] getUserRoles(UserDTO user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return new String[0];
        }
        return user.getRoles().toArray(new String[0]);
    }
    
    /**
     * 更新用户登录信息
     */
    private void updateUserLoginInfo(Long userId, String loginIp) {
        try {
            LoginInfoUpdateDTO loginInfo = LoginInfoUpdateDTO.builder()
                    .lastLoginTime(LocalDateTime.now())
                    .lastLoginIp(loginIp)
                    .loginType("password")
                    .build();
            coreServiceClient.updateLoginInfo(userId, loginInfo);
        } catch (Exception e) {
            log.warn("更新用户登录信息失败: userId={}", userId, e);
        }
    }
    
    /**
     * 构建用户信息
     */
    private LoginResponse.UserInfo buildUserInfo(UserDTO user) {
        List<String> roles = user.getRoles() != null ? user.getRoles() : Collections.emptyList();
        List<String> permissions = user.getPermissions() != null ? user.getPermissions() : Collections.emptyList();
        
        return LoginResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .sex(user.getSex())
                .avatar(user.getAvatar())
                .roles(roles)
                .permissions(permissions)
                .lastLoginTime(user.getLastLoginTime())
                .lastLoginIp(user.getLastLoginIp())
                .build();
    }

    public void logout(String token) {
        log.info("用户登出请求");
        
        // 这里可以实现令牌黑名单机制
        // 将令牌加入黑名单，使其失效
        // 由于JWT是无状态的，这里可以记录到Redis中
        
        log.info("用户登出成功");
    }

    public String refreshToken(String refreshToken) {
        log.info("刷新令牌请求");
        
        // 验证刷新令牌
        if (!jwtUtils.validateToken(refreshToken)) {
            throw AuthException.tokenInvalid();
        }

        // 获取用户信息
        Long userId = jwtUtils.getUserIdFromToken(refreshToken);
        String username = jwtUtils.getUsernameFromToken(refreshToken);

        // 生成新的访问令牌
        String newAccessToken = jwtUtils.generateUserToken(userId, username, new String[0]);
        
        log.info("令牌刷新成功: {}", username);
        return newAccessToken;
    }

    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }
}
