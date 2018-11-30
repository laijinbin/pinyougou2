package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Content;

public interface ContentService {
    PageResult findByPage(Integer page, Integer rows, Content content);

    void delete(Long[] ids);

    void save(Content content);

    void update(Content content);
}
