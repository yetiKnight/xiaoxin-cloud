package com.xiaoxin.iam.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色更新DTO
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "角色更新DTO")
public class RoleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID", required = true, example = "1")
    @NotNull(message = "角色ID不能为空")
    private Long id;

    @Schema(description = "角色名称", example = "系统管理员")
    @Size(min = 2, max = 50, message = "角色名称长度必须在2-50之间")
    private String roleName;

    @Schema(description = "角色权限字符串", example = "admin")
    @Size(min = 2, max = 50, message = "角色权限字符串长度必须在2-50之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "角色权限字符串只能包含字母、数字和下划线")
    private String roleKey;

    @Schema(description = "显示顺序", example = "1")
    private Integer roleSort;

    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）", example = "1")
    @Pattern(regexp = "^[1-4]$", message = "数据范围只能是1-4")
    private String dataScope;

    @Schema(description = "菜单树选择项是否关联显示", example = "true")
    private Boolean menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示", example = "true")
    private Boolean deptCheckStrictly;

    @Schema(description = "角色状态（0正常 1停用）", example = "0")
    @Pattern(regexp = "^[0-1]$", message = "角色状态只能是0或1")
    private String status;

    @Schema(description = "备注", example = "系统管理员角色")
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;
}
