package com.test.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mapper.CartMapper;
import com.test.mapper.OrderMapper;
import com.test.mapper.ProductMapper;
import com.test.pojo.Cart;
import com.test.pojo.Order;
import com.test.pojo.Product;
import com.test.pojo.response.ResponseOrder;
import com.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;


    @Override
    public ResponseOrder create(String userId) {
//        List<Cart> carts = cartMapper.selectList(new QueryWrapper<Cart>().eq("user_id", userId));
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Cart> carts = cartMapper.selectList(queryWrapper);
        // 从购物车中获取用户的商品

        BigDecimal totalPrice = BigDecimal.ZERO;
//        String productName="";
        List<String> productlist=new ArrayList<>();
        for (Cart cart : carts) {
            Product product = productMapper.findProductById(cart.getSkuId());
            if (product != null) {
                totalPrice=totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
//                productName = product.getProductName();
                productlist.add(product.getProductName());
            }
        }
//        Order order = new Order();
//        order.setTotalPrice(totalPrice);
//        orderMapper.insert(order);
        // 返回响应体
        ResponseOrder responseOrder = new ResponseOrder();
        responseOrder.setTotalPrice(totalPrice);
        responseOrder.setProductName(productlist);
        return responseOrder;
    }


    @Override
    public int getTodayOrderCount() {
        LocalDate today = LocalDate.now();
        return orderMapper.countTodayOrders(today);
    }

    @Override
    public BigDecimal getTodaySalesAmount() {
        LocalDate today = LocalDate.now();
        return orderMapper.sumTodaySales(today);
    }

    @Override
    public BigDecimal getYesterdaySalesAmount() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return orderMapper.sumYesterdaySales(yesterday);
    }
}
