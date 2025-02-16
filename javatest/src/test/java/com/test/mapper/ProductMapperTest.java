package com.test.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.test.es.LogRepository;
import com.test.pojo.Product;
import com.test.pojo.log.ESLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private LogRepository logRepository;

    @Test
    void selectList() {
        List<Product> list = productMapper.selectList(null);
        list.forEach(product -> System.out.println(product));
    }

//    @Test
//    public void queryDocument() {
//        // 传入Document的id
//        List<Map<String, Object>> logs = logRepository.findByRequestMethod("GET");
//        logs.forEach(log -> {
//            System.out.println(log);
//        });
//    }

}