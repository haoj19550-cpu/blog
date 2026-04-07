package com.estudys.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estudys.blog.entity.ArticleTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    @Delete("DELETE FROM article_tag WHERE article_id = #{articleId}")
    int physicalDeleteByArticleId(@Param("articleId") Long articleId);
}

