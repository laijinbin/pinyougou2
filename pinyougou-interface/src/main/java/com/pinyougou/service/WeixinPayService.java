package com.pinyougou.service;

import java.util.Map;

public interface WeixinPayService {
    Map<String,String> genPayCode(String l, String s);

    Map<String,String> queryPayStatus(String outTradeNo);
}
