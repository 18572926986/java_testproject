package com.test.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class User  {
    @TableId(value = "id", type = IdType.AUTO)
    private Long Id;
    @TableField("user_id")
    private String userId;
    @TableField("user_name")
    private String userName;
    private String gender;
    private String addr;
    private Integer status;
    private String email;
    private String password;
    private Long phone;
    private Integer age;
    @TableField("role_type")
    private Integer roleType;
    @TableField("`desc`")
    private String desc;
    @TableField(value = "create_at",fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(value = "update_at",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    @TableField("create_by")
    private String createdBy;
    @TableField("update_by")
    private String updateBy;
}

