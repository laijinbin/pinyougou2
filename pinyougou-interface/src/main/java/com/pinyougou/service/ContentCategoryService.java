package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.ContentCategory;

import java.util.List;

public interface ContentCategoryService {
    PageResult findByPage(Integer page, Integer rows, ContentCategory contentCategory);

    void save(ContentCategory contentCategory);

    void update(ContentCategory contentCategory);

    void delete(Long[] ids);

    List<ContentCategory> findAllContentCategory();
}
