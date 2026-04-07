package com.estudys.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class Article extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String title;
    private String content;
    private String summary;
    private Long categoryId;
    private String coverImage;
    private Long userId;
    private Integer viewCount;
    private Integer isTop;
    private Integer status;
}

