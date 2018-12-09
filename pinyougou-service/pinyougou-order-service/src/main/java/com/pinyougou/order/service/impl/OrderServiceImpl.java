package com.pinyougou.order.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.mapper.PayLogMapper;
import com.pinyougou.pojo.Cart;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.pojo.PayLog;
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
    private PayLogMapper payLogMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void save(Order order) {
        try {
            List<Cart> cartList = (List<Cart>) redisTemplate
                    .boundValueOps("cart_" + order.getUserId()).get();
            //订单集合
            List<String> orderIdList = new ArrayList<>();
            //多个订单的总金额
            double totalMoney=0;
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
                    //记录订单的id
                }
                order1.setPayment(new BigDecimal(money));
                orderMapper.insertSelective(order1);
                orderIdList.add(String.valueOf(orderId));
                //记录总金额
                totalMoney+=money;
            }
            if ("1".equals(order.getPaymentType())){
                PayLog payLog=new PayLog();
                //订单交易号
                payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));
                payLog.setCreateTime(new Date());
                payLog.setPayTime(payLog.getCreateTime());
                payLog.setTotalFee((long) (totalMoney*100));
                payLog.setUserId(order.getUserId());
                payLog.setTradeState("0");
                payLog.setPayType("1");
                /** 订单号集合，逗号分隔 */
                String ids = orderIdList.toString().replace("[", "")
                        .replace("]", "").replace(" ","");
                payLog.setOrderList(ids);
                payLogMapper.insertSelective(payLog);
                /** 存入缓存 */
                redisTemplate.boundValueOps("payLog_" + order.getUserId()).set(payLog);
            }
            redisTemplate.delete("cart_" + order.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public PayLog findPayLogFromRedis(String userId) {
        try {
            return (PayLog) redisTemplate
                    .boundValueOps("payLog_" + userId).get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateOrderStatus(String outTradeNo, String transactionId) {
        try {
            /** 修改支付日志状态 */
            PayLog payLog = payLogMapper.selectByPrimaryKey(outTradeNo);
            payLog.setPayTime(new Date());
            payLog.setTradeState("1");
            payLog.setTransactionId(transactionId);// 交易流水号
            payLogMapper.updateByPrimaryKeySelective(payLog);
            //修改定订单信息
            String[] orderIds = payLog.getOrderList().split(","); // 订单号列表
            for (String orderId : orderIds) {
                Order order=new Order();
                order.setOrderId(Long.valueOf(orderId));
                order.setPaymentTime(new Date());
                order.setStatus("2");
                orderMapper.updateByPrimaryKeySelective(order);
            }
            /** 清除 redis 缓存数据 */
            redisTemplate.delete("payLog_" + payLog.getUserId());
        }catch (Exception e){
            throw new RuntimeException(e);

        }
    }
}
