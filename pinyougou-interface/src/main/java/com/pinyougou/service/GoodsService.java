package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;

import java.util.Map;

public interface GoodsService {
    void save(Goods goods);

    PageResult findByPage(Integer page, Integer rows, Goods goods);

    void checkPass(Long[] ids,String status);

    void deleteGoods(Long[] ids);

    void updateMarketable(Long[] ids, String marketable);

    Map<String,Object> getGoods(Long goodsId);
}
