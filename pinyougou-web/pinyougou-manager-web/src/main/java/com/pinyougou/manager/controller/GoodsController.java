package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;

import com.pinyougou.service.GoodsService;
import jdk.nashorn.internal.runtime.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 10000)
    private GoodsService goodsService;

    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows, Goods goods){
        try {
            goods.setAuditStatus("0");
            if (StringUtils.isNoneBlank(goods.getGoodsName())){
                goods.setGoodsName(new String(goods.getGoodsName()
                .getBytes("ISO8859-1"),"utf-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goodsService.findByPage(page,rows,goods);
    }

    @GetMapping("/checkPass")
    public boolean checkPass(Long[] ids,String status){
        try {
            goodsService.checkPass(ids,status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/deleteGoods")
    public boolean deleteGoods(Long[] ids){
        try {
            goodsService.deleteGoods(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
