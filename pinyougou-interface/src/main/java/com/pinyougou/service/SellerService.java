package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Seller;

import java.util.Map;

public interface SellerService {
    void save(Seller seller);

    PageResult<Seller> findByPage(Integer page, Integer rows, Seller seller);

    void updateStatus(String sellerId, String status);
    Seller findOne(String id);

    void updateSeller(Seller seller);

    Map<String,Object> alertPwd(String sellerId, String oldPwd, String newPwd);
}
