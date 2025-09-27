package com.xiaoxin.iam.core.service;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxin.iam.core.entity.Dept;
import com.xiaoxin.iam.core.entity.Menu;
import com.xiaoxin.iam.core.entity.Permission;
import com.xiaoxin.iam.core.entity.Role;
import com.xiaoxin.iam.core.entity.User;
import com.xiaoxin.iam.common.dto.UserDTO;
import com.xiaoxin.iam.common.dto.LoginInfoUpdateDTO;

/**
 * 用户服务接口
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据用户ID查询用户详细信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserDetailById(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserDTO getUserByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param user 查询条件
     * @return 用户分页列表
     */
    IPage<User> getUserPage(Page<User> page, User user);

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean createUser(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     * @return 是否成功
     */
    boolean deleteUsers(List<Long> userIds);

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 修改用户密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(Long userId, String status);

    /**
     * 分配用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignUserRoles(Long userId, List<Long> roleIds);

    /**
     * 分配用户部门
     *
     * @param userId 用户ID
     * @param deptIds 部门ID列表
     * @return 是否成功
     */
    boolean assignUserDepts(Long userId, List<Long> deptIds);

    /**
     * 查询用户角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getUserRoles(Long userId);

    /**
     * 查询用户部门
     *
     * @param userId 用户ID
     * @return 部门列表
     */
    List<Dept> getUserDepts(Long userId);

    /**
     * 查询用户权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> getUserPermissions(Long userId);

    /**
     * 查询用户菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> getUserMenus(Long userId);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param userId 用户ID（排除自己）
     * @return 是否存在
     */
    boolean isUsernameExists(String username, Long userId);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @param userId 用户ID（排除自己）
     * @return 是否存在
     */
    boolean isEmailExists(String email, Long userId);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @param userId 用户ID（排除自己）
     * @return 是否存在
     */
    boolean isPhoneExists(String phone, Long userId);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginInfo 登录信息
     * @return 是否成功
     */
    boolean updateLoginInfo(Long userId, LoginInfoUpdateDTO loginInfo);
}
