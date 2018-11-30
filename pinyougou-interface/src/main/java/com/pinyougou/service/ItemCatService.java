package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.ItemCat;

import java.util.List;

public interface ItemCatService {

    List<ItemCat> findItemCatByParentId(Long parentId);

    void save(ItemCat itemCat);

    void update(ItemCat itemCat);

    void delete(Long[] ids, Integer grade);
}
