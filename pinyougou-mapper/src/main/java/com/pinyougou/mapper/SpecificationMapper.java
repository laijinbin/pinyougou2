package com.pinyougou.mapper;

import com.pinyougou.pojo.Specification;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecificationMapper extends Mapper<Specification> {
    List<Specification> findAll(Specification specification);

    List<Map<String,Object>> findIdAndName();
}
