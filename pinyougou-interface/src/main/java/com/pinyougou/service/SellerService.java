package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Seller;

public interface SellerService {
    void save(Seller seller);

    PageResult<Seller> findByPage(Integer page, Integer rows, Seller seller);

    void updateStatus(String sellerId, String status);
    Seller findOne(String id);
}
