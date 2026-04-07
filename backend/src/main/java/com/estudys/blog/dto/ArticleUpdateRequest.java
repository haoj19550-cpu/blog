package com.estudys.blog.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "更新文章请求")
public class ArticleUpdateRequest {
    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title length must <= 255")
    @Schema(description = "文章标题", example = "Spring Boot 实战总结(更新)")
    private String title;

    @NotBlank(message = "content is required")
    @Schema(description = "文章内容(Markdown)", example = "# 更新后标题\\n这是更新后的正文")
    private String content;

    @Size(max = 500, message = "summary length must <= 500")
    @Schema(description = "文章摘要", example = "更新后的摘要")
    private String summary;

    @Schema(description = "分类ID", example = "1001")
    private Long categoryId;
    @Schema(description = "封面图URL", example = "https://cdn.example.com/new-cover.png")
    private String coverImage;
    @Schema(description = "是否置顶(0否,1是)", example = "0")
    private Integer isTop;
    @Schema(description = "文章状态(0草稿,1已发布)", example = "1")
    private Integer status;
    @ArraySchema(schema = @Schema(description = "标签ID", example = "2001"), arraySchema = @Schema(description = "标签ID列表"))
    private List<Long> tagIds;
}

