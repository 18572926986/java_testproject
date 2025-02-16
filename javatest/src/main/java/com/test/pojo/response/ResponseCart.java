package com.test.pojo.response;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCart {
//    @TableId(value = "cart_id", type = IdType.AUTO)
//    @TableField("cart_id")
//    private Long id;
//    @TableField("user_id")
//    private String userId;
//    @TableField("sku_id")
//    private String skuId;
//    @TableField(value = "created_at",fill = FieldFill.INSERT)
//    private LocalDateTime createdAt;
//    @TableField("created_by")
//    private String createdBy;
    private Integer quantity;
    @TableField("name")
    private String productName;
    private BigDecimal price;
    private BigDecimal totalPrice;
    @TableField("`desc`")
    private String desc;
}