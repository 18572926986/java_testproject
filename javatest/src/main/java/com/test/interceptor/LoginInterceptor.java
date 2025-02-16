package com.test.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.test.pojo.LogEntry;
import com.test.pojo.response.ResponseUser;
import com.test.service.serviceimpl.RedisService;
//import com.test.utils.LogUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

//    @Autowired
//    private LogUtil logUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    String userToken = cookie.getValue();
                    // 检查 Redis 中是否有这个 token
                    if (redisService.isTokenValid(userToken)) {
                        return true; // 放行请求
                    }
                }
            }
        }
        ResponseUser user = redisService.getUserByToken(cookies[0].getValue());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("code", 400);
        responseBody.put("message", "未找到用户cookie,已退出登录状态");
        responseBody.put("data", null);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        response.getWriter().write(jsonResponse);

        return false;

    }

}
