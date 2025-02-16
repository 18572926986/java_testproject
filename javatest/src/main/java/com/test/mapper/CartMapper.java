package com.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.pojo.Cart;
import com.test.pojo.response.ResponseCart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CartMapper extends BaseMapper<Cart> {
    @Select("SELECT pr.price, pr.desc, pr.name as product_name,(pr.price * ca.quantity) as total_price ,ca.quantity FROM cart ca JOIN product pr ON ca.sku_id = pr.id WHERE ca.user_id = #{userId}")
    List<ResponseCart> QueryCart(@Param("userId") String userId);
}
