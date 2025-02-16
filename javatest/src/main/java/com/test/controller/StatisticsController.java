package com.test.controller;

import com.test.ESservice.LogService;
import com.test.pojo.Result;
import com.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private LogService logService;
    @Autowired
    private OrderService orderService;
    @PostMapping("/queryProductPreview")
    public Result<?> searchLogsByShould(@RequestBody Map<String, String> params){
        try {
            return Result.success("商品预览总数",logService.queryProductPreview(params));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("商品数据获取失败");
        }
    }

    @GetMapping("/today/count")
    public Result<?> getTodayOrderCount() {
        return Result.success("今日订单总数",orderService.getTodayOrderCount());
    }

    @GetMapping("/today/sales")
    public Result<?> getTodaySalesAmount() {
        return Result.success("今日销售总额",orderService.getTodaySalesAmount());
    }

    @GetMapping("/yesterday/sales")
    public Result<?> getYesterdaySalesAmount() {
        return Result.success("昨日销售总额",orderService.getYesterdaySalesAmount());
    }
}
