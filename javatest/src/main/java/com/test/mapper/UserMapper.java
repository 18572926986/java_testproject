package com.test.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.pojo.User;
import com.test.pojo.response.ResponseProduct;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user_info where user_name = #{username}")
    List<User> selectByName(String name);

    @Select("select * from user_info where user_id = #{userId}")
    User selectByUserId(String userId);
    @Select("select * from user_info where email = #{email}")
    User selectByEmail(String eamil);
    @Select("select * from user_info where phone = #{phone}")
    User selectByPhone(Long phone);

    @Select("select * from user_info")
    Page<User> selectAllUser(Page<User> page);

}
