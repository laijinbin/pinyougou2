package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.mapper.SeckillOrderMapper;
import com.pinyougou.pojo.SeckillGoods;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.SeckillGoodsService")
@Transactional
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;



    @Override
    public List<SeckillGoods> findSeckillGoods() {
        List<SeckillGoods> seckillGoodsList = null;
        try {
            seckillGoodsList = redisTemplate.boundHashOps("seckillGoodsList").values();
            if (seckillGoodsList != null && seckillGoodsList.size() > 0) {
                return seckillGoodsList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            /** 创建示范对象 */
            Example example = new Example(SeckillGoods.class);
/** 创建条件查询对象 */
            Example.Criteria criteria = example.createCriteria();
/** 审核通过 */
            criteria.andEqualTo("status", "1");
            //库存大于0
            criteria.andGreaterThan("stockCount", 0);
            criteria.andLessThanOrEqualTo("startTime", new Date());
            /** 结束时间大于等于当前时间 */
            criteria.andGreaterThanOrEqualTo("endTime", new Date());
            seckillGoodsList = seckillGoodsMapper.selectByExample(example);
            try {
                for (SeckillGoods seckillGoods : seckillGoodsList) {
                    redisTemplate.boundHashOps("seckillGoodsList")
                            .put(seckillGoods.getId(), seckillGoods);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return seckillGoodsList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public SeckillGoods findOne(Long id) {
        try {
            return (SeckillGoods) redisTemplate.boundHashOps("seckillGoodsList").get(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
