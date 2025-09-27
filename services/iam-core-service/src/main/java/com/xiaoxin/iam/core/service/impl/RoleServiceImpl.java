package com.xiaoxin.iam.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.xiaoxin.iam.core.mapper.RoleMapper;
import com.xiaoxin.iam.core.service.RoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * 角色服务实现类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role getRoleDetailById(Long roleId) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        Role role = roleMapper.selectRoleDetailById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        return role;
    }

    @Override
    public Role getRoleByRoleName(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色名称不能为空");
        }
        Role role = roleMapper.selectRoleByRoleName(roleName);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        return role;
    }

    @Override
    public Role getRoleByRoleKey(String roleKey) {
        if (roleKey == null || roleKey.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色权限字符串不能为空");
        }
        Role role = roleMapper.selectRoleByRoleKey(roleKey);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        return role;
    }

    @Override
    public IPage<Role> getRolePage(Page<Role> page, Role role) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        
        if (role.getRoleName() != null && !role.getRoleName().trim().isEmpty()) {
            queryWrapper.like(Role::getRoleName, role.getRoleName());
        }
        if (role.getRoleKey() != null && !role.getRoleKey().trim().isEmpty()) {
            queryWrapper.like(Role::getRoleKey, role.getRoleKey());
        }
        if (role.getStatus() != null && !role.getStatus().trim().isEmpty()) {
            queryWrapper.eq(Role::getStatus, role.getStatus());
        }
        
        queryWrapper.eq(Role::getDelFlag, "0"); // 只查询未删除的角色
        queryWrapper.orderByAsc(Role::getRoleSort).orderByDesc(Role::getCreateTime);
        
        return roleMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        if (role == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色信息不能为空");
        }
        
        // 检查角色名称是否已存在
        if (isRoleNameExists(role.getRoleName(), null)) {
            throw new BusinessException(ResultCode.ROLE_NAME_EXISTS);
        }
        
        // 检查角色权限字符串是否已存在
        if (isRoleKeyExists(role.getRoleKey(), null)) {
            throw new BusinessException(ResultCode.ROLE_KEY_EXISTS);
        }
        
        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus("0"); // 默认正常状态
        }
        if (role.getDelFlag() == null) {
            role.setDelFlag("0"); // 默认未删除
        }
        if (role.getRoleSort() == null) {
            role.setRoleSort(0); // 默认排序
        }
        if (role.getMenuCheckStrictly() == null) {
            role.setMenuCheckStrictly(true); // 默认菜单树选择项关联显示
        }
        if (role.getDeptCheckStrictly() == null) {
            role.setDeptCheckStrictly(true); // 默认部门树选择项关联显示
        }
        if (role.getDataScope() == null) {
            role.setDataScope("1"); // 默认全部数据权限
        }
        
        int result = roleMapper.insert(role);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        if (role == null || role.getId() == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色信息不能为空");
        }
        
        // 检查角色是否存在
        Role existRole = roleMapper.selectById(role.getId());
        if (existRole == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        // 检查角色名称是否已存在（排除自己）
        if (role.getRoleName() != null && isRoleNameExists(role.getRoleName(), role.getId())) {
            throw new BusinessException(ResultCode.ROLE_NAME_EXISTS);
        }
        
        // 检查角色权限字符串是否已存在（排除自己）
        if (role.getRoleKey() != null && isRoleKeyExists(role.getRoleKey(), role.getId())) {
            throw new BusinessException(ResultCode.ROLE_KEY_EXISTS);
        }
        
        int result = roleMapper.updateById(role);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        // 检查是否有用户使用该角色
        int userCount = roleMapper.countUsersByRoleId(roleId);
        if (userCount > 0) {
            throw new BusinessException(ResultCode.ROLE_HAS_USERS);
        }
        
        // 逻辑删除
        role.setDelFlag("2");
        int result = roleMapper.updateById(role);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID列表不能为空");
        }
        
        for (Long roleId : roleIds) {
            deleteRole(roleId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleStatus(Long roleId, String status) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "状态不能为空");
        }
        
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        role.setStatus(status);
        int result = roleMapper.updateById(role);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRolePermissions(Long roleId, List<Long> permissionIds) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        // 删除原有权限关联
        roleMapper.deleteRolePermissions(roleId);
        
        // 添加新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            roleMapper.insertRolePermissions(roleId, permissionIds);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleMenus(Long roleId, List<Long> menuIds) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        // 删除原有菜单关联
        roleMapper.deleteRoleMenus(roleId);
        
        // 添加新的菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            roleMapper.insertRoleMenus(roleId, menuIds);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleDepts(Long roleId, List<Long> deptIds) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        
        // 删除原有部门关联
        roleMapper.deleteRoleDepts(roleId);
        
        // 添加新的部门关联
        if (deptIds != null && !deptIds.isEmpty()) {
            roleMapper.insertRoleDepts(roleId, deptIds);
        }
        
        return true;
    }

    @Override
    public List<Permission> getRolePermissions(Long roleId) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        return roleMapper.selectRolePermissions(roleId);
    }

    @Override
    public List<Menu> getRoleMenus(Long roleId) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        return roleMapper.selectRoleMenus(roleId);
    }

    @Override
    public List<Dept> getRoleDepts(Long roleId) {
        if (roleId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "角色ID不能为空");
        }
        return roleMapper.selectRoleDepts(roleId);
    }

    @Override
    public int countUsersByRoleId(Long roleId) {
        if (roleId == null) {
            return 0;
        }
        return roleMapper.countUsersByRoleId(roleId);
    }

    @Override
    public int countDeptsByRoleId(Long roleId) {
        if (roleId == null) {
            return 0;
        }
        return roleMapper.countDeptsByRoleId(roleId);
    }

    @Override
    public boolean isRoleNameExists(String roleName, Long roleId) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return false;
        }
        int count = roleMapper.checkRoleNameExists(roleName, roleId);
        return count > 0;
    }

    @Override
    public boolean isRoleKeyExists(String roleKey, Long roleId) {
        if (roleKey == null || roleKey.trim().isEmpty()) {
            return false;
        }
        int count = roleMapper.checkRoleKeyExists(roleKey, roleId);
        return count > 0;
    }
}
