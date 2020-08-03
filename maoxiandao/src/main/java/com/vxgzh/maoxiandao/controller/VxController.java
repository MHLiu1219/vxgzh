package com.vxgzh.maoxiandao.controller;

import com.vxgzh.maoxiandao.bean.xml.InMsgEntity;
import com.vxgzh.maoxiandao.bean.xml.OutMsgEntity;
import com.vxgzh.maoxiandao.common.Account;
import com.vxgzh.maoxiandao.service.UserService;
import com.vxgzh.maoxiandao.utils.AccessTokenUtil;
import com.vxgzh.maoxiandao.utils.MessageUtil;
import com.vxgzh.maoxiandao.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * VxController
 *
 * @Author: lmh
 * @CreateTime: 2020-07-24
 * @Description:
 */
@RestController
@RequestMapping("vx")
public class VxController {

    private static Logger log = LoggerFactory.getLogger(VxController.class);

    @Autowired
    UserService userService;

    @GetMapping()
    public String verify(String signature,String timestamp,String nonce,String echostr){
        System.out.println("开始验证");
        if (SignUtil.checkSignature(signature,timestamp,nonce)) {
            System.out.println("验证成功");
            return echostr;
        }
        System.out.println("验证失败");
        return null;
    }


    @PostMapping()
    public String getMessage(@RequestBody InMsgEntity inMsgEntity){
        // 微信账号（openID）
        String openId = inMsgEntity.getFromUserName();
        // 返回体
        OutMsgEntity outMsgEntity = new OutMsgEntity();
        outMsgEntity.setFromUserName(inMsgEntity.getToUserName());
        outMsgEntity.setToUserName(inMsgEntity.getFromUserName());

        // 注册绑定
        if (inMsgEntity.getContent() != null && "@".equals(inMsgEntity.getContent().substring(0,1))) {
            // 密钥(卡号)
            String key = inMsgEntity.getContent().substring(1);

            // todo 绑定到数据库
            String uuid = userService.addUser(openId);

            // 返回成功信息
            outMsgEntity.setContent("注册成功，您的密钥为："+uuid);
        }

        // 保存验证码
        if (inMsgEntity.getContent() != null && "#".equals(inMsgEntity.getContent().substring(0,1))) {
            // 验证码
            String code = inMsgEntity.getContent().substring(1);

            // todo 把这最新的验证码保存到数据库
            userService.addCode(openId,code);

            // 返回成功信息
            outMsgEntity.setContent("开始验证。。。");
        }
        // 其它不处理
        if (outMsgEntity.getContent() == null) {
            outMsgEntity.setContent("注册绑定：发送@字符" +
                    "游戏检查验证：#验证码（用wasd表示箭头方向的四个方向）\n例如：#wwad");
        }
        return outMsgEntity.getTextXml();
    }

    @RequestMapping("change")
    public String change(String appID,String appsecret){
        // 通知到我的微信
        try {
            MessageUtil.sendTextMsg(Account.OPENID, "appID={"+appID+"}\n\n"+"appsecret={"+appsecret+"}");
        } catch (Exception e) {
            log.error("更改公众账号发送通知失败");
        }
        // 修改Accout
        Account.APPID=appID;
        Account.APPSECRET=appsecret;
        log.info("修改成功update appID success");
        // 更新access_key
        AccessTokenUtil.flushAccessToken();
        if (AccessTokenUtil.getAccessToken() == null) {
            return "flush access_token fail";
        }
        return "success";
    }
}