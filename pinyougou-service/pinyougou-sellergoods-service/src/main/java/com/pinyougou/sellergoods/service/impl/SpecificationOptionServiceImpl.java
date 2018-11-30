package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.service.SpecificationOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.beans.Transient;
import java.util.Arrays;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.SpecificationOptionService")
@Transactional
public class SpecificationOptionServiceImpl implements SpecificationOptionService {

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;
    @Override
    public List<SpecificationOption> findById(Long id) {
        return specificationOptionMapper.findById(id);
    }

    @Override
    public List<SpecificationOption> findspecificationItemsOption(Long[] specId) {
        Example example=new Example(SpecificationOption.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andIn("specId", Arrays.asList(specId));
        return specificationOptionMapper.selectByExample(example);
    }
//    @Override
//    public List<SpecificationOption> findAll() {
//        return null;
//    }
}
