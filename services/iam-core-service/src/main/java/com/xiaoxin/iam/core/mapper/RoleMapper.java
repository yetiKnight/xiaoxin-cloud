package com.xiaoxin.iam.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoxin.iam.core.entity.Dept;
import com.xiaoxin.iam.core.entity.Menu;
import com.xiaoxin.iam.core.entity.Permission;
import com.xiaoxin.iam.core.entity.Role;

/**
 * 角色Mapper接口
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色ID查询角色详细信息（包含权限、菜单、部门）
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    Role selectRoleDetailById(@Param("roleId") Long roleId);

    /**
     * 根据角色名称查询角色
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    Role selectRoleByRoleName(@Param("roleName") String roleName);

    /**
     * 根据角色权限字符串查询角色
     *
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    Role selectRoleByRoleKey(@Param("roleKey") String roleKey);

    /**
     * 查询角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectRolePermissions(@Param("roleId") Long roleId);

    /**
     * 查询角色菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> selectRoleMenus(@Param("roleId") Long roleId);

    /**
     * 查询角色部门列表
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    List<Dept> selectRoleDepts(@Param("roleId") Long roleId);

    /**
     * 检查角色名称是否存在
     *
     * @param roleName 角色名称
     * @param roleId 角色ID（排除自己）
     * @return 是否存在
     */
    int checkRoleNameExists(@Param("roleName") String roleName, @Param("roleId") Long roleId);

    /**
     * 检查角色权限字符串是否存在
     *
     * @param roleKey 角色权限字符串
     * @param roleId 角色ID（排除自己）
     * @return 是否存在
     */
    int checkRoleKeyExists(@Param("roleKey") String roleKey, @Param("roleId") Long roleId);

    /**
     * 查询使用该角色的用户数量
     *
     * @param roleId 角色ID
     * @return 用户数量
     */
    int countUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询使用该角色的部门数量
     *
     * @param roleId 角色ID
     * @return 部门数量
     */
    int countDeptsByRoleId(@Param("roleId") Long roleId);

    /**
     * 插入角色权限关联
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 影响行数
     */
    int insertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 删除角色权限关联
     *
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRolePermissions(@Param("roleId") Long roleId);

    /**
     * 插入角色菜单关联
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 影响行数
     */
    int insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    /**
     * 删除角色菜单关联
     *
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRoleMenus(@Param("roleId") Long roleId);

    /**
     * 插入角色部门关联
     *
     * @param roleId 角色ID
     * @param deptIds 部门ID列表
     * @return 影响行数
     */
    int insertRoleDepts(@Param("roleId") Long roleId, @Param("deptIds") List<Long> deptIds);

    /**
     * 删除角色部门关联
     *
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRoleDepts(@Param("roleId") Long roleId);
}
