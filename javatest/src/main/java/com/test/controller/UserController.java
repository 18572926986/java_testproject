package com.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.pojo.Result;
import com.test.pojo.User;
import com.test.pojo.response.ResponseUser;
import com.test.service.UserService;
import com.test.service.serviceimpl.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static com.test.utils.HashUtil.hash;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody User user) {
        Integer userReg = userService.register(user);
        if (userReg == 1) {
            String hashpwd = hash(user.getPassword());
            user.setPassword(hashpwd);
            userService.saveOrUpdate(user);
            return Result.success("注册成功");
        } else {
            return Result.error("注册失败，用户id已存在或邮箱已存在或手机号已存在");
        }
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody User loginRequest, HttpServletResponse response) {
        try {
            User userOpt = userService.findByIdentifier(loginRequest);
            ResponseUser responseUser = userService.login(userOpt, loginRequest.getPassword());
            String token = UUID.randomUUID().toString();
            // 将用户信息存储到 Redis
            redisService.saveUserToken(token, responseUser);
            // 保存到 Cookie 中，userToken 为 UUID
            Cookie cookie = new Cookie("userToken", token);
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath("/");
            response.addCookie(cookie);
            return Result.success("登录成功！", token);
        } catch (Exception e) {
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        redisService.deleteLoginStatus(cookies[0].getValue());
        return Result.success("已退出登录状态");
    }

    @GetMapping("/userinfo")
    public Result<?> getUserInfo(HttpServletRequest request) throws JsonProcessingException {
//        // 从 Cookie 中获取用户信息
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
//                    ResponseUser responseUser = redisService.getUserByToken(token);
//                        return Result.success("获取用户信息成功", responseUser);
//                }
//             }
//        }
//        return Result.error("未找到登录用户的Cookie");
        Cookie[] cookies = request.getCookies();
        ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
        return Result.success("获取用户信息成功", responseUser);
    }


    @PostMapping("/insertUser")
    public Result<?> insertCategory(@RequestBody User user, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                        if (responseUser.getStatus() == 1 && responseUser.getRoleType() == 1) {
                            user.setCreatedBy(responseUser.getUserName());
                            user.setUpdateBy(responseUser.getUserName());
                            user.setStatus(1);
                            user.setPassword(hash(user.getPassword()));
                            boolean updateStatus=userService.save(user);
                            if(updateStatus){
                                return Result.success("插入成功");
                            }
                        }
                        return Result.error("用户已被禁用或无管理员权限");
                }
//        return Result.error("未找到用户cookie,已退出登录状态");


    @GetMapping("/queryUser")
    public Result<?> QueryUser(@RequestParam(required = false) String name, @RequestParam(required = false) Integer age, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                        if (responseUser.getStatus() == 1 && responseUser.getRoleType() == 1) {
                            return Result.success("查询成功",userService.getUsersByNameAndAge(name, age));
                        }
                        return Result.error("用户已被禁用或无管理员权限");
    }

//        return Result.error("未找到用户cookie,已退出登录状态");


    @GetMapping("/queryUserByPage")
    public Result<?> QueryUserByPage(@RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer pageSize, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                        if (responseUser.getStatus() == 1 && responseUser.getRoleType() == 1) {
                            return Result.success("查询成功",userService.getUsersByPage(currentPage, pageSize));
                        }
                        return Result.error("用户已被禁用或无管理员权限");
            }
//        return Result.error("未找到用户cookie,已退出登录状态");


    @PostMapping("/updateUser")
    public Result<?> updateUser(@RequestBody User user, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
        if (responseUser.getStatus() == 1 && responseUser.getRoleType() == 1) {
            user.setUpdateBy(responseUser.getUserName());
            userService.saveOrUpdate(user);
            return Result.success("修改成功");
        }
        return Result.error("用户已被禁用或无管理员权限");
    }

    @DeleteMapping("/deleteUser")
    public Result<?> deleteUser(@RequestParam String Id, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
        if (responseUser.getStatus() == 1 && responseUser.getRoleType() == 1) {
            userService.removeById(Id);
            return Result.success("删除成功");
        }
        return Result.error("用户已被禁用或无管理员权限");
    }
}