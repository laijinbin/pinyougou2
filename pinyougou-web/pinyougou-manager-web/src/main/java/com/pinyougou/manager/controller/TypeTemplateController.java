package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.awt.*;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference(timeout = 10000)
    private TypeTemplateService typeTemplateService;



    @GetMapping("/findByPage")
    public PageResult<TypeTemplate> findByPage(TypeTemplate typeTemplate,Integer page,Integer rows){
      try{
          typeTemplate.setName(new String(typeTemplate.getName().getBytes("ISO8859-1"),
                  "utf-8"));
      }catch (Exception e){
          e.printStackTrace();
      }
        return typeTemplateService.findByPage(typeTemplate,page,rows);
    }

    @PostMapping("/save")
    public boolean save(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.save(typeTemplate);
        return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @PostMapping("/update")
    public boolean update(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.update(typeTemplate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try {
            typeTemplateService.delete(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @GetMapping("/findTypeTemplateList")
    public List<Map<String,Object>> findTypeTemplateList(){
       return typeTemplateService.findTypeTemplateList();
    }
}
