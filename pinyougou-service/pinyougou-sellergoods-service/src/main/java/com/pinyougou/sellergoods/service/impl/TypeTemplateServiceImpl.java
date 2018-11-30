package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Override
    public PageResult<TypeTemplate> findByPage(TypeTemplate typeTemplate, Integer page, Integer rows) {
        PageInfo<TypeTemplate> pageInfo=null;
        try{
            pageInfo= PageHelper.startPage(page,rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    typeTemplateMapper.findAll(typeTemplate);
                }
            });
            return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }



    }

    @Override
    public void save(TypeTemplate typeTemplate) {
        typeTemplateMapper.insert(typeTemplate);
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public void delete(Long[] ids) {
        Example example=new Example(TypeTemplate.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        typeTemplateMapper.deleteByExample(example);
    }

    @Override
    public List<Map<String, Object>> findTypeTemplateList() {
        return typeTemplateMapper.findTypeTemplateList();
    }

    @Override
    public TypeTemplate findBrandList(Long id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }
}
