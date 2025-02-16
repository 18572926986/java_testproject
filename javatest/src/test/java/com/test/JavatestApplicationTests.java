package com.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.test.mapper.ProductMapper;
import com.test.mapper.UserMapper;
import com.test.pojo.Product;
import com.test.pojo.User;
import com.test.pojo.response.ResponseCart;
import com.test.pojo.response.ResponseProduct;
import com.test.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JavatestApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductMapper productMapper;

    @Test
    void contextLoads() {
        List<User> users = userMapper.selectList(null);
        users.forEach(user -> System.out.println(user));
    }

    @Test
    /**
     * DELETE FROM user_info WHERE (gender = ? AND user_name = ?)
     */
    public void deletetest() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_name", "李华");
        map.put("gender", "女");
        int num = userMapper.deleteByMap(map);
        System.out.println(num);
    }

    @Test
    /**
     * DELETE FROM user_info WHERE id IN ( ? , ? )
     */
    public void deleteBatchtest() {
        List<Long> ids = new ArrayList<>();
        ids.add(3L);
        ids.add(4L);
        int num = userMapper.deleteBatchIds(ids);
        System.out.println(num);
    }

    @Test
    /**
     * UPDATE user_info SET user_name=? WHERE id=?
     */
    public void updatetest() {
        User user = new User();
        user.setId(5L);
        user.setUserName("李明");
        int num = userMapper.updateById(user);
        System.out.println(num);
    }

    @Test
    /**
     * SELECT id,user_id,user_name,gender,addr,status,email
     * ,password,phone,role_type,`desc`,create_at,update_at,create_by,update_by FROM user_info WHERE (gender = ? AND user_name = ?)
     */
    public void querytest() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_name", "李华");
        map.put("gender", "女");
        List<User> users = userMapper.selectByMap(map);
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void inserttest() {
        User user = new User();
        user.setUserId("test006");
        user.setUserName("李明");
        user.setPassword("123456");
        user.setGender("男");
        user.setAddr("北京");
        user.setAge(24);
        int num = userMapper.insert(user);
        System.out.println(num);
    }

    @Test
    public void selectByName() {
        //userMapper.selectByName("王保保").forEach(System.out::println);
        userMapper.selectByUserId("123456");
    }

    @Test
    /**
     * INSERT INTO user_info ( user_name, gender, addr, password, create_at ) VALUES ( ?, ?, ?, ?, ? )
     */
    public void insertMore() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserName("王保保" + i+"号");
            user.setPassword("123456"+i);
            user.setGender("男");
            user.setAddr("北京"+i+"号");
            user.setCreatedAt(LocalDateTime.now());
            users.add(user);
        }
        boolean b=userService.saveBatch(users);
        System.out.println(b);
    }

    @Test
    public void pagetest(){
        // 分页查询逻辑:使用limit分页公式：（当前页-1）*每页显示条数
        Page<Product> page = new Page<>(1, 2);// 2表示当前页，3表示每页显示条数
        productMapper.selectPage(page,null);
        List<Product> productList = page.getRecords();
        productList.forEach(product -> System.out.println(product));
//        System.out.println(page.getPages());//总页数
//        System.out.println(page.getTotal());//总记录数
//        System.out.println(page.hasNext());//是否有下一页
//        System.out.println(page.hasPrevious());//是否有上一页
    }
    @Test
    public void productPageTest(){
        Page<ResponseProduct> page = new Page<>(1, 2);
        productMapper.selectAllProduct(page);
        IPage<ResponseProduct> responseProductIPage;
    }

    @Test
    public void QueryByNameTest(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.isNotNull("user_name").ge("age",23);
        queryWrapper.like("user_name","明").ge("age",18);
        //查询name字段不为空，且age大于等于23的用户
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    @Test
    public void QueryByLikeNameTest(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("user_name","明");
        //查询name字段中包含王的用户
//        queryWrapper.likeRight("user_name","明");
        //查询name字段中以明结尾的用户
        queryWrapper.notLike("user_name","明");
        //查询name字段中不包含王的用户
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    @Test
    public void QueryByBetweenAgeTest(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("age",18,23);
        //查询age字段在18到23之间的用户
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    @Test
    public void QueryTest1(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name","明").and(wrapper -> wrapper.gt("age", 17)
                .or().eq("gender", "男")).orderByDesc("age");
        //查询name字段中包含明，且age大于17或者gender为男的用户，并按照age降序排序
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    @Test
    public void QueryTest2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",5);
        User user = new User();
        user.setUserId("test001");
        //修改id为5的用户的userId为test001
        userMapper.update(user,queryWrapper);
    }

    @Test
    public void QueryTest3(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name","李明").and(wrapper -> wrapper.eq("user_id","test006"));
        //删除name字段为李明且userId为test006的用户
        userMapper.delete(queryWrapper);
    }

    @Test
    public void QueryTest4(){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.gt("age",23).eq("user_name","李明").set("phone",12581);
        //将age大于23且name为李明的用户的phone字段设置为12581
        userMapper.update(updateWrapper);
    }

    @Test
    public void QueryTest5(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(User::getUserName,"李明");
        //查询name字段为李明的用户
        lambdaQueryWrapper.like(User::getUserName,"明");
        userMapper.selectList(lambdaQueryWrapper).forEach(System.out::println);
    }

    @Test
    public void QueryTest6(){
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.gt(User::getAge,18).eq(User::getUserName,"李明").set(User::getEmail,"12581@qq.com");
        userMapper.update(lambdaUpdateWrapper);
    }

    @Test
    public void QueryTest7(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("user_id","select user_id from user_info where user_id = 'test001'");
        //查询user_id字段在子查询中查询到的用户id的用户
        userMapper.selectObjs(queryWrapper).forEach(System.out::println);
    }
}
