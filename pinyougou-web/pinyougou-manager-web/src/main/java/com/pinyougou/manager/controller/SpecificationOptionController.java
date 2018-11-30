package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.service.SpecificationOptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specificationOption")
public class SpecificationOptionController {

    @Reference(timeout = 10000)
    private SpecificationOptionService specificationOptionService;


    @GetMapping("/findById")
    public List<SpecificationOption> findById(Long id) {
        return specificationOptionService.findById(id);
    }

}
