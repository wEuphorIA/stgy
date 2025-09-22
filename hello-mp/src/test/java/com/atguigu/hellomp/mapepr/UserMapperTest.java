package com.atguigu.hellomp.mapepr;

import com.atguigu.hellomp.entity.User;
import com.atguigu.hellomp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testList(){
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectById(){
        System.out.println(userMapper.selectById(1));
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setAge(18);
        user.setName("张三");
        user.setEmail("test@test.com");
        userMapper.insert(user);
    }

    @Test
    public void testUpdateById(){
        User user = userMapper.selectById(1);
        user.setName("小明");

        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    @Test
    public void testDeleteById(){
        User user = userMapper.selectById(6);
        userMapper.deleteById(user);

    }
}