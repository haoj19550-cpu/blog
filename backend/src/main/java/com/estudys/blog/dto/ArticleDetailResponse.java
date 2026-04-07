package com.estudys.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleDetailResponse {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String summary;
    private Long categoryId;
    private String coverImage;
    private Integer viewCount;
    private Integer isTop;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Long> tagIds;
}

