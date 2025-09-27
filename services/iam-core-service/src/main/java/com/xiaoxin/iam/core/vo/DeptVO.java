package com.xiaoxin.iam.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门视图对象
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "部门视图对象")
public class DeptVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    private Long id;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态（0正常 1停用）")
    private String status;

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
    @Schema(description = "子部门列表")
    private List<DeptVO> children;

    @Schema(description = "父部门名称")
    private String parentName;

    /**
     * 判断部门是否正常状态
     */
    public boolean isNormal() {
        return "0".equals(status);
    }

    /**
     * 判断部门是否被停用
     */
    public boolean isDisabled() {
        return "1".equals(status);
    }
}
