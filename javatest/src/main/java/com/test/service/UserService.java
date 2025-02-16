package com.test.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.User;
import com.test.pojo.response.ResponseUser;

import java.util.List;
import java.util.Optional;

public interface UserService extends IService<User> {
    ResponseUser login(User user, String password) throws Exception;
    Integer register(User user);
//    Boolean batchAddUser(List<User> userList);
    List<User> getUsersByNameAndAge(String name, Integer age);

    Page<User> getUsersByPage(Integer currentPage, Integer pageSize);

    User findByIdentifier(User user);

    List<User> findAllUsers();
}
