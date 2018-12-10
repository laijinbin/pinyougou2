package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.SeckillOrder;
import com.pinyougou.service.SeckillOrderService;
import com.pinyougou.service.WeixinPayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class SeckillOrderController {

    @Reference(timeout = 10000)
    private SeckillOrderService seckillOrderService;
    @Reference
    private WeixinPayService weixinPayService;

    @GetMapping("/submitOrder")
    public boolean submitOrder(HttpServletRequest request, Long id){
        try {
            String userName=request.getRemoteUser();
            seckillOrderService.submitOrder(userName,id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @GetMapping("/genPayCode")
    public Map<String,String> genPayCode(HttpServletRequest request){
        String userId = request.getRemoteUser();
        SeckillOrder seckillOrder = seckillOrderService.findSeckillOrderFromRedis(userId);
        long totalFen = (long)(seckillOrder.getMoney().doubleValue() * 100);
        return weixinPayService.genPayCode(String.valueOf(seckillOrder.getId()),String.valueOf(totalFen));
    }
    @GetMapping("/queryPayStatus")
    public Map<String,Integer> queryPayStatus(String outTradeNo,
                                              HttpServletRequest request){
        Map<String,Integer> data=new HashMap<>();
        data.put("status", 3);
        try {
            //查询订单
            Map<String,String> resMap=weixinPayService.queryPayStatus(outTradeNo);
            if (resMap != null && resMap.size() > 0) {
// 判断是否支付成功
                if ("SUCCESS".equals(resMap.get("trade_state"))) {
                    // 获取登录用户名
                    String userId = request.getRemoteUser();
                    /** 修改订单状态 */
                    seckillOrderService.updateOrderStatus(userId,
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
