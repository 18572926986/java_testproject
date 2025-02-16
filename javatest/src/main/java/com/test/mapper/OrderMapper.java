package com.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.pojo.Order;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT COUNT(*) FROM `order` WHERE DATE(created_at) = #{date}")
    int countTodayOrders(LocalDate date);

    @Select("SELECT IFNULL(SUM(total_price), 0) FROM `order` WHERE DATE(created_at) = #{date}")
    BigDecimal sumTodaySales(LocalDate date);

    @Select("SELECT IFNULL(SUM(total_price), 0) FROM `order` WHERE DATE(created_at) = #{date}")
    BigDecimal sumYesterdaySales(LocalDate date);
}
