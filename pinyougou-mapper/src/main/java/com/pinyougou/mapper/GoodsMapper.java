package com.pinyougou.mapper;

import com.pinyougou.pojo.Goods;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends Mapper<Goods> {
    List<Map<String,Object>> findByPage(Goods goods);

    void checkPass(@Param("ids") Long[] ids, @Param("status") String status);

    void deleteGoods(@Param("ids") Long[] ids, @Param("s") String s);

    void updateMarketable(@Param("ids") Long[] ids, @Param("marketable") String marketable);


}
