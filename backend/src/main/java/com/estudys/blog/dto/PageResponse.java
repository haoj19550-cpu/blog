package com.estudys.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private long total;
    private long pageNum;
    private long pageSize;
    private List<T> list;
}

