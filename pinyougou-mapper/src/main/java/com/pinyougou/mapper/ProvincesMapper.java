package com.pinyougou.mapper;

import com.pinyougou.pojo.Provinces;
import tk.mybatis.mapper.common.Mapper;

public interface ProvincesMapper extends Mapper<Provinces> {
    String findProvinceName(String provinceId);
}
