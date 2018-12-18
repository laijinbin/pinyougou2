package com.pinyougou.mapper;

import com.pinyougou.pojo.OrderItem;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderItemMapper extends Mapper<OrderItem> {
    List<OrderItem> findOrderItemByOrderId(Long orderId);
}
