package com.xiaoxin.iam.core.controller;

import java.util.List;

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
import com.xiaoxin.iam.core.dto.RoleCreateDTO;
import com.xiaoxin.iam.core.dto.RoleUpdateDTO;
import com.xiaoxin.iam.core.entity.Role;
import com.xiaoxin.iam.core.service.RoleService;
import com.xiaoxin.iam.core.vo.PageResult;
import com.xiaoxin.iam.core.vo.RoleVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色控制器
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Validated
@Tag(name = "角色管理", description = "角色相关接口")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/{roleId}")
    @Operation(summary = "根据ID查询角色", description = "根据角色ID查询角色详细信息")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<RoleVO> getRoleById(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        Role role = roleService.getRoleDetailById(roleId);
        RoleVO roleVO = convertToRoleVO(role);
        return Result.success(roleVO);
    }

    @GetMapping("/name/{roleName}")
    @Operation(summary = "根据角色名称查询角色", description = "根据角色名称查询角色信息")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<Role> getRoleByRoleName(
            @Parameter(description = "角色名称", required = true)
            @PathVariable @NotEmpty(message = "角色名称不能为空") String roleName) {
        Role role = roleService.getRoleByRoleName(roleName);
        return Result.success(role);
    }

    @GetMapping("/key/{roleKey}")
    @Operation(summary = "根据角色权限字符串查询角色", description = "根据角色权限字符串查询角色信息")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<Role> getRoleByRoleKey(
            @Parameter(description = "角色权限字符串", required = true)
            @PathVariable @NotEmpty(message = "角色权限字符串不能为空") String roleKey) {
        Role role = roleService.getRoleByRoleKey(roleKey);
        return Result.success(role);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询角色列表", description = "分页查询角色列表")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<PageResult<RoleVO>> getRolePage(
            @Parameter(description = "页码", required = true)
            @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", required = true)
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "角色名称")
            @RequestParam(required = false) String roleName,
            @Parameter(description = "角色权限字符串")
            @RequestParam(required = false) String roleKey,
            @Parameter(description = "状态")
            @RequestParam(required = false) String status) {
        
        Page<Role> page = new Page<>(current, size);
        Role role = new Role();
        role.setRoleName(roleName);
        role.setRoleKey(roleKey);
        role.setStatus(status);
        
        IPage<Role> result = roleService.getRolePage(page, role);
        PageResult<RoleVO> pageResult = convertToPageResult(result);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "创建角色", description = "创建新角色")
    @PreAuthorize("hasAuthority('role.create')")
    public Result<Boolean> createRole(
            @Parameter(description = "角色信息", required = true)
            @RequestBody @Valid RoleCreateDTO roleCreateDTO) {
        Role role = convertToRole(roleCreateDTO);
        boolean success = roleService.createRole(role);
        return Result.success(success);
    }

    @PutMapping("/{roleId}")
    @Operation(summary = "更新角色", description = "更新角色信息")
    @PreAuthorize("hasAuthority('role.update')")
    public Result<Boolean> updateRole(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId,
            @Parameter(description = "角色信息", required = true)
            @RequestBody @Valid RoleUpdateDTO roleUpdateDTO) {
        roleUpdateDTO.setId(roleId);
        Role role = convertToRole(roleUpdateDTO);
        boolean success = roleService.updateRole(role);
        return Result.success(success);
    }

    @DeleteMapping("/{roleId}")
    @Operation(summary = "删除角色", description = "根据ID删除角色")
    @PreAuthorize("hasAuthority('role.delete')")
    public Result<Boolean> deleteRole(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        boolean success = roleService.deleteRole(roleId);
        return Result.success(success);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "批量删除角色")
    @PreAuthorize("hasAuthority('role.delete')")
    public Result<Boolean> deleteRoles(
            @Parameter(description = "角色ID列表", required = true)
            @RequestBody @NotEmpty(message = "角色ID列表不能为空") List<Long> roleIds) {
        boolean success = roleService.deleteRoles(roleIds);
        return Result.success(success);
    }

    @PutMapping("/{roleId}/status")
    @Operation(summary = "更新角色状态", description = "更新角色状态")
    @PreAuthorize("hasAuthority('role.update')")
    public Result<Boolean> updateRoleStatus(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId,
            @Parameter(description = "状态", required = true)
            @RequestParam @NotEmpty(message = "状态不能为空") String status) {
        boolean success = roleService.updateRoleStatus(roleId, status);
        return Result.success(success);
    }

    @PutMapping("/{roleId}/permissions")
    @Operation(summary = "分配角色权限", description = "分配角色权限")
    @PreAuthorize("hasAuthority('role.update')")
    public Result<Boolean> assignRolePermissions(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId,
            @Parameter(description = "权限ID列表", required = true)
            @RequestBody List<Long> permissionIds) {
        boolean success = roleService.assignRolePermissions(roleId, permissionIds);
        return Result.success(success);
    }

    @PutMapping("/{roleId}/menus")
    @Operation(summary = "分配角色菜单", description = "分配角色菜单")
    @PreAuthorize("hasAuthority('role.update')")
    public Result<Boolean> assignRoleMenus(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId,
            @Parameter(description = "菜单ID列表", required = true)
            @RequestBody List<Long> menuIds) {
        boolean success = roleService.assignRoleMenus(roleId, menuIds);
        return Result.success(success);
    }

    @PutMapping("/{roleId}/depts")
    @Operation(summary = "分配角色部门", description = "分配角色部门")
    @PreAuthorize("hasAuthority('role.update')")
    public Result<Boolean> assignRoleDepts(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId,
            @Parameter(description = "部门ID列表", required = true)
            @RequestBody List<Long> deptIds) {
        boolean success = roleService.assignRoleDepts(roleId, deptIds);
        return Result.success(success);
    }

    @GetMapping("/{roleId}/permissions")
    @Operation(summary = "查询角色权限", description = "查询角色权限列表")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<List<com.xiaoxin.iam.core.entity.Permission>> getRolePermissions(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        List<com.xiaoxin.iam.core.entity.Permission> permissions = roleService.getRolePermissions(roleId);
        return Result.success(permissions);
    }

    @GetMapping("/{roleId}/menus")
    @Operation(summary = "查询角色菜单", description = "查询角色菜单列表")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<List<com.xiaoxin.iam.core.entity.Menu>> getRoleMenus(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        List<com.xiaoxin.iam.core.entity.Menu> menus = roleService.getRoleMenus(roleId);
        return Result.success(menus);
    }

    @GetMapping("/{roleId}/depts")
    @Operation(summary = "查询角色部门", description = "查询角色部门列表")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<List<com.xiaoxin.iam.core.entity.Dept>> getRoleDepts(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        List<com.xiaoxin.iam.core.entity.Dept> depts = roleService.getRoleDepts(roleId);
        return Result.success(depts);
    }

    @GetMapping("/{roleId}/users/count")
    @Operation(summary = "查询角色用户数量", description = "查询使用该角色的用户数量")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<Integer> countUsersByRoleId(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        int count = roleService.countUsersByRoleId(roleId);
        return Result.success(count);
    }

    @GetMapping("/{roleId}/depts/count")
    @Operation(summary = "查询角色部门数量", description = "查询使用该角色的部门数量")
    @PreAuthorize("hasAuthority('role.read')")
    public Result<Integer> countDeptsByRoleId(
            @Parameter(description = "角色ID", required = true)
            @PathVariable @NotNull(message = "角色ID不能为空") Long roleId) {
        int count = roleService.countDeptsByRoleId(roleId);
        return Result.success(count);
    }

    @GetMapping("/check/name")
    @Operation(summary = "检查角色名称是否存在", description = "检查角色名称是否存在")
    public Result<Boolean> checkRoleNameExists(
            @Parameter(description = "角色名称", required = true)
            @RequestParam @NotEmpty(message = "角色名称不能为空") String roleName,
            @Parameter(description = "角色ID（排除自己）")
            @RequestParam(required = false) Long roleId) {
        boolean exists = roleService.isRoleNameExists(roleName, roleId);
        return Result.success(exists);
    }

    @GetMapping("/check/key")
    @Operation(summary = "检查角色权限字符串是否存在", description = "检查角色权限字符串是否存在")
    public Result<Boolean> checkRoleKeyExists(
            @Parameter(description = "角色权限字符串", required = true)
            @RequestParam @NotEmpty(message = "角色权限字符串不能为空") String roleKey,
            @Parameter(description = "角色ID（排除自己）")
            @RequestParam(required = false) Long roleId) {
        boolean exists = roleService.isRoleKeyExists(roleKey, roleId);
        return Result.success(exists);
    }

    /**
     * 将Role实体转换为RoleVO
     */
    private RoleVO convertToRoleVO(Role role) {
        if (role == null) {
            return null;
        }
        RoleVO roleVO = new RoleVO();
        roleVO.setId(role.getId());
        roleVO.setRoleName(role.getRoleName());
        roleVO.setRoleKey(role.getRoleKey());
        roleVO.setRoleSort(role.getRoleSort());
        roleVO.setDataScope(role.getDataScope());
        roleVO.setMenuCheckStrictly(role.getMenuCheckStrictly());
        roleVO.setDeptCheckStrictly(role.getDeptCheckStrictly());
        roleVO.setStatus(role.getStatus());
        roleVO.setRemark(role.getRemark());
        roleVO.setCreateTime(role.getCreateTime());
        roleVO.setUpdateTime(role.getUpdateTime());
        roleVO.setCreateBy(role.getCreateBy());
        roleVO.setUpdateBy(role.getUpdateBy());
        return roleVO;
    }

    /**
     * 将RoleCreateDTO转换为Role实体
     */
    private Role convertToRole(RoleCreateDTO roleCreateDTO) {
        if (roleCreateDTO == null) {
            return null;
        }
        Role role = new Role();
        role.setRoleName(roleCreateDTO.getRoleName());
        role.setRoleKey(roleCreateDTO.getRoleKey());
        role.setRoleSort(roleCreateDTO.getRoleSort());
        role.setDataScope(roleCreateDTO.getDataScope());
        role.setMenuCheckStrictly(roleCreateDTO.getMenuCheckStrictly());
        role.setDeptCheckStrictly(roleCreateDTO.getDeptCheckStrictly());
        role.setStatus(roleCreateDTO.getStatus());
        role.setRemark(roleCreateDTO.getRemark());
        return role;
    }

    /**
     * 将RoleUpdateDTO转换为Role实体
     */
    private Role convertToRole(RoleUpdateDTO roleUpdateDTO) {
        if (roleUpdateDTO == null) {
            return null;
        }
        Role role = new Role();
        role.setId(roleUpdateDTO.getId());
        role.setRoleName(roleUpdateDTO.getRoleName());
        role.setRoleKey(roleUpdateDTO.getRoleKey());
        role.setRoleSort(roleUpdateDTO.getRoleSort());
        role.setDataScope(roleUpdateDTO.getDataScope());
        role.setMenuCheckStrictly(roleUpdateDTO.getMenuCheckStrictly());
        role.setDeptCheckStrictly(roleUpdateDTO.getDeptCheckStrictly());
        role.setStatus(roleUpdateDTO.getStatus());
        role.setRemark(roleUpdateDTO.getRemark());
        return role;
    }

    /**
     * 将IPage<Role>转换为PageResult<RoleVO>
     */
    private PageResult<RoleVO> convertToPageResult(IPage<Role> page) {
        if (page == null) {
            return null;
        }
        
        List<RoleVO> roleVOs = page.getRecords().stream()
                .map(this::convertToRoleVO)
                .collect(java.util.stream.Collectors.toList());
        
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), roleVOs);
    }
}
