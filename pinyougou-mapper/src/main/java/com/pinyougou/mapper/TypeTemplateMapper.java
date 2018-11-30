package com.pinyougou.mapper;

import com.pinyougou.pojo.TypeTemplate;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TypeTemplateMapper extends Mapper<TypeTemplate> {
    List<TypeTemplate> findAll(TypeTemplate typeTemplate);

    List<Map<String,Object>> findTypeTemplateList();
}
