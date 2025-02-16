package com.test.service.serviceimpl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mapper.CartMapper;
import com.test.pojo.Cart;
import com.test.pojo.Product;
import com.test.pojo.response.ResponseCart;
import com.test.service.CartService;
import com.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Autowired
    private CartMapper cartMapper;


    @Override
    public Cart getCartMsgById(String userId) {
        return cartMapper.selectById(userId);
    }

    @Override
    public Boolean batchAddCart(List<Cart> cartList) {
        return this.saveBatch(cartList);
    }

    /**
     * 根据sku_id找到product表相同值的id，
     * 找到product的price，desc，product_name，
     * 根据postman传入的user_id最终返回product的price，desc，product_name和cart的quantity
     * @param userId
     * @return
     */
    @Override
    public List<ResponseCart> GetCart(String userId) {
        return cartMapper.QueryCart(userId);
    }
}
