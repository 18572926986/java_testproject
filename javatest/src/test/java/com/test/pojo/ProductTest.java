package com.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
 public class ProductTest {
    private Integer id;
    private String name;
    private Double price;

}