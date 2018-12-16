package com.pinyougou.mapper;

import com.pinyougou.pojo.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    String getPwdByUserId(String userId);

    void savePwd(@Param("userId") String userId,@Param("newPwd") String newPwd);

    void updatePhone(@Param("userId") String userId,@Param("newPhone") String newPhone);
}
