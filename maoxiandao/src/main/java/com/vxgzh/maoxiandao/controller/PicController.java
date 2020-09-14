package com.vxgzh.maoxiandao.controller;

import com.google.gson.Gson;
import com.vxgzh.maoxiandao.common.Account;
import com.vxgzh.maoxiandao.common.VxUrl;
import com.vxgzh.maoxiandao.service.UserService;
import com.vxgzh.maoxiandao.utils.HttpUtil;
import com.vxgzh.maoxiandao.utils.MessageUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("maoxiandao")
public class PicController {

    @Autowired
    UserService userService;

    /**
     * 1.上传图片
     *
     * @param file
     * @return
     */

    @RequestMapping("uploadPic")
    public String upload(MultipartFile file, String flag, String text) {
        if (file == null) {
            return "File Is Null!";
        }
        // 获取密钥并进行身份校验
        String fileName = file.getOriginalFilename();
        String key = null;
        try {
            key = fileName.substring(0, fileName.indexOf("."));
        } catch (Exception e) {
            return "File Name Error!";
        }
        String userId = null;
        if (!StringUtils.isEmpty(key)) {
            userId = userService.uploadTuPian(key);
            if (userId == null) {
                return "No User!";
            }
        }
        
        if ("1".equals(flag)) {
            // 通知用户结果
            String content = "图片已经被自动识别，结果为：CODE\n手动输入可覆盖自动识别结果，目前自动识别剩余次数：REMAIN";
            content = StringUtils.isEmpty(text) ? content : text;

            content = content.replace("REMAIN","" + Account.access.get(key + ":remain"))
                    .replace("COUNT","" + Account.access.get(key));
            MessageUtil.sendTextMsg(userService.getUserByUuid(key), content);
        }

        // 上传到微信公众号
        Date update = new Date();
        String dir = new SimpleDateFormat("yyyyMMdd").format(update);
        String imageName = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(update);
        File file1 = null;
        try {
            file1 = new File("/opt/app/image/"+dir+"/"+imageName+".jpg");
            FileUtils.copyInputStreamToFile(file.getInputStream(), file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String resp = "";
        resp = HttpUtil.upload(VxUrl.MATERIAL_MANAGEMENT_ADD_URL, "image", file1);

        // 发送给用户
        Gson gson = new Gson();
        Map map = gson.fromJson(resp, HashMap.class);
        Object mediaId = map.get("media_id");
        if (mediaId == null) {
            // 发送失败
            return resp;
        }

        MessageUtil.sendImageMsg(userId, mediaId.toString());
        return "success";
    }

    /**
     * 2.获取识别结果
     *
     * @param userKey
     * @return
     */
    @RequestMapping("getCode")
    public String getCode(String userKey, String key, String flag, String text) {
        userKey = userKey == null ? key : userKey;
        if (userKey == null || userKey.length() == 0) {
            return "userKey Is Null!";
        }

        String code = userService.getCodeByuuid(userKey);
        if (code == null || code.length() <= 0) {
            return null;
        }
        key = userKey;
        String content = "获取到的结果为：CODE";
        if ("1".equals(flag)) {
            // 通知用户结果
            content = StringUtils.isEmpty(text) ? content : text;

            content = content.replace("CODE",code)
                    .replace("REMAIN","" + Account.access.get(key + ":remain"))
                    .replace("COUNT","" + Account.access.get(key));
            MessageUtil.sendTextMsg(userService.getUserByUuid(key), content);
        }

        return code;
    }

    /**
     * 3.给用户发送消息
     *
     * @param content
     * @return
     */
    @RequestMapping("sendText")
    public String result(String userKey, String key, String content) {

        if (content == null || content.length() == 0) {
            return "Content Is Null";
        }

        userKey = userKey == null ? key : userKey;
        if (userKey == null || userKey.length() == 0) {
            return "userKey Is Null!";
        }
        key = userKey;
        content = content.replace("REMAIN","" + Account.access.get(key + ":remain"))
                .replace("COUNT","" + Account.access.get(key));
        // 查询用户
        String userName = userService.getUserByUuid(userKey);
        // 通知用户结果
        MessageUtil.sendTextMsg(userName, content);
        return "success";
    }

    /**
     * 4.识别图片
     * @param file
     * @param flag
     * @return
     */
    @RequestMapping("identify")
    public String identify(MultipartFile file, String flag, String text) {
        // 身份校验识别
        if (file == null) {
            return "File Is Null!";
        }
        String fileName = file.getOriginalFilename();
        String key = null;
        try {
            key = fileName.substring(0, fileName.indexOf("."));
        } catch (Exception e) {
            return "File Name Error!";
        }
        if (!checkAccess(key)) {
            return "Access Expired!";
        }
        String userId = null;
        if (key != null && key.length() >= 0) {
            userId = userService.uploadTuPian(key);
            if (userId == null) {
                return "No User!";
            }
        }

        String code = userService.imageSegt(file);

        String content = "图片识别剩余次数：REMAIN";
        if ("1".equals(flag)) {
            // 通知用户结果
            content = StringUtils.isEmpty(text) ? content : text;

            content = content.replace("CODE",code)
                    .replace("REMAIN","" + Account.access.get(key + ":remain"))
                    .replace("COUNT","" + Account.access.get(key));
            MessageUtil.sendTextMsg(userService.getUserByUuid(key), content);
        }

        return code;
    }

    private boolean checkAccess(String key) {
        Integer count = Account.access.get(key);
        Integer remain = Account.access.get(key + ":remain");
        if (null == count || null == remain || remain <= 0) {
            return false;
        }
        Account.access.put(key + ":remain", remain - 1);
        Account.access.put(key, count + 1);
        return true;
    }

    /**
     * 5.开启自动符文
     * @param key
     * @param flag
     * @return
     */
    @RequestMapping("access")
    public String access(@Nullable String key, String flag, String text) {
        Integer integer = Account.access.get(key);
        if (null == integer) {
            Account.access.put(key, 0);
        }

        String content = "自动符文功能开启成功，自动识别剩余次数：REMAIN";
        if ("1".equals(flag)) {
            // 通知用户结果
            content = StringUtils.isEmpty(text) ? content : text;

            content = content.replace("REMAIN","" + Account.access.get(key + ":remain"))
                    .replace("COUNT","" + Account.access.get(key));
            MessageUtil.sendTextMsg(userService.getUserByUuid(key), content);
        }

        return "access success! " + key + ":count：" + Account.access.get(key);
    }

    /**
     * 6.添加自动识别额度
     * @param key
     * @param number
     * @param flag
     * @return
     */
    @RequestMapping("add")
    public String add(@Nullable String key, @Nullable Integer number, String flag, String text) {
        Integer remain = Account.access.get(key + ":remain");
        if (null == remain) {
            Account.access.put(key + ":remain", number);
        } else {
            Account.access.put(key + ":remain", remain + number);
        }

        String content = "增加自动识别NUMBER次额度，目前剩余次数：REMAIN";
        if ("1".equals(flag)) {
            // 通知用户结果
            content = StringUtils.isEmpty(text) ? content : text;

            content = content.replace("REMAIN","" + Account.access.get(key + ":remain"))
                    .replace("NUMBER","" + number)
                    .replace("COUNT","" + Account.access.get(key));
            MessageUtil.sendTextMsg(userService.getUserByUuid(key), content);
        }

        return "add success! key:remain=" + Account.access.get(key + ":remain");
    }

    /**
     * 7.统计接口调用次数
     * @param key
     * @return
     */
    @RequestMapping("count")
    public String count(String key) {
        Map<String,Object> jsonMap = new HashMap<>();
        if (!StringUtils.isEmpty(key)) {
            jsonMap.put("count", Account.access.get(key));
            jsonMap.put("remain", Account.access.get(key+":remain"));
            jsonMap.put("access_history", Account.access_history.get(key));
        } else {
            jsonMap.put("access", Account.access);
            jsonMap.put("access_history", Account.access_history);
        }
        return new Gson().toJson(jsonMap);
    }

    /**
     * 8.关闭自动符文
     * @param key
     * @param flag
     * @return
     */
    @RequestMapping("deleteAccess")
    public String deleteAccess(@Nullable String key, String flag, String text) {
        Integer count = Account.access.get(key);
        String content = "自动符文功能关闭成功，开启期间总共识别次数：COUNT";
        if ("1".equals(flag)) {
            // 通知用户结果
            content = StringUtils.isEmpty(text) ? content : text;

            content = content.replace("REMAIN","" + Account.access.get(key + ":remain"))
                    .replace("COUNT","" + Account.access.get(key));
            MessageUtil.sendTextMsg(userService.getUserByUuid(key), content);
        }


        if (null != count) {
            Map<Date, Integer> dateIntegerMap = Account.access_history.get(key);
            if (dateIntegerMap == null) {
                dateIntegerMap = new HashMap<>();
            }
            dateIntegerMap.put(new Date(),count);
            Account.access.remove(key);
            return "delete success! key=" + key;
        }
        return "No Key! key=" + key;
    }

}








