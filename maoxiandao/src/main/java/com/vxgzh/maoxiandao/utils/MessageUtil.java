package com.vxgzh.maoxiandao.utils;

import com.google.gson.Gson;
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
}