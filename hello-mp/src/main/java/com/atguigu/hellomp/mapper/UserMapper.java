package com.atguigu.hellomp.mapper;

import com.atguigu.hellomp.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* @author Euphoria
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-08-18 20:51:24
* @Entity com.atguigu.hellomp.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectUserPage(IPage<User> page);
}




