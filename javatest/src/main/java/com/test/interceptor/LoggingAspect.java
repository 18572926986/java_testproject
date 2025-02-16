package com.test.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.test.mapper.LogRepository;
//import com.test.pojo.LogEntry;
//import com.test.pojo.LogEntry;
//import com.test.pojo.log.ESLog;
import com.test.es.LogRepository;
import com.test.pojo.log.ESLog;
//import com.test.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class LoggingAspect {
//    @Autowired
//    private LogService logService;

    @Autowired
    private LogRepository logRepository;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/user/login", "/user/register", "/log/**");

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    @Pointcut("execution(* com.test.controller.LogController.*(..))")
    public void excludeLogController() {
    }

    @Around("controllerPointcut() && !excludeLogController()")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
        String requestBody = new String(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding());
        String params = request.getQueryString();
        String route = request.getRequestURI();
        String method = request.getMethod();
        // 获取请求体和查询参数
        for (String excludedPath : EXCLUDED_PATHS) {
            if (pathMatches(excludedPath, route)) {
                return joinPoint.proceed(); // Skip logging for this path
            }
        }
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();

        Object result;
        String responseBody = null;
        int statusCode = 200;

        // 执行方法并记录响应结果和响应时间
        try {
            result = joinPoint.proceed();
            responseBody = new ObjectMapper().writeValueAsString(result);
        } catch (Exception ex) {
            statusCode = 500;
            responseBody = ex.getMessage();
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            ESLog esLog = new ESLog();
            esLog.setRequestHeader(route);
            esLog.setParamType(params);
            esLog.setRequestMethod(method);
            esLog.setRequestBody(requestBody);
            esLog.setResponseBody(responseBody);
            esLog.setDuration(duration);
            esLog.setResponseHeaders("Request processed with status code: " + statusCode);
            esLog.setRequestTime(LocalDateTime.now());
            esLog.setResponseTime(LocalDateTime.now());
            logRepository.save(esLog);
        }
        return result;
    }

    private boolean pathMatches(String pattern, String route) {
        if (pattern.endsWith("/**")) {
            String basePattern = pattern.substring(0, pattern.length() - 3);
            return route.startsWith(basePattern);
        } else {
            return route.equals(pattern);
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}



