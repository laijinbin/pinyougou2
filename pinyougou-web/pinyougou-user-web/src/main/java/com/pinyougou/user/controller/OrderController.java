package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.AllOrder;
import com.pinyougou.pojo.PayLog;
import com.pinyougou.service.OrderService;
import com.pinyougou.service.WeixinPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;
    @Reference
    private WeixinPayService weixinPayService;
    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows, HttpServletRequest request){
        try {
            String userId=request.getRemoteUser();
            if (StringUtils.isNoneBlank(userId)){
                return orderService.findByPage(page,rows,userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/genPayCode")
    public Map<String,String> genPayCode(String money,String orderId){
        long totalFen = (long)(Double.parseDouble(money)*100);
        return weixinPayService.genPayCode(orderId,String.valueOf(totalFen));
    }
    @GetMapping("/queryPayStatus")
    public Map<String,Integer> queryPayStatus(String money,HttpServletRequest request,String outTradeNo) {
       String userId=request.getRemoteUser();
        long totalFen = (long)(Double.parseDouble(money)*100);
        Map<String, Integer> data = new HashMap<>();
        data.put("status", 3);
        try {
            //查询订单
            Map<String, String> resMap = weixinPayService.queryPayStatus(outTradeNo);
            if (resMap != null && resMap.size() > 0) {
// 判断是否支付成功
                if ("SUCCESS".equals(resMap.get("trade_state"))) {
                    /** 修改订单状态 */
                    orderService.updateOrderStatus2(totalFen,userId,outTradeNo,
                            resMap.get("transaction_id"));
                    data.put("status", 1);
                }
                if ("NOTPAY".equals(resMap.get("trade_state"))) {
                    data.put("status", 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return data;
    }
}
