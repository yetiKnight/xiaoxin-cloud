package com.xiaoxin.iam.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoxin.iam.auth.dto.LoginRequest;
import com.xiaoxin.iam.auth.service.impl.AuthServiceImpl;
import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.common.result.ResultCode;
import com.xiaoxin.iam.common.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 认证控制器测试
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginSuccess() throws Exception {
        // 准备测试数据
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("123456");

        // Mock认证服务返回
        when(authService.login(any(LoginRequest.class))).thenReturn(
            com.xiaoxin.iam.auth.dto.LoginResponse.builder()
                .accessToken("mock-token")
                .refreshToken("mock-refresh-token")
                .tokenType("Bearer")
                .expiresIn(7200L)
                .build()
        );

        // 执行测试
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("mock-token"));
    }

    @Test
    public void testLoginValidation() throws Exception {
        // 准备无效的登录请求
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(""); // 空用户名
        loginRequest.setPassword("123456");

        // 执行测试
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
