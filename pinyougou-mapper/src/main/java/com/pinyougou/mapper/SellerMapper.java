package com.pinyougou.mapper;

import com.pinyougou.pojo.Seller;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SellerMapper extends Mapper<Seller> {
    List<Seller> findByPage(Seller seller);

    String findPwdById(String sellerId);


    void alertPwd(@Param("sellerId") String sellerId, @Param("bcPwd") String bcPwd);
}
