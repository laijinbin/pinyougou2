package com.pinyougou.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Content;
import com.pinyougou.service.ContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference(timeout = 10000)
    private ContentService contentService;


    @GetMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows,Content content){
        try {
            return contentService.findByPage(page,rows,content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try {
            contentService.delete(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @PostMapping("/save")
    public boolean save(@RequestBody Content content){
        try {
            contentService.save(content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @PostMapping("/update")
    public boolean update(@RequestBody Content content){
        try {
            contentService.update(content);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
