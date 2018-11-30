package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {
//   List<Specification> findAll(Specification specification);

    PageResult<Specification> findByPage(Specification specification, Integer page, Integer rows);

    void save(Specification specification);

    void update(Specification specification);

    void delete(Long[] ids);

//    List<Specification> findAll();

    List<Map<String,Object>> findIdAndName();
}
