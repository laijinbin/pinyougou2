package com.pinyougou.mapper;

import com.pinyougou.pojo.Order;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<Order> {
    List<Order> findAllOrderByUserId(String userId);

}
