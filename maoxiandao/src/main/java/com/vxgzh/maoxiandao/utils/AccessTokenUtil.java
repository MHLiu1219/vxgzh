package com.vxgzh.maoxiandao.utils;

import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.AccessToken;
import com.vxgzh.maoxiandao.common.Account;
import com.vxgzh.maoxiandao.common.VxUrl;

/**
 * 获取微信access_token
 *
 * @Author: lmh
 * @CreateTime: 2020-07-19
 * @Description:
 */
public class AccessTokenUtil {
    public static String url= VxUrl.BASE_SUPPORT_GET_ACCESS_TOKEN_URL;

    /**
     * 获取AccessToke
     * @param APPID
     * @param APPSECRET
     * @return
     */
    public static String getAccessToken(String APPID,String APPSECRET) {
        url = url.replaceAll("APPID", APPID).replaceAll("APPSECRET", APPSECRET);
        String json = HttpUtil.httpsGet(url);
        Gson gson = new Gson();
        AccessToken accessToken = gson.fromJson(json, AccessToken.class);
        String access_token = accessToken.getAccess_token();
        return access_token;
    }

    /**
     * 获取AccessToke
     * @return
     */
    public static String getAccessToken(){
        String str = getAccessToken(Account.APPID, Account.APPSECRET);
        return str;
    }

    public static void main(String[] args) {
        String str = getAccessToken(Account.APPID, Account.APPSECRET);
        System.out.println(str);
    }
}