package com.xiaoxin.iam.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 登录请求DTO
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {

    @Schema(description = "用户名", example = "admin")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20之间")
    private String username;

    @Schema(description = "密码", example = "123456")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    @Schema(description = "验证码", example = "1234")
    private String captcha;

    @Schema(description = "验证码Key", example = "uuid-key")
    private String captchaKey;

    @Schema(description = "记住我", example = "false")
    private Boolean rememberMe = false;

    @Schema(description = "登录IP", hidden = true)
    private String loginIp;

    @Schema(description = "用户代理", hidden = true)
    private String userAgent;
}
