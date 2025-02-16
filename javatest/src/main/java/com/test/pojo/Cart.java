package com.test.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("Cart")
public class Cart {
    @TableId(value = "cart_id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private String userId;
    @TableField("sku_id")
    private Long skuId;
    @TableField(value = "created_at",fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField("created_by")
    private String createdBy;
    private Integer quantity;
//    @TableField("name")
//    private String productName;
}
