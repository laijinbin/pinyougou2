package com.pinyougou.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;
import com.pinyougou.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserController {

    @Reference(timeout = 10000)
    private UserService userService;

    @PostMapping("/save")
    public boolean save(@RequestBody User user, String smsCode) {
        try {
            boolean ok = userService.checkCode(user.getPhone(), smsCode);
            if (ok) {
                userService.save(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @GetMapping("/sendCode")
    public boolean sendCode(String phone) {
        try {
            if (StringUtils.isNoneBlank(phone)) {
                userService.sendCode(phone);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/savePwd")
    public Map<String, String> savePwd(HttpServletRequest request, @RequestBody Map<String, String> password) {
        String userId = request.getRemoteUser();
        return userService.savePwd(userId, password);
    }

    @GetMapping("/findUserById")
    public User findUserById(HttpServletRequest request) {
        return userService.findUserById(request.getRemoteUser());
    }

    @PostMapping("/goNewPhone")
    public Map<String,Object> goNewPhone(HttpServletRequest request,@RequestBody Map<String, String> allcode) {
      String checkCode= (String) request.getSession().getAttribute("CHECKCODE_SERVER");
      String userId=request.getRemoteUser();
       return userService.goNewPhone(userId,checkCode,allcode);
    }

}
