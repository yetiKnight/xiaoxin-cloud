package com.xiaoxin.iam.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单视图对象
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "菜单视图对象")
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "是否为外链（0是 1否）")
    private Integer isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;

    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(description = "菜单状态（0显示 1隐藏）")
    private String visible;

    @Schema(description = "菜单状态（0正常 1停用）")
    private String status;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

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
    @Schema(description = "子菜单列表")
    private List<MenuVO> children;

    @Schema(description = "父菜单名称")
    private String parentName;

    /**
     * 判断菜单是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status);
    }

    /**
     * 判断菜单是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }

    /**
     * 判断菜单是否显示
     */
    public boolean isVisible() {
        return "0".equals(visible);
    }

    /**
     * 判断菜单是否隐藏
     */
    public boolean isHidden() {
        return "1".equals(visible);
    }

    /**
     * 判断是否为外链
     */
    public boolean isFrame() {
        return Integer.valueOf(0).equals(isFrame);
    }

    /**
     * 判断是否缓存
     */
    public boolean isCache() {
        return Integer.valueOf(0).equals(isCache);
    }
}
