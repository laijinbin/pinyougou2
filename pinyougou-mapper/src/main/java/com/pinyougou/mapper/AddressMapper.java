package com.pinyougou.mapper;

import com.pinyougou.pojo.Address;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AddressMapper extends Mapper<Address> {
    void updateDefault(@Param("userId") String userId,@Param("s1") String s1,@Param("s0") String s0);

    List<Address> findAddressByUser(String userId);
}
