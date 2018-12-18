package com.pinyougou.service;

import com.pinyougou.pojo.Provinces;
import com.pinyougou.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    void save(User user);

    boolean sendCode(String phone);

    boolean checkCode(String phone,String smsCode);


    Map<String,String> savePwd(String userId, Map<String, String> password);

    User findUserById(String remoteUser);

    Map<String,Object> goNewPhone(String userId,String checkCode, Map<String, String> allcode);

    void updateUser(User user);

    User findUser(String username);
}
