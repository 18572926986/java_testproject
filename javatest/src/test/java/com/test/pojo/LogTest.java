package com.test.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTest {
    private Integer id;

    private String requestHeader;

    private String requestBody;

    private String requestMethod;

    private String paramType;

    private String responseHeaders;

    private String responseBody;

    private Long duration;

    private LocalDateTime responseTime;

    private LocalDateTime requestTime;
}
