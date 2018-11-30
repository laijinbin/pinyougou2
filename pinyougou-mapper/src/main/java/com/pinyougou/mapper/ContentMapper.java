package com.pinyougou.mapper;

import com.pinyougou.pojo.Content;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ContentMapper extends Mapper<Content> {
    List<Content> findContentByCategoryId(@Param("categoryId") Long categoryId, @Param("status") String status);
}
