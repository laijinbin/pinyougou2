package com.pinyougou.mapper;

import com.pinyougou.pojo.ItemCat;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ItemCatMapper  extends Mapper<ItemCat>{
    List<ItemCat> findItemCatByParentId(Long parentId);


    Long[] findIdByParentId(Long id);
}
