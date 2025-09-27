package com.xiaoxin.iam.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建DTO
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "用户创建DTO")
public class UserCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户账号", required = true, example = "admin")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 4, max = 20, message = "用户账号长度必须在4-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户账号只能包含字母、数字和下划线")
    private String username;

    @Schema(description = "用户昵称", example = "管理员")
    @Size(max = 50, message = "用户昵称长度不能超过50")
    private String nickname;

    @Schema(description = "用户邮箱", example = "admin@example.com")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100")
    private String email;

    @Schema(description = "手机号码", example = "13800138000")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Schema(description = "用户性别（0男 1女 2未知）", example = "0")
    @Pattern(regexp = "^[0-2]$", message = "性别只能是0、1或2")
    private String sex;

    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg")
    @Size(max = 200, message = "头像地址长度不能超过200")
    private String avatar;

    @Schema(description = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    @Schema(description = "备注", example = "系统管理员")
    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;
}
