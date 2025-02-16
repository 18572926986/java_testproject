package com.test.service.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.pojo.User;
import com.test.pojo.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper; // 用于将对象转为JSON字符串

    // 保存用户的 UUID 和用户信息到 Redis 中  ,  UUID 保证每次登录生成的令牌都是唯一的
    public void saveUserToken(String uuid, ResponseUser responseUser) throws JsonProcessingException {

        // 将用户对象转换为 JSON 字符串
        String userJson = objectMapper.writeValueAsString(responseUser);

        // 保存到 Redis，设置 30 分钟过期时间
        redisTemplate.opsForValue().set(uuid, userJson, 30, TimeUnit.MINUTES);
    }

    // 从 Redis 中获取用户信息
    public ResponseUser getUserByToken(String uuid) throws JsonProcessingException {
        String userJson = redisTemplate.opsForValue().get(uuid);
        if (userJson != null) {
            return objectMapper.readValue(userJson, ResponseUser.class); // 将 JSON 转换回对象
        }
        return null;
    }

    // 验证 Redis 中是否存在该 UUID
    public boolean isTokenValid(String uuid) {
        return redisTemplate.hasKey(uuid);
    }

    // 将用户登录信息存储到 Redis，设置过期时间
    public void saveLoginStatus(String username, String token) {
        redisTemplate.opsForValue().set(username, token, 30, TimeUnit.MINUTES); // 30分钟过期
    }

    // 获取用户登录状态
    public String getLoginStatus(String userId) {
        //

        return redisTemplate.opsForValue().get(userId);
    }

    // 删除用户登录状态
    public void deleteLoginStatus(String userId) {
        redisTemplate.delete(userId);
    }

    //登录状态是否被储存
    public boolean isUserLoggedIn(String userId) {
        return redisTemplate.hasKey(userId);
    }
}