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
@TableName("product")
public class Product {
    @TableId(value = "id")
    private Long productId;
    @TableField("`name`")
    private String productName;
    @TableField("category_id")
    private Long categoryId;
    @TableField("`desc`")
    private String desc;
    @TableField("image_path")
    private String imagePath;
    private BigDecimal price;
    private Integer stock;
    private String comment;
    private Double level;
    @TableField(value = "created_at",fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableField("created_by")
    private String createdBy;
    @TableField("updated_by")
    private String updatedBy;
}
