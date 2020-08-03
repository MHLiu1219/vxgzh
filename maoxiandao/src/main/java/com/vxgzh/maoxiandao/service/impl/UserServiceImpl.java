package com.vxgzh.maoxiandao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vxgzh.maoxiandao.bean.User;
import com.vxgzh.maoxiandao.mapper.UserMapper;
import com.vxgzh.maoxiandao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * UserServiceImpl
 *
 * @Author: 郭思钊
 * @CreateTime: 2020-07-25
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserMapper userMapper;



    /**
     * 根据图片名 获取用户微信号
     * @param key
     * @return
     */
    @Override
    public String uploadTuPian(String key) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid",key);
        User user = userMapper.selectOne(wrapper);
        if (user == null ){
            return null;
        }
        String name = user.getName();
        return name;
    }

    /**
     * 查询用户最新输入验证码 同时删除
     * @param userKey
     * @return
     */
    @Override
    public String getCodeByuuid(String userKey) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid",userKey);
        User user = userMapper.selectOne(wrapper);
        String code = user.getCode();

        //获取到以后删除
        user.setCode("0000");
        userMapper.updateById(user);

        return code;
    }

    /**
     * 注册
     * @param openId
     * @return
     */
    @Override
    public String addUser(String openId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name",openId);
        User user = userMapper.selectOne(wrapper);
        if (user!= null){
            return user.getUuid();
        }
        user = new User();
        String s = UUID.randomUUID().toString();
        String uuid = s.replaceAll("\\-", "");
        uuid = uuid.substring(0,10);
        user.setName(openId);
        user.setUuid(uuid);
        userMapper.insert(user);
        return uuid;
    }

    /**
     * 跟新验证码
     * @param openId
     * @param code
     */
    @Override
    public void addCode(String openId, String code) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name",openId);
        User user = userMapper.selectOne(wrapper);
        user.setCode(code);
        userMapper.updateById(user);
    }

    @Override
    public String getUserByUuid(String userKey) {
        return uploadTuPian(userKey);
    }

}