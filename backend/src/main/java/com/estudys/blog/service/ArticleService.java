package com.estudys.blog.service;

import com.estudys.blog.dto.ArticleCreateRequest;
import com.estudys.blog.dto.ArticleDetailResponse;
import com.estudys.blog.dto.ArticleListItem;
import com.estudys.blog.dto.ArticleListQuery;
import com.estudys.blog.dto.ArticleUpdateRequest;
import com.estudys.blog.dto.PageResponse;

public interface ArticleService {
    Long createArticle(Long currentUserId, ArticleCreateRequest request);

    void updateArticle(Long articleId, Long currentUserId, ArticleUpdateRequest request);

    void deleteArticle(Long articleId, Long currentUserId);

    ArticleDetailResponse getArticleDetail(Long articleId);

    PageResponse<ArticleListItem> listArticles(ArticleListQuery query);
}

