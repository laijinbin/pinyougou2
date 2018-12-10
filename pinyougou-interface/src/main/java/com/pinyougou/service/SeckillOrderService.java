package com.pinyougou.service;

import com.pinyougou.pojo.SeckillOrder;

public interface SeckillOrderService {
    void submitOrder(String userName, Long id);

    SeckillOrder findSeckillOrderFromRedis(String userId);

    void updateOrderStatus(String userId, String transactionId);
}
