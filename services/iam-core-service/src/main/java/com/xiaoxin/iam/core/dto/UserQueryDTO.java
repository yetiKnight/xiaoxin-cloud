package com.xiaoxin.iam.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户查询DTO
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "用户查询DTO")
public class UserQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户账号", example = "admin")
    private String username;

    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    @Schema(description = "用户邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    @Schema(description = "用户性别（0男 1女 2未知）", example = "0")
    private String sex;

    @Schema(description = "帐号状态（0正常 1停用）", example = "0")
    private String status;

    @Schema(description = "创建时间开始", example = "2024-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束", example = "2024-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;
}
