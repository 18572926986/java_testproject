package com.test.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("category")
public class Category {
    @TableId(value = "category_id",type = IdType.AUTO)
    private Long id;
    @TableField("category_name")
    private String categoryName;
    @TableField("`desc`")
    private String desc;
    @TableField(value = "created_at",fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableField("created_by")
    private String createdBy;
    @TableField("updated_by")
    private String updatedBy;
}
