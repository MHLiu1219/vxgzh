package com.vxgzh.maoxiandao.controller;

import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.Image;
import com.vxgzh.maoxiandao.bean.ImageMsg;
import com.vxgzh.maoxiandao.common.Account;
import com.vxgzh.maoxiandao.common.VxUrl;
import com.vxgzh.maoxiandao.service.UserService;
import com.vxgzh.maoxiandao.utils.HttpUtil;
import com.vxgzh.maoxiandao.utils.MessageUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * PicController
 *
 * @Author: lmh
 * @CreateTime: 2020-07-24
 * @Description:
 */
@RestController
@RequestMapping("pic")
public class PicController {

    @Autowired
    UserService userService;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping("upload")
    public String upload(MultipartFile file){
        if (file ==null) {
            return "fail";
        }
        // 获取密钥
        String fileName = file.getName();
        String key = fileName.substring(0, fileName.indexOf("."));
        // todo 从数据库查询对应的用户
        //gsz完成部分
        String userId = null;
        if(key!= null && key.length()>= 0){
            userId = userService.uploadTuPian(key);
            if (userId==null){
                return "不付钱滚";
            }
        }

        // 上传到微信公众号
        String image = "";
        try {
            File file1 = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(),file1);
            image = HttpUtil.upload(VxUrl.MATERIAL_MANAGEMENT_ADD_URL, "image", file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送给用户
        Gson gson = new Gson();
        Map map = gson.fromJson(image, HashMap.class);

        ImageMsg imageMsg = new ImageMsg();
        imageMsg.setTouser(userId);
        imageMsg.setMsgtype(map.get("type").toString());
        imageMsg.setImage(new Image(map.get("media_id").toString()));
        String json = gson.toJson(imageMsg);
        MessageUtil.SendMsg(json);
        return "success";
    }

    /**
     * 查询最新的验证码
     * @param userKey
     * @return
     */
    @RequestMapping("code")
    public String getCode(String userKey){
        if (userKey == null || userKey.length() == 0) {
            return "fail";
        }

        // todo 从数据库查询最新的验证码
        String code =userService.getCodeByuuid(userKey);
        if (code == null || code.length()<= 0){
            return null;
        }

        return code;
    }

    /**
     * 通知用户验证结果
     * @param result
     * @return
     */
    @RequestMapping("result")
    public String result(String result){
        if (result == null || result.length() == 0) {
            return "fail";
        }

        // 通知用户结果

        return "success";
    }
}