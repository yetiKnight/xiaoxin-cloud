package com.xiaoxin.iam.core.controller;

import com.xiaoxin.iam.common.dto.LoginInfoUpdateDTO;
import com.xiaoxin.iam.common.dto.UserDTO;
import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.core.constant.CoreConstants;
import com.xiaoxin.iam.core.entity.User;
import com.xiaoxin.iam.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 内部用户控制器
 * 用于服务间调用的内部API
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(CoreConstants.INTERNAL_USERS_PATH)
@RequiredArgsConstructor
@Validated
@Tag(name = "内部用户API", description = "服务间调用的用户相关接口")
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户", description = "根据用户名查询用户信息（内部API）")
    public Result<UserDTO> getUserByUsername(
            @Parameter(description = "用户名", required = true)
            @PathVariable @NotEmpty(message = "用户名不能为空") String username) {
        log.debug("内部API调用：根据用户名查询用户，username={}", username);
        UserDTO user = userService.getUserByUsername(username);
        return Result.success(user);
    }

    @PostMapping("/{userId}/login-info")
    @Operation(summary = "更新用户登录信息", description = "更新用户登录信息（内部API）")
    public Result<Boolean> updateLoginInfo(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "登录信息", required = true)
            @RequestBody LoginInfoUpdateDTO loginInfo) {
        log.debug("内部API调用：更新用户登录信息，userId={}", userId);
        boolean success = userService.updateLoginInfo(userId, loginInfo);
        return Result.success(success);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详细信息（内部API）")
    public Result<UserDTO> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        log.debug("内部API调用：根据ID查询用户，userId={}", userId);
        User user = userService.getUserDetailById(userId);
        UserDTO userDTO = convertToUserDTO(user);
        return Result.success(userDTO);
    }

    @GetMapping("/{userId}/roles")
    @Operation(summary = "查询用户角色", description = "查询用户角色列表（内部API）")
    public Result<java.util.List<com.xiaoxin.iam.core.entity.Role>> getUserRoles(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        log.debug("内部API调用：查询用户角色，userId={}", userId);
        java.util.List<com.xiaoxin.iam.core.entity.Role> roles = userService.getUserRoles(userId);
        return Result.success(roles);
    }

    @GetMapping("/{userId}/permissions")
    @Operation(summary = "查询用户权限", description = "查询用户权限列表（内部API）")
    public Result<java.util.List<com.xiaoxin.iam.core.entity.Permission>> getUserPermissions(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        log.debug("内部API调用：查询用户权限，userId={}", userId);
        java.util.List<com.xiaoxin.iam.core.entity.Permission> permissions = userService.getUserPermissions(userId);
        return Result.success(permissions);
    }

    @GetMapping("/{userId}/menus")
    @Operation(summary = "查询用户菜单", description = "查询用户菜单列表（内部API）")
    public Result<java.util.List<com.xiaoxin.iam.core.entity.Menu>> getUserMenus(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        log.debug("内部API调用：查询用户菜单，userId={}", userId);
        java.util.List<com.xiaoxin.iam.core.entity.Menu> menus = userService.getUserMenus(userId);
        return Result.success(menus);
    }

    @GetMapping("/check/username")
    @Operation(summary = "检查用户名是否存在", description = "检查用户名是否存在（内部API）")
    public Result<Boolean> checkUsernameExists(
            @Parameter(description = "用户名", required = true)
            @RequestParam @NotEmpty(message = "用户名不能为空") String username,
            @Parameter(description = "用户ID（排除自己）")
            @RequestParam(required = false) Long userId) {
        log.debug("内部API调用：检查用户名是否存在，username={}, userId={}", username, userId);
        boolean exists = userService.isUsernameExists(username, userId);
        return Result.success(exists);
    }

    @GetMapping("/check/email")
    @Operation(summary = "检查邮箱是否存在", description = "检查邮箱是否存在（内部API）")
    public Result<Boolean> checkEmailExists(
            @Parameter(description = "邮箱", required = true)
            @RequestParam @NotEmpty(message = "邮箱不能为空") String email,
            @Parameter(description = "用户ID（排除自己）")
            @RequestParam(required = false) Long userId) {
        log.debug("内部API调用：检查邮箱是否存在，email={}, userId={}", email, userId);
        boolean exists = userService.isEmailExists(email, userId);
        return Result.success(exists);
    }

    @GetMapping("/check/phone")
    @Operation(summary = "检查手机号是否存在", description = "检查手机号是否存在（内部API）")
    public Result<Boolean> checkPhoneExists(
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotEmpty(message = "手机号不能为空") String phone,
            @Parameter(description = "用户ID（排除自己）")
            @RequestParam(required = false) Long userId) {
        log.debug("内部API调用：检查手机号是否存在，phone={}, userId={}", phone, userId);
        boolean exists = userService.isPhoneExists(phone, userId);
        return Result.success(exists);
    }

    /**
     * 将User实体转换为UserDTO
     */
    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setNickname(user.getNickname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setSex(user.getSex());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setPassword(user.getPassword());
        userDTO.setStatus(user.getStatus());
        userDTO.setLastLoginTime(user.getLoginDate());
        userDTO.setLastLoginIp(user.getLoginIp());
        userDTO.setCreateTime(user.getCreateTime());
        userDTO.setUpdateTime(user.getUpdateTime());
        return userDTO;
    }
}
