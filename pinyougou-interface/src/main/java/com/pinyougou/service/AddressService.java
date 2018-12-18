package com.pinyougou.service;

import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;

import java.util.List;

public interface AddressService {
    List<Address> findAddressByUser(String userId);

    void deleteAddress(Long id);

    void save(Address address);

    void update(Address address);

    void setDefault(String userId, Long id);

    List<Provinces> findProvinceList();

    List<Cities> findCity(String provinceId);

    List<Areas> findArea(String cityId);
}
