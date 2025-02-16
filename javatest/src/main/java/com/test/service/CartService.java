package com.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.Cart;
import com.test.pojo.response.ResponseCart;

import java.util.List;
import java.util.Map;

public interface CartService extends IService<Cart> {
    Cart getCartMsgById(String userId);

    Boolean batchAddCart(List<Cart> cartList);

    List<ResponseCart> GetCart(String userId);
}
