package com.estudys.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "文章列表查询参数")
public class ArticleListQuery {
    @Schema(description = "页码(从1开始)", example = "1")
    private Integer pageNum = 1;
    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
    @Schema(description = "分类ID", example = "1001")
    private Long categoryId;
    @Schema(description = "标题关键词", example = "Spring")
    private String keyword;
    @Schema(description = "文章状态(0草稿,1已发布)", example = "1")
    private Integer status = 1;
}

