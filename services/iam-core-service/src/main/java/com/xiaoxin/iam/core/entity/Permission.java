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
 * 权限实体类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_permission")
@Schema(description = "权限信息")
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "权限ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "权限名称")
    @TableField("permission_name")
    private String permissionName;

    @Schema(description = "权限编码")
    @TableField("permission_code")
    private String permissionCode;

    @Schema(description = "权限类型（1功能权限 2数据权限 3字段权限）")
    @TableField("permission_type")
    private String permissionType;

    @Schema(description = "资源类型（API接口 MENU菜单 BUTTON按钮 DATA数据）")
    @TableField("resource_type")
    private String resourceType;

    @Schema(description = "资源路径")
    @TableField("resource_path")
    private String resourcePath;

    @Schema(description = "HTTP方法（GET POST PUT DELETE）")
    @TableField("http_method")
    private String httpMethod;

    @Schema(description = "父权限ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "权限层级")
    @TableField("level")
    private Integer level;

    @Schema(description = "排序")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "状态（0正常 1停用）")
    @TableField("status")
    private String status;

    @Schema(description = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "子权限列表")
    @TableField(exist = false)
    private List<Permission> children;

    @Schema(description = "父权限名称")
    @TableField(exist = false)
    private String parentName;

    /**
     * 判断权限是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status) && "0".equals(delFlag);
    }

    /**
     * 判断权限是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }

    /**
     * 判断权限是否被删除
     */
    public boolean isDeleted() {
        return "2".equals(delFlag);
    }

    /**
     * 权限类型枚举
     */
    public enum PermissionType {
        FUNCTION("1", "功能权限"),
        DATA("2", "数据权限"),
        FIELD("3", "字段权限");

        private final String code;
        private final String desc;

        PermissionType(String code, String desc) {
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
     * 资源类型枚举
     */
    public enum ResourceType {
        API("API", "API接口"),
        MENU("MENU", "菜单"),
        BUTTON("BUTTON", "按钮"),
        DATA("DATA", "数据");

        private final String code;
        private final String desc;

        ResourceType(String code, String desc) {
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
     * HTTP方法枚举
     */
    public enum HttpMethod {
        GET("GET", "GET"),
        POST("POST", "POST"),
        PUT("PUT", "PUT"),
        DELETE("DELETE", "DELETE"),
        PATCH("PATCH", "PATCH");

        private final String code;
        private final String desc;

        HttpMethod(String code, String desc) {
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
