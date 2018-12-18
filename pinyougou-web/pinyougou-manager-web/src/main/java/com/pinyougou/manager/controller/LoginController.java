package com.pinyougou.manager.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/findUserName")
    @ResponseBody
    public Map<String,String> findUserName(){
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,String> map=new HashMap<>();
        map.put("loginName",loginName);
        map.put("lastLoginTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return map;
    }
    @RequestMapping("/login")
    public String login(String username, String password,
                      String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (StringUtils.isNoneBlank(code)){
                if (code.equalsIgnoreCase((String) request.getSession().getAttribute("CHECKCODE_SERVER"))){
                    UsernamePasswordAuthenticationToken token
                            = new UsernamePasswordAuthenticationToken(username, password);
                    Authentication authenticate = authenticationManager.authenticate(token);
                    if (authenticate.isAuthenticated()) {
                        SecurityContextHolder.getContext().setAuthentication(authenticate);
                        return "redirect:/admin/index.html";
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        // 重定向到登录页面
        return "redirect:/login.html";
    }

}
