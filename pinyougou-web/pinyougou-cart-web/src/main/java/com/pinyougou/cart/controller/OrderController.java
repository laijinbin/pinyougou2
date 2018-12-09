package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.pojo.Order;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Reference(timeout = 10000)
    private OrderService orderService;

    @Reference(timeout = 10000)
    private WeixinPayService weixinPayService;

    @PostMapping("/save")
    public boolean save(@RequestBody Order order, HttpServletRequest request){
        try {
            order.setUserId(request.getRemoteUser());
            order.setSourceType("2");
            orderService.save(order);
        return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/genPayCode")
    public Map<String,String> genPayCode(HttpServletRequest request){
        String userId = request.getRemoteUser();
        PayLog payLog = orderService.findPayLogFromRedis(userId);
        return weixinPayService.genPayCode(payLog.getOutTradeNo(),String.valueOf(payLog.getTotalFee()));
    }
    @GetMapping("/queryPayStatus")
    public Map<String,Integer> queryPayStatus(String outTradeNo){
        Map<String,Integer> data=new HashMap<>();
        data.put("status", 3);
        try {
            //查询订单
            Map<String,String> resMap=weixinPayService.queryPayStatus(outTradeNo);
            if (resMap != null && resMap.size() > 0) {
// 判断是否支付成功
                if ("SUCCESS".equals(resMap.get("trade_state"))) {
                    /** 修改订单状态 */
                    orderService.updateOrderStatus(outTradeNo,
                            resMap.get("transaction_id"));
                    data.put("status", 1);
                }
                if ("NOTPAY".equals(resMap.get("trade_state"))) {
                    data.put("status", 2);
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return data;
    }
}
