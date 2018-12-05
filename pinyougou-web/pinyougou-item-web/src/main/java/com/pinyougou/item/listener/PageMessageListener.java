package com.pinyougou.item.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.GoodsService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

public class PageMessageListener implements SessionAwareMessageListener<ObjectMessage> {
   @Value("${page.dir}")
    private String pageDir;

   @Reference(timeout = 10000)
   private GoodsService goodsService;

   //获得模板对象
   @Autowired
   private FreeMarkerConfigurer freeMarkerConfigurer;


    @Override
    public void onMessage(ObjectMessage objectMessage, Session session) throws JMSException {
        try {
            Long[] ids =(Long[]) objectMessage.getObject();
            //获得模板对象
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("item.ftl");
        //定义模板文件需要的数据模型

            //输出到文件
            for (Long id : ids) {
            Map<String,Object> dataModel=goodsService.getGoods(id);
                OutputStreamWriter Writer=
                        new OutputStreamWriter(new FileOutputStream(pageDir+id+".html"),"UTF-8");
                template.process(dataModel,Writer);
                Writer.flush();
                Writer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
