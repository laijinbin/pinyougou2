package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.AllOrder;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;

import java.util.List;

public interface OrderService {
    void save(Order order);

    PayLog findPayLogFromRedis(String userId);

    void updateOrderStatus(String outTradeNo, String transactionId);

    PageResult findByPage(Integer page, Integer rows,String userId);

    void updateOrderStatus2(Long money,String userId, String outTradeNo, String transaction_id);
}
