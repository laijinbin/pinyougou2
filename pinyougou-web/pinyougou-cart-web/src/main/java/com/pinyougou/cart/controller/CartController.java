package com.pinyougou.cart.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.common.util.CookieUtils;
import com.pinyougou.pojo.Cart;
import com.pinyougou.pojo.Item;
import com.pinyougou.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 10000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;




    @GetMapping("/addCart")
    public boolean addCart(Long itemId, Integer num){
        try {
            String userName = request.getRemoteUser();
            List<Cart> cartList=findCart();
            cartList=cartService.addCart(cartList,itemId,num);
            if (StringUtils.isNoneBlank(userName)) {
                cartService.saveCartRedis(userName, cartList);
            } else {
                CookieUtils.setCookie(request,response,CookieUtils.CookieName.PINYOUGOU_CART,
                        JSON.toJSONString(cartList),3600 * 24,true);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @GetMapping("/findCart")
    private List<Cart> findCart() {
        String userName=request.getRemoteUser();
        List<Cart> cartList = null;
        if (StringUtils.isNoneBlank(userName)){
            //获得redis的购物车
            cartList=cartService.findCartRedis(userName);
            //获得cookie中的购物车
            String cookieValue = CookieUtils.getCookieValue(request,
                    CookieUtils.CookieName.PINYOUGOU_CART, true);
            if (StringUtils.isNoneBlank(cookieValue)){
                List<Cart> cookieCarts=JSON.parseArray(cookieValue,Cart.class);
                if (cookieCarts!=null && cookieCarts.size()>0){
                    cartList=cartService.mergeCart(cartList,cookieCarts);
                    cartService.saveCartRedis(userName,cartList);
                    CookieUtils.deleteCookie(request,response,CookieUtils.CookieName.PINYOUGOU_CART);
                }
            }
        }else {
            String cookieValue = CookieUtils.getCookieValue(request, CookieUtils.CookieName.PINYOUGOU_CART, true);
            if (StringUtils.isBlank(cookieValue)){
                cookieValue="[]";
            }
            cartList=JSON.parseArray(cookieValue,Cart.class);
        }

        return cartList;
    }
}
