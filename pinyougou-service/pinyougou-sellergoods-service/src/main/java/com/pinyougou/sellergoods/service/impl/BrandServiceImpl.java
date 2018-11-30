package com.pinyougou.sellergoods.service.impl;


import com.alibaba.dubbo.config.annotation.Service;


import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.Brand;

import com.pinyougou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.BrandService")
@Transactional
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandMapper brandMapper;

//    @Override
//    public List<Brand> findAll() {
//        return brandMapper.selectAll();
//    }

    @Override
    public void save(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public PageResult<Brand> findByPage(Brand brand, Integer page, Integer rows) {
        PageInfo<Brand> pageInfo= null;
        try {
            pageInfo = PageHelper.startPage(page,rows)
             .doSelectPageInfo(new ISelect() {
                 @Override
                 public void doSelect() {
                     brandMapper.findAll(brand);
                 }
             });
            return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long[] ids) {
        Example example=new Example(Brand.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        brandMapper.deleteByExample(example);
    }

    @Override
    public List<Map<String,Object>> findIdAndName() {
        return brandMapper.findIdAndName();
    }


}
