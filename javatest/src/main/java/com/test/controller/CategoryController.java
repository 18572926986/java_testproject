package com.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.pojo.Category;
import com.test.pojo.Product;
import com.test.pojo.Result;
import com.test.pojo.response.ResponseUser;
import com.test.service.CategoryService;
import com.test.service.serviceimpl.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/insert")
    public Result<?> insertCategory(@RequestBody List<Category> category, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                    if (responseUser != null)
                        if (responseUser.getStatus() == 1 && responseUser.getRoleType() == 1) {
                            category.forEach(categoryInsert ->{
                                categoryInsert.setCreatedBy(responseUser.getUserName());
                                categoryInsert.setUpdatedBy(responseUser.getUserName());
                            } );
                            boolean updateStatus=categoryService.batchAddCategory(category);
                            if(updateStatus){
                                return Result.success("插入成功");
                            }
                        }
                        return Result.error("用户已被禁用或无管理员权限");
    }





    @DeleteMapping("/")
    public Result<?> deleteById(@RequestParam Long id, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                    if (responseUser.getStatus() == 1 || responseUser.getRoleType() == 1) {
                        boolean deleteStatus = categoryService.removeById(id);
                        if (deleteStatus) {
                            return Result.success("删除成功");
                        }
                    }
                     return Result.error("删除失败");
    }

    @PostMapping("/update")
    public Result<?> editById(@RequestBody Category category, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    String token = cookie.getValue();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                    if (responseUser != null)
                        if (responseUser.getStatus() == 1 || responseUser.getRoleType() == 1) {
                            category.setUpdatedBy(responseUser.getUserName());
                            boolean updateStatus=categoryService.saveOrUpdate(category);
                            if(updateStatus){
                                return Result.success("修改成功");
                            }
                        }
                        return Result.error("用户已被禁用或无管理员权限");
    }
}
