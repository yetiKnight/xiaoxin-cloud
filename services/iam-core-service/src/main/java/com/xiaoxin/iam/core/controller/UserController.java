package com.xiaoxin.iam.core.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxin.iam.common.result.Result;
import com.xiaoxin.iam.core.dto.UserCreateDTO;
import com.xiaoxin.iam.core.dto.UserUpdateDTO;
import com.xiaoxin.iam.common.dto.UserDTO;
import com.xiaoxin.iam.common.dto.LoginInfoUpdateDTO;
import com.xiaoxin.iam.core.entity.User;
import com.xiaoxin.iam.core.service.UserService;
import com.xiaoxin.iam.core.vo.PageResult;
import com.xiaoxin.iam.core.vo.UserVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户控制器
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详细信息")
    @PreAuthorize("hasAuthority('user.read')")
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        User user = userService.getUserDetailById(userId);
        UserVO userVO = convertToUserVO(user);
        return Result.success(userVO);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名查询用户", description = "根据用户名查询用户信息")
    @PreAuthorize("hasAuthority('user.read')")
    public Result<UserDTO> getUserByUsername(
            @Parameter(description = "用户名", required = true)
            @PathVariable @NotEmpty(message = "用户名不能为空") String username) {
        UserDTO user = userService.getUserByUsername(username);
        return Result.success(user);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表")
    @PreAuthorize("hasAuthority('user.read')")
    public Result<PageResult<UserVO>> getUserPage(
            @Parameter(description = "页码", required = true)
            @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", required = true)
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名")
            @RequestParam(required = false) String username,
            @Parameter(description = "昵称")
            @RequestParam(required = false) String nickname,
            @Parameter(description = "邮箱")
            @RequestParam(required = false) String email,
            @Parameter(description = "手机号")
            @RequestParam(required = false) String phone,
            @Parameter(description = "状态")
            @RequestParam(required = false) String status) {
        
        Page<User> page = new Page<>(current, size);
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus(status);
        
        IPage<User> result = userService.getUserPage(page, user);
        PageResult<UserVO> pageResult = convertToPageResult(result);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    @PreAuthorize("hasAuthority('user.create')")
    public Result<Boolean> createUser(
            @Parameter(description = "用户信息", required = true)
            @RequestBody @Valid UserCreateDTO userCreateDTO) {
        User user = convertToUser(userCreateDTO);
        boolean success = userService.createUser(user);
        return Result.success(success);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    @PreAuthorize("hasAuthority('user.update')")
    public Result<Boolean> updateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "用户信息", required = true)
            @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        userUpdateDTO.setId(userId);
        User user = convertToUser(userUpdateDTO);
        boolean success = userService.updateUser(user);
        return Result.success(success);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @PreAuthorize("hasAuthority('user.delete')")
    public Result<Boolean> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        boolean success = userService.deleteUser(userId);
        return Result.success(success);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除用户")
    @PreAuthorize("hasAuthority('user.delete')")
    public Result<Boolean> deleteUsers(
            @Parameter(description = "用户ID列表", required = true)
            @RequestBody @NotEmpty(message = "用户ID列表不能为空") List<Long> userIds) {
        boolean success = userService.deleteUsers(userIds);
        return Result.success(success);
    }

    @PutMapping("/{userId}/password/reset")
    @Operation(summary = "重置用户密码", description = "重置用户密码")
    @PreAuthorize("hasAuthority('user.update')")
    public Result<Boolean> resetPassword(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "新密码", required = true)
            @RequestParam @NotEmpty(message = "新密码不能为空") String newPassword) {
        boolean success = userService.resetPassword(userId, newPassword);
        return Result.success(success);
    }

    @PutMapping("/{userId}/password/change")
    @Operation(summary = "修改用户密码", description = "修改用户密码")
    public Result<Boolean> changePassword(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "旧密码", required = true)
            @RequestParam @NotEmpty(message = "旧密码不能为空") String oldPassword,
            @Parameter(description = "新密码", required = true)
            @RequestParam @NotEmpty(message = "新密码不能为空") String newPassword) {
        boolean success = userService.changePassword(userId, oldPassword, newPassword);
        return Result.success(success);
    }

    @PutMapping("/{userId}/status")
    @Operation(summary = "更新用户状态", description = "更新用户状态")
    @PreAuthorize("hasAuthority('user.update')")
    public Result<Boolean> updateUserStatus(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "状态", required = true)
            @RequestParam @NotEmpty(message = "状态不能为空") String status) {
        boolean success = userService.updateUserStatus(userId, status);
        return Result.success(success);
    }

    @PutMapping("/{userId}/roles")
    @Operation(summary = "分配用户角色", description = "分配用户角色")
    @PreAuthorize("hasAuthority('user.update')")
    public Result<Boolean> assignUserRoles(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "角色ID列表", required = true)
            @RequestBody List<Long> roleIds) {
        boolean success = userService.assignUserRoles(userId, roleIds);
        return Result.success(success);
    }

    @PutMapping("/{userId}/depts")
    @Operation(summary = "分配用户部门", description = "分配用户部门")
    @PreAuthorize("hasAuthority('user.update')")
    public Result<Boolean> assignUserDepts(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "部门ID列表", required = true)
            @RequestBody List<Long> deptIds) {
        boolean success = userService.assignUserDepts(userId, deptIds);
        return Result.success(success);
    }

    @GetMapping("/{userId}/roles")
    @Operation(summary = "查询用户角色", description = "查询用户角色列表")
    @PreAuthorize("hasAuthority('user.read')")
    public Result<List<com.xiaoxin.iam.core.entity.Role>> getUserRoles(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<com.xiaoxin.iam.core.entity.Role> roles = userService.getUserRoles(userId);
        return Result.success(roles);
    }

    @GetMapping("/{userId}/depts")
    @Operation(summary = "查询用户部门", description = "查询用户部门列表")
    @PreAuthorize("hasAuthority('user.read')")
    public Result<List<com.xiaoxin.iam.core.entity.Dept>> getUserDepts(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<com.xiaoxin.iam.core.entity.Dept> depts = userService.getUserDepts(userId);
        return Result.success(depts);
    }

    @GetMapping("/{userId}/permissions")
    @Operation(summary = "查询用户权限", description = "查询用户权限列表")
    @PreAuthorize("hasAuthority('user.read')")
    public Result<List<com.xiaoxin.iam.core.entity.Permission>> getUserPermissions(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<com.xiaoxin.iam.core.entity.Permission> permissions = userService.getUserPermissions(userId);
        return Result.success(permissions);
    }

    @GetMapping("/{userId}/menus")
    @Operation(summary = "查询用户菜单", description = "查询用户菜单列表")
    @PreAuthorize("hasAuthority('user.read')")
    public Result<List<com.xiaoxin.iam.core.entity.Menu>> getUserMenus(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId) {
        List<com.xiaoxin.iam.core.entity.Menu> menus = userService.getUserMenus(userId);
        return Result.success(menus);
    }

    @PostMapping("/{userId}/login-info")
    @Operation(summary = "更新用户登录信息", description = "更新用户登录信息")
    public Result<Boolean> updateLoginInfo(
            @Parameter(description = "用户ID", required = true)
            @PathVariable @NotNull(message = "用户ID不能为空") Long userId,
            @Parameter(description = "登录信息", required = true)
            @RequestBody LoginInfoUpdateDTO loginInfo) {
        boolean success = userService.updateLoginInfo(userId, loginInfo);
        return Result.success(success);
    }

    @GetMapping("/check/username")
    @Operation(summary = "检查用户名是否存在", description = "检查用户名是否存在")
    public Result<Boolean> checkUsernameExists(
            @Parameter(description = "用户名", required = true)
            @RequestParam @NotEmpty(message = "用户名不能为空") String username,
            @Parameter(description = "用户ID（排除自己）")
            @RequestParam(required = false) Long userId) {
        boolean exists = userService.isUsernameExists(username, userId);
        return Result.success(exists);
    }

    @GetMapping("/check/email")
    @Operation(summary = "检查邮箱是否存在", description = "检查邮箱是否存在")
    public Result<Boolean> checkEmailExists(
            @Parameter(description = "邮箱", required = true)
            @RequestParam @NotEmpty(message = "邮箱不能为空") String email,
            @Parameter(description = "用户ID（排除自己）")
            @RequestParam(required = false) Long userId) {
        boolean exists = userService.isEmailExists(email, userId);
        return Result.success(exists);
    }

    @GetMapping("/check/phone")
    @Operation(summary = "检查手机号是否存在", description = "检查手机号是否存在")
    public Result<Boolean> checkPhoneExists(
            @Parameter(description = "手机号", required = true)
            @RequestParam @NotEmpty(message = "手机号不能为空") String phone,
            @Parameter(description = "用户ID（排除自己）")
            @RequestParam(required = false) Long userId) {
        boolean exists = userService.isPhoneExists(phone, userId);
        return Result.success(exists);
    }

    /**
     * 将User实体转换为UserVO
     */
    private UserVO convertToUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        userVO.setSex(user.getSex());
        userVO.setAvatar(user.getAvatar());
        userVO.setStatus(user.getStatus());
        userVO.setLoginIp(user.getLoginIp());
        userVO.setLoginDate(user.getLoginDate());
        userVO.setRemark(user.getRemark());
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        userVO.setCreateBy(user.getCreateBy());
        userVO.setUpdateBy(user.getUpdateBy());
        return userVO;
    }

    /**
     * 将UserCreateDTO转换为User实体
     */
    private User convertToUser(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setNickname(userCreateDTO.getNickname());
        user.setEmail(userCreateDTO.getEmail());
        user.setPhone(userCreateDTO.getPhone());
        user.setSex(userCreateDTO.getSex());
        user.setAvatar(userCreateDTO.getAvatar());
        user.setPassword(userCreateDTO.getPassword());
        user.setRemark(userCreateDTO.getRemark());
        return user;
    }

    /**
     * 将UserUpdateDTO转换为User实体
     */
    private User convertToUser(UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userUpdateDTO.getId());
        user.setUsername(userUpdateDTO.getUsername());
        user.setNickname(userUpdateDTO.getNickname());
        user.setEmail(userUpdateDTO.getEmail());
        user.setPhone(userUpdateDTO.getPhone());
        user.setSex(userUpdateDTO.getSex());
        user.setAvatar(userUpdateDTO.getAvatar());
        user.setPassword(userUpdateDTO.getPassword());
        user.setRemark(userUpdateDTO.getRemark());
        return user;
    }

    /**
     * 将IPage<User>转换为PageResult<UserVO>
     */
    private PageResult<UserVO> convertToPageResult(IPage<User> page) {
        if (page == null) {
            return null;
        }
        
        List<UserVO> userVOs = page.getRecords().stream()
                .map(this::convertToUserVO)
                .collect(java.util.stream.Collectors.toList());
        
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), userVOs);
    }
}
