package com.xiaoxin.iam.core.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxin.iam.core.entity.Dept;
import com.xiaoxin.iam.core.entity.Menu;
import com.xiaoxin.iam.core.entity.Permission;
import com.xiaoxin.iam.core.entity.Role;

/**
 * 角色服务接口
 *
 * @author xiaoxin
 * @since 1.0.0
 */
public interface RoleService {

    /**
     * 根据角色ID查询角色详细信息
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    Role getRoleDetailById(Long roleId);

    /**
     * 根据角色名称查询角色
     *
     * @param roleName 角色名称
     * @return 角色信息
     */
    Role getRoleByRoleName(String roleName);

    /**
     * 根据角色权限字符串查询角色
     *
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    Role getRoleByRoleKey(String roleKey);

    /**
     * 分页查询角色列表
     *
     * @param page 分页参数
     * @param role 查询条件
     * @return 角色分页列表
     */
    IPage<Role> getRolePage(Page<Role> page, Role role);

    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean createRole(Role role);

    /**
     * 更新角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(Role role);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteRole(Long roleId);

    /**
     * 批量删除角色
     *
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean deleteRoles(List<Long> roleIds);

    /**
     * 更新角色状态
     *
     * @param roleId 角色ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateRoleStatus(Long roleId, String status);

    /**
     * 分配角色权限
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    boolean assignRolePermissions(Long roleId, List<Long> permissionIds);

    /**
     * 分配角色菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    boolean assignRoleMenus(Long roleId, List<Long> menuIds);

    /**
     * 分配角色部门
     *
     * @param roleId 角色ID
     * @param deptIds 部门ID列表
     * @return 是否成功
     */
    boolean assignRoleDepts(Long roleId, List<Long> deptIds);

    /**
     * 查询角色权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getRolePermissions(Long roleId);

    /**
     * 查询角色菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> getRoleMenus(Long roleId);

    /**
     * 查询角色部门
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    List<Dept> getRoleDepts(Long roleId);

    /**
     * 查询使用该角色的用户数量
     *
     * @param roleId 角色ID
     * @return 用户数量
     */
    int countUsersByRoleId(Long roleId);

    /**
     * 查询使用该角色的部门数量
     *
     * @param roleId 角色ID
     * @return 部门数量
     */
    int countDeptsByRoleId(Long roleId);

    /**
     * 检查角色名称是否存在
     *
     * @param roleName 角色名称
     * @param roleId 角色ID（排除自己）
     * @return 是否存在
     */
    boolean isRoleNameExists(String roleName, Long roleId);

    /**
     * 检查角色权限字符串是否存在
     *
     * @param roleKey 角色权限字符串
     * @param roleId 角色ID（排除自己）
     * @return 是否存在
     */
    boolean isRoleKeyExists(String roleKey, Long roleId);
}
