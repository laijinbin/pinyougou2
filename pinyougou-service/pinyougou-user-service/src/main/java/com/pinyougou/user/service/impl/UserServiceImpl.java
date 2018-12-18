package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.HttpClientUtils;
import com.pinyougou.mapper.AreasMapper;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import tk.mybatis.mapper.entity.Example;

import javax.print.DocFlavor;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service(interfaceName = "com.pinyougou.service.UserService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.templateCode}")
    private String templateCode;

    @Autowired
    private ProvincesMapper provincesMapper;
    @Autowired
    private CitiesMapper citiesMapper;
    @Autowired
    private AreasMapper areasMapper;


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
    public boolean sendCode(String phone) {
        try {
            String code = UUID.randomUUID().toString().replaceAll("-", "")
                    .replaceAll("[a-z|A-Z]", "").substring(0, 6);
            HttpClientUtils httpClientUtils = new HttpClientUtils(false);
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("signName", signName);
            map.put("templateCode", templateCode);
            map.put("templateParam", "{\"number\":\"" + code + "\"}");
            String s = httpClientUtils.sendPost(smsUrl, map);
            Map<String, Object> resMap = JSON.parseObject(s, Map.class);
            //存入redis中90s
            redisTemplate.boundValueOps(phone).set(code, 90, TimeUnit.SECONDS);
            return (boolean) resMap.get("success");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCode(String phone, String smsCode) {
        String sysCode = redisTemplate.boundValueOps(phone).get();

        return StringUtils.isNoneBlank(sysCode) && sysCode.equals(smsCode);

    }

    @Override
    public Map<String, String> savePwd(String userId, Map<String, String> password) {
        Map<String, String> map = new HashMap<>();
        try {
            String pwd = userMapper.getPwdByUserId(userId);
            map.put("flag", "false");
            if (pwd.equals(DigestUtils.md5Hex(password.get("oldPwd")))) {
                String newPwd = DigestUtils.md5Hex(password.get("newPwd"));
                userMapper.savePwd(userId, newPwd);
                map.put("msg", "修改成功，请重新登录谢谢");
                map.put("flag", "true");
            } else {
                map.put("msg", "原密码错误，请重新输入");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "服务器忙");
        }
        return map;
    }

    @Override
    public User findUserById(String remoteUser) {
        User user = new User();
        user.setUsername(remoteUser);
        return userMapper.selectOne(user);
    }

    @Override
    public Map<String, Object> goNewPhone(String userId,String checkCode, Map<String, String> allcode) {
        Map<String, Object> map = new HashMap<>();
        map.put("flag", true);
        map.put("msg","全部正确");
        try {
            if (!checkCode.equalsIgnoreCase(allcode.get("code"))) {
                map.put("flag", false);
                map.put("msg", "验证码错误");
            }
            if (!checkCode(allcode.get("phone"), allcode.get("msgcode"))) {
                map.put("msg", "短信验证码错误");
                map.put("flag", false);
            }
            if ("2".equals(allcode.get("step"))){
                String newPhone=allcode.get("phone");
                userMapper.updatePhone(userId,newPhone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "服务器忙");
            map.put("flag", false);
        }
        return map;
    }

    @Override
    public void updateUser(User user) {
        try {
            userMapper.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUser(String username) {
        User user=new User();
        user.setUsername(username);
        return userMapper.selectOne(user);

    }
}
