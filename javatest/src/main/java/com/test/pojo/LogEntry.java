//package com.test.pojo;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import lombok.Data;
//import org.w3c.dom.Text;
//
//import java.time.LocalDateTime;
//
//@Data
//@TableName("log")
//public class LogEntry {
//    @TableId(value = "id",type = IdType.AUTO)
//    private Integer id;
//    @TableField(value = "request_header")
//    private String requestHeader;
//    @TableField(value = "request_body")
//    private String requestBody;
//    @TableField(value = "request_method")
//    private String requestMethod;
//    @TableField(value = "param_type")
//    private String paramType;
//    @TableField(value = "response_headers")
//    private String responseHeaders;
//    @TableField(value = "response_body")
//    private String responseBody;
//    private Long duration;
//    @TableField("response_time")
//    private LocalDateTime responseTime;
//    @TableField("request_time")
//    private LocalDateTime requestTime;
//}