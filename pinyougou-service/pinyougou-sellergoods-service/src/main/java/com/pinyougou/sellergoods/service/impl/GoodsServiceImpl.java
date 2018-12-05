package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.GoodsDesc;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


@Service(interfaceName = "com.pinyougou.service.GoodsService")
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Override
    public void save(Goods goods) {
        try {
            goods.setAuditStatus("0");
            goodsMapper.insertSelective(goods);
            goods.getGoodsDesc().setGoodsId(goods.getId());
            goodsDescMapper.insertSelective(goods.getGoodsDesc());
            if ("1".equals(goods.getIsEnableSpec())){
                for (Item item : goods.getItems()) {
                    StringBuilder title=new StringBuilder();
                    title.append(goods.getGoodsName());
                    /* 把规格选项 JSON 字符串转化成 Map 集合 */
                    Map<String,Object> spec= JSON.parseObject(item.getSpec());
                    for (Object value : spec.values()) {
                        title.append(" "+value);
                    }
                    item.setTitle(title.toString());
                    setItemInfo(item, goods);
                 itemMapper.insertSelective(item);
                }
            }else {
                Item item = new Item();
/** 设置 SKU 商品的标题 */
                item.setTitle(goods.getGoodsName());
/** 设置 SKU 商品的价格 */
                item.setPrice(goods.getPrice());
/** 设置 SKU 商品库存数据 */
                item.setNum(9999);
/** 设置 SKU 商品启用状态 */
                item.setStatus("1");
/** 设置是否默认*/
                item.setIsDefault("1");
/** 设置规格选项 */
                item.setSpec("{}");
/** 设置 SKU 商品其它属性 */
                setItemInfo(item, goods);
                itemMapper.insertSelective(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResult<Goods> findByPage(Integer page, Integer rows, Goods goods) {
        PageInfo<Map<String,Object>> pageInfo=null;
        try {
            pageInfo= PageHelper.startPage(page,rows).doSelectPageInfo(
                    new ISelect() {
                        @Override
                        public void doSelect() {
                           goodsMapper.findByPage(goods);
                        }
                    });
            for (Map<String, Object> map : pageInfo.getList()) {
                Long category3Id= (Long) map.get("category3Id");
                if (category3Id!=null && category3Id>0){
                ItemCat itemCat1=itemCatMapper.selectByPrimaryKey(map.get("category1Id"));
                ItemCat itemCat2=itemCatMapper.selectByPrimaryKey(map.get("category2Id"));
                ItemCat itemCat3=itemCatMapper.selectByPrimaryKey(map.get("category3Id"));
                map.put("category1Name",itemCat1.getName());
                map.put("category2Name",itemCat2.getName());
                map.put("category3Name",itemCat3.getName());
                }
            }
                return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void checkPass(Long[] ids,String status) {
        try {
            goodsMapper.checkPass(ids,status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteGoods(Long[] ids) {
        try {
            goodsMapper.deleteGoods(ids,"1");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMarketable(Long[] ids, String marketable) {
        try {
            goodsMapper.updateMarketable(ids,marketable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> getGoods(Long goodsId) {
        Map<String, Object> data= null;
        try {
            data = new HashMap<>();
            Goods goods=goodsMapper.selectByPrimaryKey(goodsId);
            data.put("goods",goods);
            GoodsDesc goodsDesc=goodsDescMapper.selectByPrimaryKey(goodsId);
            data.put("goodsDesc",goodsDesc);
            if (goods!=null && goods.getCategory3Id()!=null){
                String itemCat1=itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
                String itemCat2=itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
                String itemCat3=itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
            data.put("itemCat1",itemCat1);
            data.put("itemCat2",itemCat2);
            data.put("itemCat3",itemCat3);
            }
            //查询sku
            Example example=new Example(Item.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("status",'1');
            criteria.andEqualTo("goodsId",goodsId);
            example.orderBy("isDefault").desc();
            List<Item> itemList = itemMapper.selectByExample(example);
            data.put("itemList", JSON.toJSONString(itemList));
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> findItemByGoodsId(Long[] ids) {
        try{
            /** 创建示范对象 */
            Example example = new Example(Item.class);
            /** 创建查询条件对象 */
            Example.Criteria criteria = example.createCriteria();
            /** 添加in查询条件 */
            criteria.andIn("goodsId", Arrays.asList(ids));
            /** 查询数据 */
            return itemMapper.selectByExample(example);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }

    private void setItemInfo(Item item, Goods goods){
        List<Map> imageList=JSON.parseArray(goods.getGoodsDesc().getItemImages(),Map.class);
        if (imageList!=null && imageList.size()>0){
            item.setImage((String) imageList.get(0).get("url"));
        }
        item.setCategoryid(goods.getCategory3Id());
        item.setCreateTime(new Date());
        item.setUpdateTime(item.getCreateTime());
        item.setGoodsId(goods.getId());
        item.setSellerId(goods.getSellerId());
        item.setCategory(itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName());
        item.setBrand(brandMapper.selectByPrimaryKey(goods.getBrandId()).getName());
        item.setSeller(sellerMapper.selectByPrimaryKey(goods.getSellerId()).getNickName());
    }
}
