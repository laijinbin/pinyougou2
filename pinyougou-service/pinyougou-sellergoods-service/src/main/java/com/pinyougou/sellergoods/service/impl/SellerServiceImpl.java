package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        PageInfo<Seller> pageInfo = null;
        pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                sellerMapper.findByPage(seller);
            }
        });
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        try {
            Seller seller = new Seller();
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

    @Override
    public void updateSeller(Seller seller) {
        try {
            sellerMapper.updateByPrimaryKeySelective(seller);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> alertPwd(String sellerId, @RequestBody String oldPwd, @RequestBody String newPwd) {
        Map<String, Object> map = new HashMap<>();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        map.put("flag", "false");
        try {
            String pwd = sellerMapper.findPwdById(sellerId);
            if (bCryptPasswordEncoder.matches(oldPwd, pwd)) {
                map.put("flag", "true");
                String bcPwd=bCryptPasswordEncoder.encode(newPwd);
                sellerMapper.alertPwd(sellerId,bcPwd);
                map.put("msg", "修改成功，请重新登录");
            } else {
                map.put("msg", "原密码错误，请重新输入");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg","服务器忙");
        }
        return map;
    }
}
