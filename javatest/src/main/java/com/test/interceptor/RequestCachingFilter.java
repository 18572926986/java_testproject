package com.test.interceptor;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;


@Component
public class RequestCachingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 包装请求对象，以便后续读取请求体
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 包装请求对象，以便后续读取请求体
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        // 继续执行过滤器链
        chain.doFilter(wrappedRequest, response);
    }
}
