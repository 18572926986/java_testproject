package com.test.config;


//import com.test.interceptor.LoggingInterceptor;
import com.test.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;
//    @Autowired
//    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有以 / 开头的请求，登录和注册请求除外
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/register","/log/**");
//        registry.addInterceptor(loggingInterceptor).addPathPatterns("/product/**", "/cart/**", "/order/**")
//                                                    .excludePathPatterns("/log/**");
    }
}