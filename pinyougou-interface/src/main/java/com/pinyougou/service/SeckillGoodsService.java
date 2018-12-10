package com.pinyougou.service;

import com.pinyougou.pojo.SeckillGoods;

import java.util.List;

public interface SeckillGoodsService {
    List<SeckillGoods> findSeckillGoods();

    SeckillGoods findOne(Long id);
}
