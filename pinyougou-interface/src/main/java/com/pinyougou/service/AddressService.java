package com.pinyougou.service;

import com.pinyougou.pojo.Address;

import java.util.List;

public interface AddressService {
    List<Address> findAddressByUser(String userId);

    void deleteAddress(Long id);

    void save(Address address);

    void update(Address address);

    void setDefault(String userId, Long id);
}
