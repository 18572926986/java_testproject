package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavatestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavatestApplication.class, args);
        System.out.println("hello");
    }
}
