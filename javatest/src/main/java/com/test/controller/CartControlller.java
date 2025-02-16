package com.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.pojo.Cart;
import com.test.pojo.Result;
import com.test.pojo.response.ResponseCart;
import com.test.pojo.response.ResponseUser;
import com.test.service.CartService;
import com.test.service.ProductService;
import com.test.service.serviceimpl.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartControlller {
    @Autowired
    private CartService cartService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProductService productService;

    @GetMapping("/cartInfo")
    public Result<?> QueryCart(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
        List<ResponseCart> list=cartService.GetCart(responseUser.getUserId());
        return Result.success("购物车信息", list);
    }

    @PostMapping("/addCart")
    public Result<?> addCart(@RequestBody List<Cart> cart, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
        ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
        if (responseUser != null) {
            cart.forEach(cartInfo -> {
                cartInfo.setUserId(responseUser.getUserId());
                cartInfo.setCreatedBy(responseUser.getUserName());
            });
            Map<String, Object> map = new HashMap<>();
            for (Cart cartInfo : cart) {
                if (cartInfo.getQuantity() > productService.getById(cartInfo.getSkuId()).getStock()) {
                    map.put(productService.getById(cartInfo.getSkuId()).getProductName(), "商品库存不足");
                }
            }
            if(map.size()==0){
                cartService.batchAddCart(cart);
                return Result.success("添加购物车成功");
            }else{
                return Result.success("添加购物车失败,商品库存不足",map);
            }
        }
        return Result.error("未找到用户cookie,已退出登录状态");
    }




    @DeleteMapping("/deleteCart")
    public Result<?> deleteById(@RequestParam Long cartId, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(token);
                    if (responseUser != null) {
                        boolean deleteStatus=cartService.removeById(cartId);
                        if(deleteStatus){
                            return Result.success("删除成功");
                        }
                    }
                }
            }
        }
        return Result.error("未找到用户cookie,已退出登录状态");
    }
}
