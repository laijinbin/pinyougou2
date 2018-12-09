package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.WeixinPayService")
@Transactional
public class WeixinPayServiceImpl implements WeixinPayService {
    /** 微信公众号 */
    @Value("${appid}")
    private String appid;
    /** 商户账号 */
    @Value("${partner}")
    private String partner;
    /** 商户密钥 */
    @Value("${partnerkey}")
    private String partnerkey;
    /** 统一下单请求地址 */
    @Value("${unifiedorder}")
    private String unifiedorder;
    /** 订单查询请求地址 */
    @Value("${orderquery}")
    private String orderquery;
    @Override
    public Map<String, String> genPayCode(String l, String s) {
        Map<String,String> map=new HashMap<>();
        map.put("appid",appid);
        map.put("mch_id",partner);
        //随机字符创
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        map.put("body","品优购");
        /** 商户订单交易号 */
        map.put("out_trade_no", l);
        map.put("total_fee",s);
        /** IP 地址 */
        map.put("spbill_create_ip", "127.0.0.1");
        /** 回调地址(随意写) */
        map.put("notify_url", "http://test.itcast.cn");
        /** 交易类型 */
        map.put("trade_type", "NATIVE");
        try {
            /** 根据商户密钥签名生成 XML 格式请求参数 */
            String xmlParam = WXPayUtil.generateSignedXml(map, partnerkey);
            System.out.println("请求参数：" + xmlParam);
            /** 创建 HttpClientUtils 对象发送请求 */
            HttpClientUtils client = new HttpClientUtils(true);
            /** 发送请求，得到响应数据 */
            String result = client.sendPost(unifiedorder, xmlParam);
            System.out.println("响应数据：" + result);
/** 将响应数据 XML 格式转化成 Map 集合 */
            Map<String,String> resultMap = WXPayUtil.xmlToMap(result);
/** 创建 Map 集合封装返回数据 */
            Map<String,String> data = new HashMap<>();
/** 支付地址(二维码中的 URL) */
            data.put("codeUrl", resultMap.get("code_url"));
/** 总金额 */
            data.put("totalFee", s);
/** 订单交易号 */
            data.put("outTradeNo", l);
            return data;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    @Override
    public Map<String, String> queryPayStatus(String outTradeNo) {
        /** 创建 Map 集合封装请求参数 */
        Map<String, String> param = new HashMap<>(5);
/** 公众号 */
        param.put("appid", appid);
/** 商户号 */
        param.put("mch_id", partner);
/** 订单交易号 */
        param.put("out_trade_no", outTradeNo);
/** 随机字符串 */
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        try{
            //请求参数
            String s = WXPayUtil.generateSignedXml(param, partnerkey);
            HttpClientUtils httpClientUtils=new HttpClientUtils(true);
            String result = httpClientUtils.sendPost(orderquery, s);
            return WXPayUtil.xmlToMap(result);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }
}
