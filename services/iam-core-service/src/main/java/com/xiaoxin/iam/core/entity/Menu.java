package com.xiaoxin.iam.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaoxin.iam.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 菜单实体类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
@Schema(description = "菜单信息")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "菜单名称")
    @TableField("menu_name")
    private String menuName;

    @Schema(description = "父菜单ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "显示顺序")
    @TableField("order_num")
    private Integer orderNum;

    @Schema(description = "路由地址")
    @TableField("path")
    private String path;

    @Schema(description = "组件路径")
    @TableField("component")
    private String component;

    @Schema(description = "路由参数")
    @TableField("query")
    private String query;

    @Schema(description = "是否为外链（0是 1否）")
    @TableField("is_frame")
    private Integer isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    @TableField("is_cache")
    private Integer isCache;

    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    @TableField("menu_type")
    private String menuType;

    @Schema(description = "菜单状态（0显示 1隐藏）")
    @TableField("visible")
    private String visible;

    @Schema(description = "菜单状态（0正常 1停用）")
    @TableField("status")
    private String status;

    @Schema(description = "权限标识")
    @TableField("perms")
    private String perms;

    @Schema(description = "菜单图标")
    @TableField("icon")
    private String icon;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "子菜单列表")
    @TableField(exist = false)
    private List<Menu> children;

    @Schema(description = "父菜单名称")
    @TableField(exist = false)
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

    /**
     * 菜单类型枚举
     */
    public enum MenuType {
        DIRECTORY("M", "目录"),
        MENU("C", "菜单"),
        BUTTON("F", "按钮");

        private final String code;
        private final String desc;

        MenuType(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 菜单状态枚举
     */
    public enum MenuStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用");

        private final String code;
        private final String desc;

        MenuStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 显示状态枚举
     */
    public enum Visible {
        SHOW("0", "显示"),
        HIDDEN("1", "隐藏");

        private final String code;
        private final String desc;

        Visible(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
