package com.test.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`order`")
public class Order {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private String userid;
    @TableField("total_price")
    private BigDecimal totalPrice;
    private Integer status;
    @TableField(value = "created_at",fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField("created_by")
    private String createdBy;
}
