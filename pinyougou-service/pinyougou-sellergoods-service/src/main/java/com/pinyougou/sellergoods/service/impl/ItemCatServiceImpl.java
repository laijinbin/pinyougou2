package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.ItemCatService;
import org.apache.commons.lang3.ArrayUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.ItemCatService")
@Transactional
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public List<ItemCat> findItemCatByParentId(Long parentId) {
        return itemCatMapper.findItemCatByParentId(parentId);
    }

    @Override
    public void save(ItemCat itemCat) {
        itemCatMapper.insertSelective(itemCat);
    }

    @Override
    public void update(ItemCat itemCat) {
        itemCatMapper.updateByPrimaryKeySelective(itemCat);
    }

    @Override
    public void delete(Long[] ids, Integer grade) {
        try {
            Long[] allids=Arrays.copyOf(ids,ids.length);
            Example example=new Example(ItemCat.class);
            Example.Criteria criteria=example.createCriteria();
            if (grade==3){
                criteria.andIn("id", Arrays.asList(ids));
            }else if (grade==2){
                for (Long id : ids) {
                    Long[]  thirdlyId=itemCatMapper.findIdByParentId(id);
                    allids= ArrayUtils.addAll(allids,thirdlyId);
                }
                criteria.andIn("id", Arrays.asList(allids));
            }else if (grade==1){
                for (Long id : ids) {
                    Long[]  secondId=itemCatMapper.findIdByParentId(id);
                       allids= ArrayUtils.addAll(allids,secondId);
                    for (Long aLong : secondId) {
                        Long[]  thirdlyId=itemCatMapper.findIdByParentId(aLong);
                       allids= ArrayUtils.addAll(allids,thirdlyId);
                    }
                }
                criteria.andIn("id", Arrays.asList(allids));
            }
            itemCatMapper.deleteByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
