package com.pinyougou.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {
    /** 注入身份认证管理器 */
    @GetMapping("/showName")
    public Map<String,String> showName(){
        /** 获取用户登录名 */
        String name = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        Map<String, String> map = new HashMap<>();
        map.put("loginName", name);
        return map;
    }


}
