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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private SeckillOrderMapper seckillOrderMapper;

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
                        .put(userName, seckillOrder);
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
            SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("seckillOrderList")
                    .get(userId);
            if (seckillOrder != null) {
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

    @Override
    public List<SeckillOrder> findOrderByTimeout() {
        try {
            // 定义List集合封装超时5分钟未支付的订单
            List<SeckillOrder> seckillOrders = new ArrayList<>();
            List<Object> seckillOrderList = redisTemplate.
                    boundHashOps("seckillOrderList").values();
            if (seckillOrderList != null && seckillOrderList.size() > 0) {
                for (Object o : seckillOrderList) {
                    SeckillOrder seckillOrder = (SeckillOrder) o;
                    long endTime = new Date().getTime() - (5 * 60 * 1000);
                    if (seckillOrder.getCreateTime().getTime() < endTime) {
                        seckillOrders.add(seckillOrder);
                    }
                }

            }

            return seckillOrders;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteOrderFromRedis(SeckillOrder seckillOrder) {
        try {
            redisTemplate.boundHashOps("seckillOrderList").delete(seckillOrder.getUserId());
            /** ######## 恢复库存数量 #######*/
            // 从Redis查询秒杀商品
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate
                    .boundHashOps("seckillGoodsList")
                    .get(seckillOrder.getSeckillId());
            // 判断缓存中是否存在该商品
            if (seckillGoods != null){
                // 修改缓存中秒杀商品的库存
                seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);
            }else{ // 代表秒光,要考虑到如果数据库已经清0 的情况
                // 从数据库查询该商品
                seckillGoods = seckillGoodsMapper.
                        selectByPrimaryKey(seckillOrder.getSeckillId());
                // 设置秒杀商品库存数量
                seckillGoods.setStockCount(1);
            }
            // 存入缓存
            redisTemplate.boundHashOps("seckillGoodsList")
                    .put(seckillOrder.getSeckillId(), seckillGoods);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
