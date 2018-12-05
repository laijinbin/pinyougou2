package com.pinyougou.service;

import com.pinyougou.pojo.User;

public interface UserService {
    void save(User user);

    boolean sendCode(String phone);

    boolean checkCode(String phone,String smsCode);
}
