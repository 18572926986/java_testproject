package com.test.pojo.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(indexName = "log")
public class ESLog{

    @Id
    @Field(type = FieldType.Integer)
    private Integer id;
    @Field(type = FieldType.Text)
    private String requestHeader;

    @Field(type = FieldType.Text)
    private String requestBody;
    @Field(type = FieldType.Text)
    private String requestMethod;

    @Field(type = FieldType.Text)
    private String paramType;

    @Field(type = FieldType.Text)
    private String responseHeaders;

    @Field(type = FieldType.Text)
    private String responseBody;
    @Field(type = FieldType.Long)
    private Long duration;

    @Field(type = FieldType.Date, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime responseTime;

    @Field(type = FieldType.Date, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime requestTime;


}
