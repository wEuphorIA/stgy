package com.atguigu.hellomp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.hellomp.entity.User;
import com.atguigu.hellomp.service.UserService;
import com.atguigu.hellomp.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Euphoria
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-08-18 20:51:24
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




