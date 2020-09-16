package com.vxgzh.maoxiandao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.BaiduImageSegtResult;
import com.vxgzh.maoxiandao.bean.WjmUser;
import com.vxgzh.maoxiandao.mapper.WjmUserMapper;
import com.vxgzh.maoxiandao.service.BaiduAccessService;
import com.vxgzh.maoxiandao.service.WjmUserService;
import com.vxgzh.maoxiandao.utils.BaiduAccessTokenUtil;
import com.vxgzh.maoxiandao.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
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
public class WjmUserServiceImpl implements WjmUserService {


    @Autowired
    private WjmUserMapper wjmUserMapper;
    @Autowired
    private BaiduAccessService baiduAccessService;

    @Value("${baidu.api.image.segt}")
    private String apiUrl;

    /**
     * 根据图片名 获取用户微信号
     *
     * @param key uuid
     * @return openid
     */
    @Override
    public String uploadTuPian(String key) {
        QueryWrapper<WjmUser> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", key);
        WjmUser wjmUser = wjmUserMapper.selectOne(wrapper);
        if (wjmUser == null) {
            return null;
        }
        return wjmUser.getName();
    }

    /**
     * 查询用户最新输入验证码 同时删除
     *
     * @param userKey uuid
     * @return code
     */
    @Override
    public String getCodeByuuid(String userKey) {
        QueryWrapper<WjmUser> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", userKey);
        WjmUser wjmUser = wjmUserMapper.selectOne(wrapper);

        if (wjmUser == null) {
            return null;
        }

        String code = wjmUser.getCode();

        //获取到以后删除
        wjmUser.setCode("0000");
        wjmUserMapper.updateById(wjmUser);

        return code;
    }

    /**
     * 注册
     *
     * @param openId 微信号
     * @return uuid
     */
    @Override
    public String addUser(String openId) {
        QueryWrapper<WjmUser> wrapper = new QueryWrapper<>();
        wrapper.eq("name", openId);
        WjmUser wjmUser = wjmUserMapper.selectOne(wrapper);
        if (wjmUser != null) {
            return wjmUser.getUuid();
        }
        wjmUser = new WjmUser();
        String s = UUID.randomUUID().toString();
        String uuid = s.replaceAll("-", "");
        uuid = uuid.substring(0, 10);
        wjmUser.setName(openId);
        wjmUser.setUuid(uuid);
        wjmUserMapper.insert(wjmUser);
        return uuid;
    }

    /**
     * 跟新验证码
     *
     * @param openId 微信号
     * @param code 符文（验证码）
     */
    @Override
    public void addCode(String openId, String code) {
        QueryWrapper<WjmUser> wrapper = new QueryWrapper<>();
        wrapper.eq("name", openId);
        WjmUser wjmUser = wjmUserMapper.selectOne(wrapper);
        wjmUser.setCode(code);
        wjmUserMapper.updateById(wjmUser);
    }

    @Override
    public String getUserByUuid(String userKey) {
        return uploadTuPian(userKey);
    }

    @Override
    public WjmUser getUserByName(String openId) {
        QueryWrapper<WjmUser> wrapper = new QueryWrapper<>();
        wrapper.eq("name", openId);
        return wjmUserMapper.selectOne(wrapper);
    }

    @Override
    public String imageSegt(MultipartFile file) {
        // 获取access token
        String accessToken = BaiduAccessTokenUtil.getAccessToken();
        // 调用接口
        String url = apiUrl.replace("ACCESS_TOKEN", accessToken);
        // 结果处理
        String resp;
        Gson gson = new Gson();

        String imageBase64 = null;
        try {
            imageBase64 = Base64Utils.encodeToString(file.getBytes());
        } catch (IOException e) {
            System.out.println("error,图片转imageBase64");
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("image", imageBase64);
        resp = HttpUtil.httpsPost(url, gson.toJson(map));
        // 返回
        BaiduImageSegtResult result = gson.fromJson(resp, BaiduImageSegtResult.class);
        if (null == result || result.getLogId() <= 0) {
            return "-1";
        }

        return result.getResults().stream()
                .sorted(Comparator.comparing(s -> s.getLocation().getLeft()))
                .map(m -> m.getName().substring(0,1)).collect(Collectors.joining());
    }

}