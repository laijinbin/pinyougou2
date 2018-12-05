package com.pinyougou.search.listener;
import java.math.BigDecimal;
import java.util.*;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.Item;
import com.pinyougou.service.GoodsService;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public class ItemMessageListener implements SessionAwareMessageListener<ObjectMessage> {


    @Reference(timeout = 30000)
    private GoodsService goodsService;

    @Reference(timeout = 30000)
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(ObjectMessage objectMessage, Session session)
            throws JMSException {
        //获得消息
        Long[] ids= (Long[]) objectMessage.getObject();
        List<Item> list=goodsService.findItemByGoodsId(ids);
        List<SolrItem> solrItems=new ArrayList<>();
        if (list.size()>0){
            for (Item item : list) {
                SolrItem solrItem=new SolrItem();
                solrItem.setTitle(item.getTitle());
                solrItem.setPrice(item.getPrice());
                solrItem.setImage(item.getImage());
                solrItem.setGoodsId(item.getGoodsId());
                solrItem.setCategory(item.getCategory());
                solrItem.setBrand(item.getBrand());
                solrItem.setSeller(item.getSeller());
                solrItem.setUpdateTime(item.getUpdateTime());
                solrItem.setSpecMap(JSON.parseObject(item.getSpec(),Map.class));
                solrItem.setId(item.getId());
                solrItems.add(solrItem);
            }
            itemSearchService.saveOrUpdate(solrItems);
        }

    }
}
