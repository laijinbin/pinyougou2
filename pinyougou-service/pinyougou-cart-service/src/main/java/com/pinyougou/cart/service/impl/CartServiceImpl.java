package com.pinyougou.cart.service.impl;
import java.math.BigDecimal;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.util.CookieUtils;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Cart;
import com.pinyougou.pojo.Item;
import com.pinyougou.pojo.OrderItem;
import com.pinyougou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceName ="com.pinyougou.service.CartService")
@Transactional
public class CartServiceImpl implements CartService {


    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Cart> addCart(List<Cart> cartList, Long itemId, Integer num) {
        try {
            List<Cart> list=new ArrayList<>();
            //得到SKU对象
            Item item=itemMapper.selectByPrimaryKey(itemId);

            //获取商家ID
            String sellerId = item.getSellerId();
            //据商家ID判断购物车集合中是否存在该商家的购物车
            Cart cart=searchCartBySellerId(cartList,sellerId);
            //当集合中不存在商家
            if (cart==null){
                cart=new Cart();
                cart.setSellerId(sellerId);
                cart.setSellerName(item.getSeller());
                //创建一个商品订单明细
                OrderItem orderItems=createOrderItem(item,num);
                //添加到购物车集合中
                List<OrderItem> orderItemList=new ArrayList<>();
                orderItemList.add(orderItems);
                cart.setOrderItems(orderItemList);
                cartList.add(cart);
            }else {//购物车中存在该商家的购物车
                // 判断购物车订单明细集合中是否存在该商品
                OrderItem orderItem=searchOrderItemByItemId(cart.getOrderItems(),itemId);
                //购物车订单明细集合中不存在该商品
                if (orderItem==null){
                    OrderItem newOrderItem=createOrderItem(item,num);
                    cart.getOrderItems().add(newOrderItem);
                }else {//如果存在改商品
                    orderItem.setNum(orderItem.getNum()+num);
                    orderItem.setTotalFee(new BigDecimal(
                            orderItem.getPrice().doubleValue()
                                    * orderItem.getNum()));
                    //判断商品数量
                    if (orderItem.getNum()<=0){
                        cart.getOrderItems().remove(orderItem);
                    }
                    if (cart.getOrderItems().size()==0){
                        cartList.remove(cart);
                    }
                }
            }

            return cartList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Cart> findCartRedis(String userName) {
        List<Cart> cartList= (List<Cart>) redisTemplate.boundValueOps("cart_" +userName).get();
       //要判断是否为空
        if (cartList==null){
            cartList=new ArrayList<>();
        }
        return cartList;
    }

    @Override
    public void saveCartRedis(String userName, List<Cart> cartList) {
        redisTemplate.boundValueOps("cart_"+userName).set(cartList);
    }

    @Override
    public List<Cart> mergeCart(List<Cart> cartList, List<Cart> cookieCarts) {
        for (Cart cookieCart : cookieCarts) {
            for (OrderItem orderItem : cookieCart.getOrderItems()) {
                cartList=addCart(cartList,orderItem.getItemId(),orderItem.getNum());
            }
        }
        return cartList;
    }


    private OrderItem searchOrderItemByItemId(List<OrderItem> orderItems, Long itemId) {
        for (OrderItem item : orderItems) {
            if (item.getItemId().equals(itemId)){
                return item;
            }
        }
        return null;
    }

    private OrderItem createOrderItem(Item item, Integer num) {
        OrderItem orderItem=new OrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setTotalFee(new BigDecimal("0"));
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
        return orderItem;
    }

    private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
        Cart cart=new Cart();
        for (Cart cart1 : cartList) {
            if (cart1.getSellerId().equals(sellerId)){
                //购物车集合存在商家集合
                return cart1;
            }
        }
        return null;
    }
}
