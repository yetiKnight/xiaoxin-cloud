package com.xiaoxin.iam.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户视图对象
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "用户视图对象")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "帐号状态（0正常 1停用）")
    private String status;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;

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
    @Schema(description = "用户角色列表")
    private List<RoleVO> roles;

    @Schema(description = "用户部门列表")
    private List<DeptVO> depts;

    @Schema(description = "用户权限列表")
    private List<PermissionVO> permissions;

    @Schema(description = "用户菜单列表")
    private List<MenuVO> menus;

    /**
     * 获取用户显示名称
     */
    public String getDisplayName() {
        return (nickname != null && !nickname.trim().isEmpty()) ? nickname : username;
    }

    /**
     * 判断用户是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status);
    }

    /**
     * 判断用户是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }
}
