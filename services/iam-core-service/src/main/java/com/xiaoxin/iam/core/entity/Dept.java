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
 * 部门实体类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dept")
@Schema(description = "部门信息")
public class Dept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "父部门ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "祖级列表")
    @TableField("ancestors")
    private String ancestors;

    @Schema(description = "部门名称")
    @TableField("dept_name")
    private String deptName;

    @Schema(description = "显示顺序")
    @TableField("order_num")
    private Integer orderNum;

    @Schema(description = "负责人")
    @TableField("leader")
    private String leader;

    @Schema(description = "联系电话")
    @TableField("phone")
    private String phone;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "部门状态（0正常 1停用）")
    @TableField("status")
    private String status;

    @Schema(description = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;

    // 非数据库字段
    @Schema(description = "子部门列表")
    @TableField(exist = false)
    private List<Dept> children;

    @Schema(description = "父部门名称")
    @TableField(exist = false)
    private String parentName;

    /**
     * 判断部门是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status) && "0".equals(delFlag);
    }

    /**
     * 判断部门是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }

    /**
     * 判断部门是否被删除
     */
    public boolean isDeleted() {
        return "2".equals(delFlag);
    }

    /**
     * 部门状态枚举
     */
    public enum DeptStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用");

        private final String code;
        private final String desc;

        DeptStatus(String code, String desc) {
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
