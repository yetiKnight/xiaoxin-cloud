package com.xiaoxin.iam.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxin.iam.core.entity.Dept;
import com.xiaoxin.iam.core.entity.Menu;
import com.xiaoxin.iam.core.entity.Permission;
import com.xiaoxin.iam.core.entity.Role;
import com.xiaoxin.iam.core.entity.User;

/**
 * 用户Mapper接口
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID查询用户详细信息（包含角色、部门、权限、菜单）
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User selectUserDetailById(@Param("userId") Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User selectUserByUsername(@Param("username") String username);

    /**
     * 查询用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectUserRoles(@Param("userId") Long userId);

    /**
     * 查询用户部门列表
     *
     * @param userId 用户ID
     * @return 部门列表
     */
    List<Dept> selectUserDepts(@Param("userId") Long userId);

    /**
     * 查询用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> selectUserPermissions(@Param("userId") Long userId);

    /**
     * 查询用户菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectUserMenus(@Param("userId") Long userId);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @param userId 用户ID（排除自己）
     * @return 是否存在
     */
    int checkUsernameExists(@Param("username") String username, @Param("userId") Long userId);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @param userId 用户ID（排除自己）
     * @return 是否存在
     */
    int checkEmailExists(@Param("email") String email, @Param("userId") Long userId);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @param userId 用户ID（排除自己）
     * @return 是否存在
     */
    int checkPhoneExists(@Param("phone") String phone, @Param("userId") Long userId);

    /**
     * 插入用户角色关联
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 影响行数
     */
    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);

    /**
     * 删除用户角色关联
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserRoles(@Param("userId") Long userId);

    /**
     * 插入用户部门关联
     *
     * @param userId 用户ID
     * @param deptIds 部门ID列表
     * @return 影响行数
     */
    int insertUserDepts(@Param("userId") Long userId, @Param("deptIds") List<Long> deptIds);

    /**
     * 删除用户部门关联
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserDepts(@Param("userId") Long userId);
}
