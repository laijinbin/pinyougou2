import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.pojo.Provinces;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class demo {
    @Autowired
    private ProvincesMapper provincesMapper;
    @Test
    public void test1(){
        Map<String,Map<String,String>> AddressMap=new HashMap<>();
        List<Provinces> provincesList=provincesMapper.selectAll();
        Map<String,String> provincesMap=new HashMap<>();
        for (Provinces provinces : provincesList) {
            provincesMap.put(provinces.getProvinceId(),provinces.getProvince());
        }
        AddressMap.put("80",provincesMap);
        System.out.println(AddressMap.toString());
    }

    @Test
    public void test2(){
       String s=DigestUtils.md5Hex("123456");
        System.out.println(s);

        //e10adc3949ba59abbe56e057f20f883e

    }
}
