package com.xiaoxin.iam.core.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxin.iam.common.exception.BusinessException;
import com.xiaoxin.iam.common.result.ResultCode;
import com.xiaoxin.iam.core.entity.Dept;
import com.xiaoxin.iam.core.entity.Menu;
import com.xiaoxin.iam.core.entity.Permission;
import com.xiaoxin.iam.core.entity.Role;
import com.xiaoxin.iam.core.entity.User;
import com.xiaoxin.iam.common.dto.UserDTO;
import com.xiaoxin.iam.common.dto.LoginInfoUpdateDTO;
import com.xiaoxin.iam.core.mapper.UserMapper;
import com.xiaoxin.iam.core.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户服务实现类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserDetailById(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        User user = userMapper.selectUserDetailById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户名不能为空");
        }
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            return null;
        }
        return convertToUserDTO(user);
    }

    @Override
    public IPage<User> getUserPage(Page<User> page, User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
            queryWrapper.like(User::getUsername, user.getUsername());
        }
        if (user.getNickname() != null && !user.getNickname().trim().isEmpty()) {
            queryWrapper.like(User::getNickname, user.getNickname());
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            queryWrapper.like(User::getEmail, user.getEmail());
        }
        if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
            queryWrapper.like(User::getPhone, user.getPhone());
        }
        if (user.getStatus() != null && !user.getStatus().trim().isEmpty()) {
            queryWrapper.eq(User::getStatus, user.getStatus());
        }
        
        queryWrapper.eq(User::getDelFlag, "0"); // 只查询未删除的用户
        queryWrapper.orderByDesc(User::getCreateTime);
        
        return userMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user) {
        if (user == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户信息不能为空");
        }
        
        // 检查用户名是否已存在
        if (isUsernameExists(user.getUsername(), null)) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        
        // 检查邮箱是否已存在
        if (user.getEmail() != null && isEmailExists(user.getEmail(), null)) {
            throw new BusinessException(ResultCode.EMAIL_EXISTS);
        }
        
        // 检查手机号是否已存在
        if (user.getPhone() != null && isPhoneExists(user.getPhone(), null)) {
            throw new BusinessException(ResultCode.PHONE_EXISTS);
        }
        
        // 加密密码
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus("0"); // 默认正常状态
        }
        if (user.getDelFlag() == null) {
            user.setDelFlag("0"); // 默认未删除
        }
        
        int result = userMapper.insert(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户信息不能为空");
        }
        
        // 检查用户是否存在
        User existUser = userMapper.selectById(user.getId());
        if (existUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 检查用户名是否已存在（排除自己）
        if (user.getUsername() != null && isUsernameExists(user.getUsername(), user.getId())) {
            throw new BusinessException(ResultCode.USERNAME_EXISTS);
        }
        
        // 检查邮箱是否已存在（排除自己）
        if (user.getEmail() != null && isEmailExists(user.getEmail(), user.getId())) {
            throw new BusinessException(ResultCode.EMAIL_EXISTS);
        }
        
        // 检查手机号是否已存在（排除自己）
        if (user.getPhone() != null && isPhoneExists(user.getPhone(), user.getId())) {
            throw new BusinessException(ResultCode.PHONE_EXISTS);
        }
        
        // 如果密码不为空，则加密密码
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 如果密码为空，则不更新密码
            user.setPassword(null);
        }
        
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 逻辑删除
        user.setDelFlag("2");
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID列表不能为空");
        }
        
        for (Long userId : userIds) {
            deleteUser(userId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long userId, String newPassword) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "新密码不能为空");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "旧密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "新密码不能为空");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ResultCode.OLD_PASSWORD_ERROR);
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(Long userId, String status) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "状态不能为空");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setStatus(status);
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 删除原有角色关联
        userMapper.deleteUserRoles(userId);
        
        // 添加新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            userMapper.insertUserRoles(userId, roleIds);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignUserDepts(Long userId, List<Long> deptIds) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 删除原有部门关联
        userMapper.deleteUserDepts(userId);
        
        // 添加新的部门关联
        if (deptIds != null && !deptIds.isEmpty()) {
            userMapper.insertUserDepts(userId, deptIds);
        }
        
        return true;
    }

    @Override
    public List<Role> getUserRoles(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        return userMapper.selectUserRoles(userId);
    }

    @Override
    public List<Dept> getUserDepts(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        return userMapper.selectUserDepts(userId);
    }

    @Override
    public List<Permission> getUserPermissions(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        return userMapper.selectUserPermissions(userId);
    }

    @Override
    public List<Menu> getUserMenus(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        return userMapper.selectUserMenus(userId);
    }

    @Override
    public boolean isUsernameExists(String username, Long userId) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        int count = userMapper.checkUsernameExists(username, userId);
        return count > 0;
    }

    @Override
    public boolean isEmailExists(String email, Long userId) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        int count = userMapper.checkEmailExists(email, userId);
        return count > 0;
    }

    @Override
    public boolean isPhoneExists(String phone, Long userId) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        int count = userMapper.checkPhoneExists(phone, userId);
        return count > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLoginInfo(Long userId, LoginInfoUpdateDTO loginInfo) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "用户ID不能为空");
        }
        
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        user.setLoginIp(loginInfo.getLastLoginIp());
        user.setLoginDate(loginInfo.getLastLoginTime());
        int result = userMapper.updateById(user);
        return result > 0;
    }
    
    /**
     * 转换User实体为UserDTO
     */
    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .sex(user.getSex())
                .avatar(user.getAvatar())
                .password(user.getPassword())
                .status(String.valueOf(user.getStatus()))
                .lastLoginTime(user.getLoginDate())
                .lastLoginIp(user.getLoginIp())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .roles(getUserRoleNames(user.getId()))
                .permissions(getUserPermissionNames(user.getId()))
                .build();
    }
    
    /**
     * 获取用户角色列表（字符串）
     */
    private List<String> getUserRoleNames(Long userId) {
        List<Role> roles = getUserRoles(userId);
        return roles.stream()
                .map(Role::getRoleKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取用户权限列表（字符串）
     */
    private List<String> getUserPermissionNames(Long userId) {
        List<Permission> permissions = getUserPermissions(userId);
        return permissions.stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.toList());
    }
}
