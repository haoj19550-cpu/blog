package com.estudys.blog.controller;

import com.estudys.blog.common.Result;
import com.estudys.blog.dto.ArticleCreateRequest;
import com.estudys.blog.dto.ArticleDetailResponse;
import com.estudys.blog.dto.ArticleListItem;
import com.estudys.blog.dto.ArticleListQuery;
import com.estudys.blog.dto.ArticleUpdateRequest;
import com.estudys.blog.dto.PageResponse;
import com.estudys.blog.security.JwtAuthInterceptor;
import com.estudys.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "文章模块", description = "文章发布、编辑、删除、详情与列表")
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "发布文章", description = "需要 Bearer Token")
    @PostMapping
    public Result<Long> createArticle(@Valid @RequestBody ArticleCreateRequest request, HttpServletRequest servletRequest) {
        Long currentUserId = (Long) servletRequest.getAttribute(JwtAuthInterceptor.CURRENT_USER_ID);
        return Result.ok(articleService.createArticle(currentUserId, request));
    }

    @Operation(summary = "编辑文章", description = "需要 Bearer Token，仅作者可操作")
    @PutMapping("/{articleId}")
    public Result<Void> updateArticle(@Parameter(description = "文章ID", example = "1912345678901234567") @PathVariable Long articleId, @Valid @RequestBody ArticleUpdateRequest request, HttpServletRequest servletRequest) {
        Long currentUserId = (Long) servletRequest.getAttribute(JwtAuthInterceptor.CURRENT_USER_ID);
        articleService.updateArticle(articleId, currentUserId, request);
        return Result.ok(null);
    }

    @Operation(summary = "删除文章", description = "需要 Bearer Token，仅作者可操作")
    @DeleteMapping("/{articleId}")
    public Result<Void> deleteArticle(@Parameter(description = "文章ID", example = "1912345678901234567") @PathVariable Long articleId, HttpServletRequest servletRequest) {
        Long currentUserId = (Long) servletRequest.getAttribute(JwtAuthInterceptor.CURRENT_USER_ID);
        articleService.deleteArticle(articleId, currentUserId);
        return Result.ok(null);
    }

    @Operation(summary = "文章详情", description = "查看文章详情并自动增加浏览量")
    @GetMapping("/{articleId}")
    public Result<ArticleDetailResponse> getArticleDetail(@Parameter(description = "文章ID", example = "1912345678901234567") @PathVariable Long articleId) {
        return Result.ok(articleService.getArticleDetail(articleId));
    }

    @Operation(summary = "文章列表", description = "支持分页、分类筛选和关键词搜索")
    @GetMapping
    public Result<PageResponse<ArticleListItem>> listArticles(ArticleListQuery query) {
        return Result.ok(articleService.listArticles(query));
    }
}

