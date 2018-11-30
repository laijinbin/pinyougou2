package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.support.Parameter;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Brand;
import com.pinyougou.service.BrandService;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference(timeout = 10000)
    private BrandService brandService;

    @GetMapping("/findIdAndName")
    public List<Map<String,Object>> findIdAndName() {
        List<Map<String,Object>> list = brandService.findIdAndName();
        return list;
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Brand brand){
        try {
            brandService.save(brand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/update")
    public boolean update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/findByPage")
    public PageResult<Brand> findByPage(Brand brand, Integer page, Integer rows){
        try {
            brand.setName(new String(brand.getName()
                    .getBytes("ISO8859-1"),"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brandService.findByPage(brand,page,rows);
    }

    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try {
            brandService.delete(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
