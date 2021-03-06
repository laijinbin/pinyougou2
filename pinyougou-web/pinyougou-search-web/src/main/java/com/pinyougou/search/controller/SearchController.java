package com.pinyougou.search.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.ItemSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SearchController {

    @Reference(timeout = 10000)
    private ItemSearchService itemSearchService;

    @PostMapping("/Search")
    public Map<String, Object> Search(@RequestBody Map<String,Object> params){
        return itemSearchService.Search(params);
    }




}
