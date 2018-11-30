package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.SpecificationService")
@Transactional
public class SpecificationServiceImpl implements SpecificationService{

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;
//    @Override
//    public List<Specification> findAll() {
//        return specificationMapper.selectAll();
//    }

    @Override
    public PageResult<Specification> findByPage(Specification specification, Integer page, Integer rows) {
        PageInfo<Specification> pageInfo = null;
        try {
            pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    specificationMapper.findAll(specification);
                }
            });
            return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Specification specification) {
        try {
            specificationMapper.insertSelective(specification);
            specificationOptionMapper.save(specification);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Specification specification) {
        try {
            specificationMapper.updateByPrimaryKeySelective(specification);
            specificationOptionMapper.deleteById(specification.getId());
            specificationOptionMapper.save(specification);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long[] ids) {
        try {
            Example example=new Example(Specification.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids));
            for (Long id : ids) {
             specificationOptionMapper.deleteById(id);
            }
            specificationMapper.deleteByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> findIdAndName() {
        return specificationMapper.findIdAndName();
    }
}
