package com.pinyougou.manager.controller;



import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.ContentCategory;
import com.pinyougou.service.ContentCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class ContentCategoryController {

    @Reference(timeout = 10000)
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/findByPage")
    public PageResult findByPage(Integer page, Integer rows,
                                 ContentCategory contentCategory){
        try {
            return contentCategoryService.findByPage(page,rows,contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @PostMapping("/save")
    public boolean save(@RequestBody ContentCategory contentCategory){
        try {
            contentCategoryService.save(contentCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/update")
    public boolean update(@RequestBody ContentCategory contentCategory){
        try {
            contentCategoryService.update(contentCategory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @GetMapping("/delete")
    public boolean delete(Long[] ids){
        try {
            contentCategoryService.delete(ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @GetMapping("/findAllContentCategory")
    public List<ContentCategory> findAllContentCategory(){
        return contentCategoryService.findAllContentCategory();
    }
}
