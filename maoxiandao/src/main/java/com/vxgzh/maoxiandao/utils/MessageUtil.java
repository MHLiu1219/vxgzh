package com.vxgzh.maoxiandao.utils;

import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.Image;
import com.vxgzh.maoxiandao.bean.ImageMsg;
import com.vxgzh.maoxiandao.bean.Text;
import com.vxgzh.maoxiandao.bean.TextMsg;
import com.vxgzh.maoxiandao.common.VxUrl;

/**
 * MessageUtil
 *
 * @Author: lmh
 * @CreateTime: 2020-07-24
 * @Description:
 */
public class MessageUtil {

    /**
     * 客服-发送消息
     * @param json
     */
    public static void SendMsg(String json){
        //发送消息的url
        String url= VxUrl.SEND_MESSAGE_CUSTOMER_SERVICE_URL;
        String accessToken = AccessTokenUtil.getAccessToken();
        url = url.replaceAll("ACCESS_TOKEN", accessToken);
        String httpsRequest = HttpUtil.httpsPost(url, json);
        System.out.println("***httpsRequest>"+httpsRequest);
    }

    /**
     * 客服-发送消息
     * @param src
     */
    public static void SendMsg(Object src){
        SendMsg(new Gson().toJson(src));
    }


    /**
     * 发送客服消息-文本
     * @param userNameOrOpenId 用户
     * @param content 文本
     */
    public static void sendTextMsg(String userNameOrOpenId,String content){
        TextMsg textMsg = new TextMsg();
        textMsg.setTouser(userNameOrOpenId);
        textMsg.setText(new Text(content));
        SendMsg(textMsg);
    }

    /**
     * 发送客服消息-图片
     * @param userNameOrOpenId 用户
     * @param media_id 图片的媒体id
     */
    public static void sendImageMsg(String userNameOrOpenId,String media_id){
        ImageMsg imageMsg = new ImageMsg();
        imageMsg.setTouser(userNameOrOpenId);
        imageMsg.setImage(new Image(media_id));
        SendMsg(imageMsg);
    }
}