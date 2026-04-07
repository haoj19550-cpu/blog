package com.estudys.blog.dto;

import lombok.Data;

@Data
public class ArticleListQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Long categoryId;
    private String keyword;
    private Integer status = 1;
}

