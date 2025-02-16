package com.test.pojo.response;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOrder {
//    @TableField("order_id")
//    private Long id;
//    @TableField("user_id")
//    private String userid;
    @TableField("total_price")
    private BigDecimal totalPrice;
    @TableField("created_by")
    private String createdBy;
    @TableField("`name`")
    private List<String> productName;
//    private Integer status;
}

