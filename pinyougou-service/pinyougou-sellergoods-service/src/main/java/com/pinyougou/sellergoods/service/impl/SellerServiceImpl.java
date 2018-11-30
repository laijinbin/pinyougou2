package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(interfaceName = "com.pinyougou.service.SellerService")
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public void save(Seller seller) {
        try {
            seller.setStatus("0");
            seller.setCreateTime(new Date());
            sellerMapper.insertSelective(seller);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public PageResult<Seller> findByPage(Integer page, Integer rows, Seller seller) {
        PageInfo<Seller> pageInfo=null;
        pageInfo= PageHelper.startPage(page,rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                sellerMapper.findByPage(seller);
            }
        });
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        try {
            Seller seller=new Seller();
            seller.setStatus(status);
            seller.setSellerId(sellerId);
            sellerMapper.updateByPrimaryKeySelective(seller);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Seller findOne(String id) {
        return sellerMapper.selectByPrimaryKey(id);
    }
}
