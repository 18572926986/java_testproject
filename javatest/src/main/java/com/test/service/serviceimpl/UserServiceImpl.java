package com.test.service.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mapper.UserMapper;
import com.test.pojo.User;
import com.test.pojo.request.RequestUser;
import com.test.pojo.response.ResponseUser;
import com.test.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.test.utils.HashUtil.verify;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseUser login(User user,String password) throws Exception {
            if (verify(password,user.getPassword())) {
                return convertToResponse(user);
            } else {
                throw new Exception("密码错误");
            }
    }

    @Override
    public Integer register(User user) {
        User existUserId =userMapper.selectByUserId(user.getUserId());
        User existUserEmail=userMapper.selectByEmail(user.getEmail());
        User existUserPhone=userMapper.selectByPhone(user.getPhone());
        int num=1;
        if(existUserId!= null||existUserEmail!=null||existUserPhone!=null){
            num = 0;
            return num;
        }
        userMapper.insert(user);
        return num;
    }

    @Override
        public List<User> getUsersByNameAndAge(String name, Integer age) {
            QueryWrapper<User> queryWrapper = Wrappers.query();
            if (name != null && !name.isEmpty()) {
                queryWrapper.like("user_name", name);
            }
            if (age != null) {
                queryWrapper.ge("age", age);
            }
            return userMapper.selectList(queryWrapper);
    }

    @Override
    public Page<User> getUsersByPage(Integer currentPage, Integer pageSize) {
        Page<User> page = new Page<>(currentPage, pageSize);
        return userMapper.selectAllUser(page);
    }

    @Override
    public User findByIdentifier(User user) {
        if (isValidEmail(user.getEmail())) {
            return userMapper.selectByEmail(user.getEmail());
        } else if (isValidPhoneNumber(user.getPhone())) {
            return userMapper.selectByPhone(user.getPhone());
        } else {
            return userMapper.selectByUserId(user.getUserId());
        }
    }

    private boolean isValidEmail(String email) {
        // 简单的邮箱验证逻辑
        if (email == null || email.isEmpty()) {
            return false;
        }else if(!email.contains("@")){
            return false;
        }
        return email.contains("@");
    }

    private boolean isValidPhoneNumber(Long phone) {
        // 简单的手机号验证逻辑
        if (phone == null || phone.toString().length() != 11) {
//            throw new IllegalArgumentException("手机号格式不正确");
            return false;
        }
        // 检查号码是否在合理的范围内
        long minPhoneNumber = 13000000000L;
        long maxPhoneNumber = 19999999999L;

        if (phone < minPhoneNumber || phone > maxPhoneNumber) {
//            throw new IllegalArgumentException("手机号格式不正确");
            return false;
        }

        // 通过所有检查，号码有效
        return true;
    }

    private ResponseUser convertToResponse(User user) {
        ResponseUser responseUser = new ResponseUser();
        BeanUtils.copyProperties(user, responseUser);
        return responseUser;
    }

    private RequestUser convertToRequest(User user) {
        RequestUser requestUser = new RequestUser();
        BeanUtils.copyProperties(user, requestUser);
        return requestUser;
    }

    public List<User> findAllUsers() {
        return userMapper.selectList(null);
    }
}
