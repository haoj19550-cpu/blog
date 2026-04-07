package com.estudys.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estudys.blog.dto.ArticleCreateRequest;
import com.estudys.blog.dto.ArticleDetailResponse;
import com.estudys.blog.dto.ArticleListItem;
import com.estudys.blog.dto.ArticleListQuery;
import com.estudys.blog.dto.ArticleUpdateRequest;
import com.estudys.blog.dto.PageResponse;
import com.estudys.blog.entity.Article;
import com.estudys.blog.entity.ArticleTag;
import com.estudys.blog.exception.BusinessException;
import com.estudys.blog.mapper.ArticleMapper;
import com.estudys.blog.mapper.ArticleTagMapper;
import com.estudys.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createArticle(Long currentUserId, ArticleCreateRequest request) {
        Article article = new Article();
        article.setUserId(currentUserId);
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCategoryId(request.getCategoryId());
        article.setCoverImage(request.getCoverImage());
        article.setViewCount(0);
        article.setIsTop(request.getIsTop() == null ? 0 : request.getIsTop());
        article.setStatus(request.getStatus() == null ? 0 : request.getStatus());
        articleMapper.insert(article);

        saveArticleTags(article.getId(), request.getTagIds());
        return article.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(Long articleId, Long currentUserId, ArticleUpdateRequest request) {
        Article dbArticle = mustGetArticle(articleId);
        checkOwner(dbArticle, currentUserId);

        Article article = new Article();
        article.setId(articleId);
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCategoryId(request.getCategoryId());
        article.setCoverImage(request.getCoverImage());
        article.setIsTop(request.getIsTop() == null ? 0 : request.getIsTop());
        article.setStatus(request.getStatus() == null ? 0 : request.getStatus());
        articleMapper.updateById(article);

        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
        saveArticleTags(articleId, request.getTagIds());
    }

    @Override
    public void deleteArticle(Long articleId, Long currentUserId) {
        Article dbArticle = mustGetArticle(articleId);
        checkOwner(dbArticle, currentUserId);
        articleMapper.deleteById(articleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleDetailResponse getArticleDetail(Long articleId) {
        Article article = mustGetArticle(articleId);
        articleMapper.update(
                null,
                new LambdaUpdateWrapper<Article>()
                        .eq(Article::getId, articleId)
                        .set(Article::getViewCount, article.getViewCount() + 1)
        );
        article.setViewCount(article.getViewCount() + 1);
        return toDetail(article);
    }

    @Override
    public PageResponse<ArticleListItem> listArticles(ArticleListQuery query) {
        if (query == null) {
            query = new ArticleListQuery();
        }
        int pageNum = query.getPageNum() == null || query.getPageNum() < 1 ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null || query.getPageSize() < 1 ? 10 : Math.min(query.getPageSize(), 50);
        IPage<Article> page = articleMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Article>()
                        .eq(query.getCategoryId() != null, Article::getCategoryId, query.getCategoryId())
                        .eq(query.getStatus() != null, Article::getStatus, query.getStatus())
                        .like(StringUtils.hasText(query.getKeyword()), Article::getTitle, query.getKeyword())
                        .orderByDesc(Article::getIsTop)
                        .orderByDesc(Article::getCreateTime)
        );

        List<ArticleListItem> list = page.getRecords().stream().map(this::toListItem).toList();
        return new PageResponse<>(page.getTotal(), pageNum, pageSize, list);
    }

    private Article mustGetArticle(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BusinessException("article not found");
        }
        return article;
    }

    private void checkOwner(Article article, Long userId) {
        if (userId == null || !userId.equals(article.getUserId())) {
            throw new BusinessException("no permission to operate this article");
        }
    }

    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        for (Long tagId : tagIds) {
            if (tagId == null) {
                continue;
            }
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tagId);
            articleTagMapper.insert(articleTag);
        }
    }

    private ArticleListItem toListItem(Article article) {
        ArticleListItem item = new ArticleListItem();
        item.setId(article.getId());
        item.setTitle(article.getTitle());
        item.setSummary(article.getSummary());
        item.setCategoryId(article.getCategoryId());
        item.setCoverImage(article.getCoverImage());
        item.setViewCount(article.getViewCount());
        item.setIsTop(article.getIsTop());
        item.setStatus(article.getStatus());
        item.setCreateTime(article.getCreateTime());
        item.setTagIds(getTagIds(article.getId()));
        return item;
    }

    private ArticleDetailResponse toDetail(Article article) {
        ArticleDetailResponse detail = new ArticleDetailResponse();
        detail.setId(article.getId());
        detail.setUserId(article.getUserId());
        detail.setTitle(article.getTitle());
        detail.setContent(article.getContent());
        detail.setSummary(article.getSummary());
        detail.setCategoryId(article.getCategoryId());
        detail.setCoverImage(article.getCoverImage());
        detail.setViewCount(article.getViewCount());
        detail.setIsTop(article.getIsTop());
        detail.setStatus(article.getStatus());
        detail.setCreateTime(article.getCreateTime());
        detail.setUpdateTime(article.getUpdateTime());
        detail.setTagIds(getTagIds(article.getId()));
        return detail;
    }

    private List<Long> getTagIds(Long articleId) {
        List<ArticleTag> relations = articleTagMapper.selectList(
                new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId)
        );
        if (relations == null || relations.isEmpty()) {
            return new ArrayList<>();
        }
        return relations.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
    }
}

