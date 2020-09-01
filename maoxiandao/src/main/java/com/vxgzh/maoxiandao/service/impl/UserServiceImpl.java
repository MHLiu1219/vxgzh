package com.vxgzh.maoxiandao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.BaiduImageSegtResult;
import com.vxgzh.maoxiandao.bean.User;
import com.vxgzh.maoxiandao.mapper.UserMapper;
import com.vxgzh.maoxiandao.service.UserService;
import com.vxgzh.maoxiandao.utils.BaiduAccessTokenUtil;
import com.vxgzh.maoxiandao.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Value("${baidu.api.image.segt}")
    String apiUrl;

    /**
     * 根据图片名 获取用户微信号
     *
     * @param key
     * @return
     */
    @Override
    public String uploadTuPian(String key) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", key);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return null;
        }
        String name = user.getName();
        return name;
    }

    /**
     * 查询用户最新输入验证码 同时删除
     *
     * @param userKey
     * @return
     */
    @Override
    public String getCodeByuuid(String userKey) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", userKey);
        User user = userMapper.selectOne(wrapper);
        String code = user.getCode();

        //获取到以后删除
        user.setCode("0000");
        userMapper.updateById(user);

        return code;
    }

    /**
     * 注册
     *
     * @param openId
     * @return
     */
    @Override
    public String addUser(String openId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", openId);
        User user = userMapper.selectOne(wrapper);
        if (user != null) {
            return user.getUuid();
        }
        user = new User();
        String s = UUID.randomUUID().toString();
        String uuid = s.replaceAll("\\-", "");
        uuid = uuid.substring(0, 10);
        user.setName(openId);
        user.setUuid(uuid);
        userMapper.insert(user);
        return uuid;
    }

    /**
     * 跟新验证码
     *
     * @param openId
     * @param code
     */
    @Override
    public void addCode(String openId, String code) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", openId);
        User user = userMapper.selectOne(wrapper);
        user.setCode(code);
        userMapper.updateById(user);
    }

    @Override
    public String getUserByUuid(String userKey) {
        return uploadTuPian(userKey);
    }

    @Override
    public User getUserByName(String openId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", openId);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public String imageSegt(MultipartFile file) {
        // 获取access token
        String accessToken = BaiduAccessTokenUtil.getAccessToken();
        // 调用接口
        String url = apiUrl.replace("ACCESS_TOKEN", accessToken);
        // 结果处理
        String resp = "";
        Gson gson = new Gson();

        String imageBase64 = null;
        try {
            imageBase64 = Base64Utils.encodeToString(file.getBytes());
        } catch (IOException e) {
            System.out.println("error,图片转imageBase64");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("image", imageBase64);
        resp = HttpUtil.httpsPost(url, gson.toJson(map));
        // 返回
        BaiduImageSegtResult result = gson.fromJson(resp, BaiduImageSegtResult.class);
        if (null == result || result.getLogId() <= 0) {
            return "-1";
        }

        String collect = result.getResults().stream()
                .sorted((s1, s2) -> s1.getLocation().getLeft().compareTo(s2.getLocation().getLeft()))
                .map(m -> m.getName()).collect(Collectors.joining());
        return collect;
    }

}