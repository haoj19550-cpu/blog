package com.estudys.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ArticleCreateRequest {
    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title length must <= 255")
    private String title;

    @NotBlank(message = "content is required")
    private String content;

    @Size(max = 500, message = "summary length must <= 500")
    private String summary;

    private Long categoryId;
    private String coverImage;
    private Integer isTop;
    private Integer status;
    private List<Long> tagIds;
}

