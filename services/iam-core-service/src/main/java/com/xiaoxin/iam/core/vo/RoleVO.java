package com.xiaoxin.iam.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色视图对象
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "角色视图对象")
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色权限字符串")
    private String roleKey;

    @Schema(description = "显示顺序")
    private Integer roleSort;

    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private String dataScope;

    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    private Boolean deptCheckStrictly;

    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "更新者")
    private String updateBy;

    // 关联数据
    @Schema(description = "角色权限列表")
    private List<PermissionVO> permissions;

    @Schema(description = "角色菜单列表")
    private List<MenuVO> menus;

    @Schema(description = "角色部门列表")
    private List<DeptVO> depts;

    /**
     * 判断角色是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status);
    }

    /**
     * 判断角色是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }
}
