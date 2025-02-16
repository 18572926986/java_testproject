package com.test.mapper;

import com.test.pojo.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CartMapperTest {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void CartTest(){
        cartMapper.selectById(123456);
    }

}