package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.AddressMapper;
import com.pinyougou.mapper.AreasMapper;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(interfaceName = "com.pinyougou.service.AddressService")
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ProvincesMapper provincesMapper;
    @Autowired
    private CitiesMapper citiesMapper;
    @Autowired
    private AreasMapper areasMapper;

    @Override
    public List<Address> findAddressByUser(String userId) {
        List<Address> addressList=addressMapper.findAddressByUser(userId);
        for (Address address : addressList) {
            String provinceName=provincesMapper.findProvinceName(address.getProvinceId());
            address.setProvinceName(provinceName);
            String cityName=citiesMapper.findCityName(address.getCityId());
            address.setCityName(cityName);
            String areaName=areasMapper.findAreaName(address.getTownId());
            address.setAreaName(areaName);
        }
        return addressList;
    }

    @Override
    public void deleteAddress(Long id) {
        try {
            addressMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Address address) {
        try {
            addressMapper.insertSelective(address);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Address address) {
        try {
            addressMapper.updateByPrimaryKeySelective(address);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setDefault(String userId, Long id) {
        try {
            addressMapper.updateDefault(userId, "1","0");
            Address newAddress = new Address();
            newAddress.setId(id);
            newAddress.setIsDefault("1");
            addressMapper.updateByPrimaryKeySelective(newAddress);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Provinces> findProvinceList() {
        return provincesMapper.selectAll();
    }

    @Override
    public List<Cities> findCity(String provinceId) {
        Cities cities=new Cities();
        cities.setProvinceId(provinceId);
        return citiesMapper.select(cities);
    }

    @Override
    public List<Areas> findArea(String cityId) {
        Areas areas=new Areas();
        areas.setCityId(cityId);
        return areasMapper.select(areas);
    }
}
