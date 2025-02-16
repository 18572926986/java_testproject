package com.test.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductAdmin {
    private String productName;
    private String desc;
    private BigDecimal price;
    private Integer stock;
    private String categoryName;
    private String comment;
    private Double level;
    private Integer preview_count;
}
