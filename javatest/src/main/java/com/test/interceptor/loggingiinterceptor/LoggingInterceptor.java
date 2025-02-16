//package com.test.interceptor;
//
//import com.test.pojo.LogEntry;
//import com.test.service.LogService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.time.LocalDateTime;
//import java.util.Enumeration;
//
//@Component
//public class LoggingInterceptor implements HandlerInterceptor {
//    @Autowired
//    private LogService logService;
//
//    private ThreadLocal<LogEntry> threadLocal = new ThreadLocal<>();
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        ContentCachingRequestWrapper1 wrappedRequest = new ContentCachingRequestWrapper1(request);
//        LogEntry log = new LogEntry();
//        log.setRequestHeader(getHeadersInfo(wrappedRequest));
//        log.setRequestBody(new String(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding()));
//        log.setRequestMethod(wrappedRequest.getMethod());
//        log.setParamType(determineParameterType(wrappedRequest)); // 动态设置参数类型
//        log.setRequestTime(LocalDateTime.now());
//        threadLocal.set(log);
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
//        LogEntry log = threadLocal.get();
//        log.setResponseHeaders(wrappedResponse.getContentType());
//        log.setResponseBody(new String(wrappedResponse.getContentAsByteArray(), wrappedResponse.getCharacterEncoding()));
//        log.setResponseTime(LocalDateTime.now());
//        log.setDuration(java.time.Duration.between(log.getRequestTime(), log.getResponseTime()).toMillis());
//        logService.saveLog(log);
//        threadLocal.remove();
//    }
//
//    private String getHeadersInfo(HttpServletRequest request) {
//        StringBuilder headers = new StringBuilder();
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
//        }
//        return headers.toString();
//    }
//
//    private String determineParameterType(HttpServletRequest request) {
//        String method = request.getMethod();
//        switch (method) {
//            case "GET":
//                return "Query";
//            case "POST":
//            case "PUT":
//            case "DELETE":
//                return "Body";
//            default:
//                return "Unknown";
//        }
//    }
//}
//
////    @Override
////    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
////        LogEntry log = new LogEntry();
////        log.setRequestHeader(getHeadersInfo(request));
////        log.setRequestBody(getBody(request));
////        log.setRequestMethod(request.getMethod());
////        log.setParamType(determineParameterType(request)); // Assuming all requests are body parameters for simplicity
////        log.setRequestTime(LocalDateTime.now());
////        threadLocal.set(log);
////        return true;
////    }
////
////    private String determineParameterType(HttpServletRequest request) {
////        String method = request.getMethod();
////        switch (method) {
////            case "GET":
////                return "Query";
////            case "POST":
////            case "PUT":
////            case "DELETE":
////                return "Body";
////            default:
////                return "Unknown";
////        }
////    }
////
////    @Override
////    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
////        LogEntry log = threadLocal.get();
////        log.setResponseHeaders(response.getContentType());
////        log.setResponseBody(response.getContentType()); // Simplified for example purposes
////        log.setResponseTime(LocalDateTime.now());
////        log.setDuration(java.time.Duration.between(log.getRequestTime(), log.getResponseTime()).toMillis());
////        logService.saveLog(log);
////        threadLocal.remove();
////    }
////
////    private String getHeadersInfo(HttpServletRequest request) {
////        StringBuilder headers = new StringBuilder();
////        Enumeration<String> headerNames = request.getHeaderNames();
////        while (headerNames.hasMoreElements()) {
////            String headerName = headerNames.nextElement();
////            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
////        }
////        return headers.toString();
////    }
////
////    private Text getBody(HttpServletRequest request) throws IOException {
////        String body = null;
////        if (request instanceof ContentCachingRequestWrapper) {
////            body = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray(), request.getCharacterEncoding());
////        } else {
////            BufferedReader reader = request.getReader();
////            body = reader.lines().collect(Collectors.joining(System.lineSeparator()));
////        }
////        return body;
////    }
//
//
//
