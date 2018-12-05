package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service(interfaceName ="com.pinyougou.service.UserService" )
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;



    @Autowired
    private UserMapper userMapper;
    @Override
    public void save(User user) {
        try {
            user.setCreated(new Date());
            user.setUpdated(user.getCreated());
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            userMapper.insertSelective(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    @Override
    public boolean  sendCode(String phone) {
        try {
            String code= UUID.randomUUID().toString().replaceAll("-","")
                  .replaceAll("[a-z|A-Z]","").substring(0,6);
            HttpClientUtils httpClientUtils=new HttpClientUtils(false);
            Map<String,String> map=new HashMap<>();
            map.put("phone",phone);
            map.put("signName",signName);
            map.put("templateCode",templateCode);
            map.put("templateParam","{\"number\":\""+code+"\"}");
            String s = httpClientUtils.sendPost(smsUrl, map);
            Map<String,Object> resMap  = JSON.parseObject(s, Map.class);
            //存入redis中90s
            redisTemplate.boundValueOps(phone).set(code,90, TimeUnit.SECONDS);
            return (boolean)resMap.get("success");

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCode(String phone,String smsCode) {
       String sysCode=redisTemplate.boundValueOps(phone).get();

        return StringUtils.isNoneBlank(sysCode) && sysCode.equals(smsCode);

    }
}
