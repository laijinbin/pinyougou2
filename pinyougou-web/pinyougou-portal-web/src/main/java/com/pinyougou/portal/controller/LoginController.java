package com.pinyougou.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.cs.ext.MacArabic;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @GetMapping("/user/showName")
    public Map<String,Object> showName(HttpServletRequest request){
        String loginName=request.getRemoteUser();
        Map<String,Object> map=new HashMap<>();
        map.put("loginName",loginName);
        return map;
    }
}
