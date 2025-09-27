package com.xiaoxin.iam.auth.client;

import com.xiaoxin.iam.common.dto.LoginInfoUpdateDTO;
import com.xiaoxin.iam.common.dto.UserDTO;
import com.xiaoxin.iam.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 核心服务Feign客户端
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@FeignClient(name = "iam-core-service", path = "/api/v1/internal")
public interface CoreServiceClient {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/users/username/{username}")
    Result<UserDTO> getUserByUsername(@PathVariable("username") String username);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginInfo 登录信息
     * @return 更新结果
     */
    @PostMapping("/users/{userId}/login-info")
    Result<Boolean> updateLoginInfo(@PathVariable("userId") Long userId, @RequestBody LoginInfoUpdateDTO loginInfo);
}
