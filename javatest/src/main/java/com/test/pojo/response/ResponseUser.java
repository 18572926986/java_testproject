package com.test.pojo.response;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUser {
//    private Long id;
    private String userId;
    private String userName;
    private Integer age;
    private String gender;
    private String addr;
    private Integer status;
    private String email;
    private Long phone;
    private Integer roleType;
    private String desc;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private String createdBy;
//    private String updatedBy;
}
