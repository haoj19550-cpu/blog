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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public Result<Long> createArticle(@Valid @RequestBody ArticleCreateRequest request, HttpServletRequest servletRequest) {
        Long currentUserId = (Long) servletRequest.getAttribute(JwtAuthInterceptor.CURRENT_USER_ID);
        return Result.ok(articleService.createArticle(currentUserId, request));
    }

    @PutMapping("/{articleId}")
    public Result<Void> updateArticle(@PathVariable Long articleId, @Valid @RequestBody ArticleUpdateRequest request, HttpServletRequest servletRequest) {
        Long currentUserId = (Long) servletRequest.getAttribute(JwtAuthInterceptor.CURRENT_USER_ID);
        articleService.updateArticle(articleId, currentUserId, request);
        return Result.ok(null);
    }

    @DeleteMapping("/{articleId}")
    public Result<Void> deleteArticle(@PathVariable Long articleId, HttpServletRequest servletRequest) {
        Long currentUserId = (Long) servletRequest.getAttribute(JwtAuthInterceptor.CURRENT_USER_ID);
        articleService.deleteArticle(articleId, currentUserId);
        return Result.ok(null);
    }

    @GetMapping("/{articleId}")
    public Result<ArticleDetailResponse> getArticleDetail(@PathVariable Long articleId) {
        return Result.ok(articleService.getArticleDetail(articleId));
    }

    @GetMapping
    public Result<PageResponse<ArticleListItem>> listArticles(ArticleListQuery query) {
        return Result.ok(articleService.listArticles(query));
    }
}

