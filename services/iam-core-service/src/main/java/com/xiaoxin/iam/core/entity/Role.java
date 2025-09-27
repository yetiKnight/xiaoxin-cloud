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
 * 角色实体类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
@Schema(description = "角色信息")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色名称")
    @TableField("role_name")
    private String roleName;

    @Schema(description = "角色权限字符串")
    @TableField("role_key")
    private String roleKey;

    @Schema(description = "显示顺序")
    @TableField("role_sort")
    private Integer roleSort;

    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    @TableField("data_scope")
    private String dataScope;

    @Schema(description = "菜单树选择项是否关联显示")
    @TableField("menu_check_strictly")
    private Boolean menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    @TableField("dept_check_strictly")
    private Boolean deptCheckStrictly;

    @Schema(description = "角色状态（0正常 1停用）")
    @TableField("status")
    private String status;

    @Schema(description = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "角色权限列表")
    @TableField(exist = false)
    private List<Permission> permissions;

    @Schema(description = "角色菜单列表")
    @TableField(exist = false)
    private List<Menu> menus;

    @Schema(description = "角色部门列表")
    @TableField(exist = false)
    private List<Dept> depts;

    /**
     * 判断角色是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status) && "0".equals(delFlag);
    }

    /**
     * 判断角色是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }

    /**
     * 判断角色是否被删除
     */
    public boolean isDeleted() {
        return "2".equals(delFlag);
    }

    /**
     * 角色状态枚举
     */
    public enum RoleStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用");

        private final String code;
        private final String desc;

        RoleStatus(String code, String desc) {
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
     * 删除标志枚举
     */
    public enum DelFlag {
        NORMAL("0", "正常"),
        DELETED("2", "删除");

        private final String code;
        private final String desc;

        DelFlag(String code, String desc) {
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
     * 数据范围枚举
     */
    public enum DataScope {
        ALL("1", "全部数据权限"),
        CUSTOM("2", "自定数据权限"),
        DEPT("3", "本部门数据权限"),
        DEPT_AND_CHILD("4", "本部门及以下数据权限");

        private final String code;
        private final String desc;

        DataScope(String code, String desc) {
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
