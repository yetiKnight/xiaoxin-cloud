package com.xiaoxin.iam.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装
 *
 * @author xiaoxin
 * @since 1.0.0
 */
@Data
@Schema(description = "分页结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页码")
    private Long current;

    @Schema(description = "每页大小")
    private Long size;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "数据列表")
    private List<T> records;

    @Schema(description = "是否有下一页")
    private Boolean hasNext;

    @Schema(description = "是否有上一页")
    private Boolean hasPrevious;

    /**
     * 创建分页结果
     *
     * @param current 当前页码
     * @param size 每页大小
     * @param total 总记录数
     * @param records 数据列表
     * @return 分页结果
     */
    public static <T> PageResult<T> of(Long current, Long size, Long total, List<T> records) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCurrent(current);
        pageResult.setSize(size);
        pageResult.setTotal(total);
        pageResult.setRecords(records);
        
        // 计算总页数
        Long pages = (total + size - 1) / size;
        pageResult.setPages(pages);
        
        // 计算是否有下一页和上一页
        pageResult.setHasNext(current < pages);
        pageResult.setHasPrevious(current > 1);
        
        return pageResult;
    }

    /**
     * 从MyBatis Plus的IPage创建分页结果
     *
     * @param page MyBatis Plus分页对象
     * @return 分页结果
     */
    public static <T> PageResult<T> of(com.baomidou.mybatisplus.core.metadata.IPage<T> page) {
        return of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }
}
