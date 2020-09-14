package com.vxgzh.maoxiandao.service;

import com.vxgzh.maoxiandao.bean.WjUser;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {


    //根据图片名获取用户微信唯一对应值
    String uploadTuPian(String key);

    //获取用户输入的验证码
    String getCodeByuuid(String userKey);

    //注册用户
    String addUser(String openId);

    //跟新code
    void addCode(String openId, String code);

    /**
     * 根据uuid查询用户名
     * @param userKey
     * @return
     */
    String getUserByUuid(String userKey);

    /**
     * 根据微信号查询用户
     * @param openId
     * @return
     */
    WjUser getUserByName(String openId);

    /**
     * 图行识别
     * @param file
     * @return
     */
    String imageSegt(MultipartFile file);
}
