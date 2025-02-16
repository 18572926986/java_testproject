package com.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.Order;
import com.test.pojo.response.ResponseOrder;

import java.math.BigDecimal;

public interface OrderService extends IService<Order> {

    ResponseOrder create(String userId);

    int getTodayOrderCount();

    BigDecimal getTodaySalesAmount();

    BigDecimal getYesterdaySalesAmount();
}
