package com.vxgzh.maoxiandao.controller;

import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.BaiduImageSegtResult;
import com.vxgzh.maoxiandao.common.VxUrl;
import com.vxgzh.maoxiandao.service.UserService;
import com.vxgzh.maoxiandao.utils.BaiduAccessTokenUtil;
import com.vxgzh.maoxiandao.utils.HttpUtil;
import com.vxgzh.maoxiandao.utils.MessageUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PicController
 *
 * @Author: lmh
 * @CreateTime: 2020-07-24
 * @Description:
 */
@RestController
@RequestMapping("maoxiandao")
public class PicController {

    @Autowired
    UserService userService;

    /**
     * 上传图片
     * @param file
     * @return
     */

    @RequestMapping("uploadPic")
    public String upload(MultipartFile file){
        if (file ==null) {
            return "fail";
        }
        // 获取密钥并进行身份校验
        String fileName = file.getOriginalFilename();
        String key = null;
        try {
            key = fileName.substring(0, fileName.indexOf("."));
        } catch (Exception e) {
            return "fail";
        }
        String userId = null;
        if(key!= null && key.length()>= 0){
            userId = userService.uploadTuPian(key);
            if (userId==null){
                return "无法查询到密钥对应的用户";
            }
        }

        // 上传到微信公众号
        String resp = "";
        try {
            String format = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date());
            File file1 = new File("../image/"+format+".jpg");
            FileUtils.copyInputStreamToFile(file.getInputStream(),file1);
            resp = HttpUtil.upload(VxUrl.MATERIAL_MANAGEMENT_ADD_URL, "image", file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送给用户
        Gson gson = new Gson();
        Map map = gson.fromJson(resp, HashMap.class);
        Object media_id = map.get("media_id");
        if (media_id == null) {
            // 发送失败
            return resp;
        }

        MessageUtil.sendImageMsg(userId,media_id.toString());
        return "success";
    }

    /**
     * 查询最新的验证码
     * @param userKey
     * @return
     */
    @RequestMapping("getCode")
    public String getCode(String userKey){
        if (userKey == null || userKey.length() == 0) {
            return "fail";
        }

        // todo 从数据库查询最新的验证码
        String code =userService.getCodeByuuid(userKey);
        if (code == null || code.length() <= 0){
            return null;
        }

        return code;
    }

    /**
     * 通知用户验证结果
     * @param content
     * @return
     */
    @RequestMapping("sendText")
    public String result(String userKey,String content){
        if (content == null || content.length() == 0) {
            return "fail";
        }
        // 查询用户
        String userName = userService.getUserByUuid(userKey);
        // 通知用户结果
        MessageUtil.sendTextMsg(userName,content);
        return "success";
    }

    @RequestMapping("identify")
    public String identify(MultipartFile file){
        // 身份校验识别
        if (file ==null) {
            return "fail";
        }
        String fileName = file.getOriginalFilename();
        String key = null;
        try {
            key = fileName.substring(0, fileName.indexOf("."));
        } catch (Exception e) {
            return "fail";
        }
        String userId = null;
        if(key!= null && key.length()>= 0){
            userId = userService.uploadTuPian(key);
            if (userId==null){
                return "无法查询到密钥对应的用户";
            }
        }

        // 获取access token
        String accessToken = BaiduAccessTokenUtil.getAccessToken();
        // 调用接口
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/segmentation/wejiema" +
                "?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", accessToken);
        // 结果处理
        String resp = "";
        Gson gson = new Gson();

        try {
            Date update = new Date();
            String dir = new SimpleDateFormat("yyyyMMdd").format(update);
            String imageName = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(update);
            File file1 = new File("./image/"+dir+"/"+imageName+".jpg");
            FileUtils.copyInputStreamToFile(file.getInputStream(),file1);

            String imageBase64 = Base64Utils.encodeToString(file.getBytes());

            Map<String, Object> map = new HashMap<>();
            map.put("image", imageBase64);
            resp = HttpUtil.httpsPost(url, gson.toJson(map));
        } catch (IOException e) {
            e.printStackTrace();
        }
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








