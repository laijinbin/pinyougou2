package com.pinyougou.search.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Item;
import com.pinyougou.service.GoodsService;
import com.pinyougou.service.ItemSearchService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.*;
import java.util.Arrays;
import java.util.List;

public class DeleteMessageListener implements SessionAwareMessageListener<ObjectMessage> {

    @Reference(timeout = 30000)
    private JmsTemplate jmsTemplate;



    @Reference
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(ObjectMessage objectMessage, Session session)
            throws JMSException {
        Long[] ids= (Long[]) objectMessage.getObject();
        itemSearchService.delete(Arrays.asList(ids));


    }
}
