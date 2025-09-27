package com.xiaoxin.iam.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限视图对象
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "权限视图对象")
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "权限ID")
    private Long id;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限编码")
    private String permissionCode;

    @Schema(description = "权限类型（1功能权限 2数据权限 3字段权限）")
    private String permissionType;

    @Schema(description = "资源类型（API接口 MENU菜单 BUTTON按钮 DATA数据）")
    private String resourceType;

    @Schema(description = "资源路径")
    private String resourcePath;

    @Schema(description = "HTTP方法（GET POST PUT DELETE）")
    private String httpMethod;

    @Schema(description = "父权限ID")
    private Long parentId;

    @Schema(description = "权限层级")
    private Integer level;

    @Schema(description = "排序")
    private Integer sortOrder;

    @Schema(description = "权限状态（0正常 1停用）")
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

    // 非数据库字段
    @Schema(description = "子权限列表")
    private List<PermissionVO> children;

    @Schema(description = "父权限名称")
    private String parentName;

    /**
     * 判断权限是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status);
    }

    /**
     * 判断权限是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }
}
