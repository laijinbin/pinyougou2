package com.pinyougou.service;

import com.pinyougou.pojo.Cart;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CartService {
    List<Cart> addCart(List<Cart> cartList, Long itemId, Integer num);

    List<Cart> findCartRedis(String userName);

    void saveCartRedis(String userName, List<Cart> cartList);

    List<Cart> mergeCart(List<Cart> cartList, List<Cart> cookieCarts);
}
