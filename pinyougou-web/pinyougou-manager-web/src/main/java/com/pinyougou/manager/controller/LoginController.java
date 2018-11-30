package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/findUserName")
    public Map<String,String> findUserName(){
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,String> map=new HashMap<>();
        map.put("loginName",loginName);
        map.put("lastLoginTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return map;
    }

}
