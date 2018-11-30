package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.*;
import com.pinyougou.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference(timeout = 10000)
    private GoodsService goodsService;

    @Reference(timeout = 10000)
    private ItemCatService itemCatService;
    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;

    @Reference(timeout = 10000)
    private SpecificationOptionService specificationOptionService;

    @PostMapping("/save")
    public boolean save(@RequestBody Goods goods){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.setSellerId(name);
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/findItemCatList")
    public List<ItemCat> findItemCatList(@RequestParam(value = "parentId",defaultValue = "0")
                                         Long parentId){
        return itemCatService.findItemCatByParentId(parentId);
    }

    @GetMapping("/findBrandList")
    public TypeTemplate findBrandList(Long id){
        return typeTemplateService.findBrandList(id);
    }

    @GetMapping("/findspecificationItemsOption")
    public List<SpecificationOption> findspecificationItemsOption(Long[] specId){
        return specificationOptionService.findspecificationItemsOption(specId);
    }

    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows, Goods goods){
        String sellerId = SecurityContextHolder.getContext()
                .getAuthentication().getName();
/** 添加查询条件 */
        goods.setSellerId(sellerId);
/** GET 请求中文转码 */
        if (StringUtils.isNoneBlank(goods.getGoodsName())) {
            try {
                goods.setGoodsName(new String(goods
                        .getGoodsName().getBytes("ISO8859-1"), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return goodsService.findByPage(page,rows,goods);
    }

    @RequestMapping("/updateMarketable")
    public boolean updateMarketable(Long[] ids,String marketable){
        try {
            goodsService.updateMarketable(ids,marketable);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
