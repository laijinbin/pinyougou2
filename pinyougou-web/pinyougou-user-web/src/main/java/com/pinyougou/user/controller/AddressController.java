package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Address;
import com.pinyougou.pojo.Areas;
import com.pinyougou.pojo.Cities;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference(timeout = 10000)
    private AddressService addressService;

    @GetMapping("/findAddress")
    public List<Address> findAddress(HttpServletRequest request) {
        String userId = request.getRemoteUser();
        return addressService.findAddressByUser(userId);
    }

    @GetMapping("/deleteAddress")
    public boolean deleteAddress(Long id) {
        try {
            addressService.deleteAddress(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Address address, HttpServletRequest request) {
        try {
            address.setIsDefault("0");
            address.setUserId(request.getRemoteUser());
            addressService.save(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping("/update")
    public boolean update(@RequestBody Address address) {
        try {
            addressService.update(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/setDefault")
    public boolean setDefault(Long id, HttpServletRequest request) {
        try {
            String userId = request.getRemoteUser();
            addressService.setDefault(userId, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @GetMapping("/findProvinceList")
    public List<Provinces> findProvinceList(){
        return addressService.findProvinceList();
    }
    @GetMapping("/findCity")
    public List<Cities> findCity(String provinceId){
        return addressService.findCity(provinceId);
    }
    @GetMapping("/findArea")
    public List<Areas> findArea(String cityId){
        return addressService.findArea(cityId);
    }

}
