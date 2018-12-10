package com.pinyougou.seckill.service.impl;
import java.math.BigDecimal;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.mapper.SeckillOrderMapper;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(interfaceName = "com.pinyougou.service.SeckillOrderService")
@Transactional
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SeckillOrderMapper  seckillOrderMapper;

    @Override
    public void submitOrder(String userName, Long id) {
        try {
            SeckillGoods seckillGoods =
                    (SeckillGoods) redisTemplate.boundHashOps("seckillGoodsList").get(id);
            if (seckillGoods != null && seckillGoods.getStockCount() > 0) {
                seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
                if (seckillGoods.getStockCount() == 0) {
                    seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                    redisTemplate.boundHashOps("seckillGoodsList").delete(id);
                } else {
                    redisTemplate.boundHashOps("seckillGoodsList").put(id, seckillGoods);
                }
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setId(idWorker.nextId());
                seckillOrder.setSeckillId(id);
                seckillOrder.setMoney(seckillGoods.getCostPrice());
                seckillOrder.setUserId(userName);
                seckillOrder.setSellerId(seckillGoods.getSellerId());
                seckillOrder.setCreateTime(new Date());
                seckillOrder.setStatus("0");
                // 保存订单到 Redis
                redisTemplate.boundHashOps("seckillOrderList")
                        .put(userName,seckillOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public SeckillOrder findSeckillOrderFromRedis(String userId) {
        return (SeckillOrder) redisTemplate.boundHashOps("seckillOrderList")
                .get(userId);
    }

    @Override
    public void updateOrderStatus(String userId, String transactionId) {
        try {
            SeckillOrder seckillOrder= (SeckillOrder) redisTemplate.boundHashOps("seckillOrderList")
                    .get(userId);
            if (seckillOrder!=null){
                seckillOrder.setPayTime(new Date());
                seckillOrder.setStatus("1");
                seckillOrder.setTransactionId(transactionId);
                seckillOrderMapper.insertSelective(seckillOrder);
/** 删除 Redis 中的订单 */
                redisTemplate.boundHashOps("seckillOrderList").delete(userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
