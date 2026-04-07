package com.estudys.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleListItem {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private Long categoryId;
    private String coverImage;
    private Integer viewCount;
    private Integer isTop;
    private Integer status;
    private LocalDateTime createTime;
    private List<Long> tagIds;
}

