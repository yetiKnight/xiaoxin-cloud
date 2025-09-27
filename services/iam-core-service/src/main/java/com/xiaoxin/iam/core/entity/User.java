package com.xiaoxin.iam.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaoxin.iam.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
@Schema(description = "用户信息")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户账号")
    @TableField("username")
    private String username;

    @Schema(description = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "用户邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "手机号码")
    @TableField("phone")
    private String phone;

    @Schema(description = "用户性别（0男 1女 2未知）")
    @TableField("sex")
    private String sex;

    @Schema(description = "头像地址")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "密码")
    @TableField("password")
    @JsonIgnore
    private String password;

    @Schema(description = "帐号状态（0正常 1停用）")
    @TableField("status")
    private String status;

    @Schema(description = "删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;

    @Schema(description = "最后登录IP")
    @TableField("login_ip")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @TableField("login_date")
    private LocalDateTime loginDate;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "用户角色列表")
    @TableField(exist = false)
    private List<Role> roles;

    @Schema(description = "用户部门列表")
    @TableField(exist = false)
    private List<Dept> depts;

    @Schema(description = "用户权限列表")
    @TableField(exist = false)
    private List<Permission> permissions;

    @Schema(description = "用户菜单列表")
    @TableField(exist = false)
    private List<Menu> menus;

    /**
     * 判断用户是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status) && "0".equals(delFlag);
    }

    /**
     * 判断用户是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }

    /**
     * 判断用户是否被删除
     */
    public boolean isDeleted() {
        return "2".equals(delFlag);
    }

    /**
     * 获取用户显示名称
     */
    public String getDisplayName() {
        return com.xiaoxin.iam.common.utils.StringUtils.isNotBlank(nickname) ? nickname : username;
    }

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用");

        private final String code;
        private final String desc;

        UserStatus(String code, String desc) {
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
     * 性别枚举
     */
    public enum Sex {
        MALE("0", "男"),
        FEMALE("1", "女"),
        UNKNOWN("2", "未知");

        private final String code;
        private final String desc;

        Sex(String code, String desc) {
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
