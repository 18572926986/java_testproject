package com.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.pojo.Order;
import com.test.pojo.Result;
import com.test.pojo.response.ResponseOrder;
import com.test.pojo.response.ResponseUser;
import com.test.service.OrderService;
import com.test.service.serviceimpl.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @GetMapping("/getOrder")
    public Result<?> getOrder(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(token);
                    if (responseUser != null) {
                        ResponseOrder responseOrder;
                        responseOrder=orderService.create(responseUser.getUserId());
                        responseOrder.setCreatedBy(responseUser.getUserName());
                        return Result.success("展示订单", responseOrder);
                    }
                }
            }
        }
        return Result.error("未找到用户cookie,已退出登录状态");
    }

    @GetMapping("/change")
    public Result<?> changeOrder(HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(token);
                    if (responseUser != null) {
                        ResponseOrder responseOrder=orderService.create(responseUser.getUserId());
                        Order order=new Order();
                        order.setStatus(1);
                        order.setTotalPrice(responseOrder.getTotalPrice());
                        order.setCreatedBy(responseUser.getUserName());
                        order.setCreatedAt(LocalDateTime.now());
                        order.setUserid(responseUser.getUserId());
                        orderService.save(order);
                        return Result.success("订单已生成");
                    }
                }
            }
        }
        return Result.error("未找到用户cookie,已退出登录状态");
    }
}
