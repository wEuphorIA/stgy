package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.common.constant.RedisConstant;
import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.common.utils.JwtUtil;
import com.atguigu.lease.common.utils.VerifyCodeUtil;
import com.atguigu.lease.model.entity.UserInfo;
import com.atguigu.lease.model.enums.BaseStatus;
import com.atguigu.lease.web.app.mapper.UserInfoMapper;
import com.atguigu.lease.web.app.service.LoginService;
import com.atguigu.lease.web.app.service.SmsService;
import com.atguigu.lease.web.app.vo.user.LoginVo;
import com.atguigu.lease.web.app.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void getSMSCode(String phone) throws Exception {
        String code = VerifyCodeUtil.getVerifyCode(6);
        String key = RedisConstant.APP_LOGIN_PREFIX + phone;

        Boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey){
            Long ttl = redisTemplate.getExpire(key, TimeUnit.DAYS);
            if (RedisConstant.APP_LOGIN_CODE_TTL_SEC - ttl < RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC){
                throw new LeaseException(ResultCodeEnum.APP_SEND_SMS_TOO_OFTEN);
            }
        }

        smsService.sendCode(phone, code);
        redisTemplate.opsForValue().set(key,code,RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.DAYS);

    }

    @Override
    public String login(LoginVo loginVo) {
        if (loginVo.getPhone() == null) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }
        if (loginVo.getCode() == null) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }

        String key = RedisConstant.APP_LOGIN_PREFIX + loginVo.getPhone();

        String code = redisTemplate.opsForValue().get(key);

        if (code == null) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EXPIRED);
        }

        if (!code.equals(loginVo.getCode())) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
        }

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getPhone,loginVo.getPhone());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            //注册
            userInfo = new UserInfo();
            userInfo.setPhone(loginVo.getPhone());
            userInfo.setStatus(BaseStatus.ENABLE);
            userInfo.setNickname("用户-"+ loginVo.getPhone().substring(7));
        }else {
            //判断是否被禁用
            if (userInfo.getStatus().equals(BaseStatus.DISABLE)) {
                throw new LeaseException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
            }
        }


        return JwtUtil.createToken(userInfo.getId(),userInfo.getPhone());
    }

    @Override
    public UserInfoVo getLoginUserById() {
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return new UserInfoVo(userInfo.getNickname(),userInfo.getAvatarUrl());
    }
}
