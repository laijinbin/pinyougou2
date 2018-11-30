package com.pinyougou.service;

import com.pinyougou.pojo.SpecificationOption;

import java.util.List;

public interface SpecificationOptionService {
    List<SpecificationOption> findById(Long id);

    List<SpecificationOption> findspecificationItemsOption(Long[] specId);


//    List<SpecificationOption> findAll();
}
