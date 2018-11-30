package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
    PageResult<TypeTemplate> findByPage(TypeTemplate typeTemplate, Integer page, Integer rows);

    void save(TypeTemplate typeTemplate);

    void update(TypeTemplate typeTemplate);

    void delete(Long[] ids);

    List<Map<String,Object>> findTypeTemplateList();

    TypeTemplate findBrandList(Long id);
}
