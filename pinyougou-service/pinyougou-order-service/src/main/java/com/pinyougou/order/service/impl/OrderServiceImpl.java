package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.pojo.Cart;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceName = "com.pinyougou.service.OrderService")
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(Order order) {
        try {
            List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps("cart_" + order.getUserId()).get();
            for (Cart cart : cartList) {
                //往订单表插入数据
                long orderId=idWorker.nextId();
                Order order1=new Order();
                order1.setOrderId(orderId);
                order1.setPaymentType(order.getPaymentType());
                order1.setPostFee("");
                order1.setStatus("1");//未支付
                order1.setCreateTime(new Date());
                order1.setUpdateTime(order1.getCreateTime());
                order1.setUserId(order.getUserId());
                order1.setReceiverAreaName(order.getReceiverAreaName());
                order1.setReceiverMobile(order.getReceiverMobile());
                order1.setReceiver(order.getReceiver());
                order1.setSourceType(order.getSourceType());
                order1.setSellerId(order.getSellerId());
                double money=0;
                for (OrderItem orderItem : cart.getOrderItems()) {
                    orderItem.setId(idWorker.nextId());
                    // 设置关联的订单 id
                    orderItem.setOrderId(orderId);
                    // 累计总金额
                    money += orderItem.getTotalFee().doubleValue();
                    orderItemMapper.insertSelective(orderItem);
                }
                order1.setPayment(new BigDecimal(money));
                orderMapper.insertSelective(order1);
            }
            redisTemplate.delete("cart_" + order.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
