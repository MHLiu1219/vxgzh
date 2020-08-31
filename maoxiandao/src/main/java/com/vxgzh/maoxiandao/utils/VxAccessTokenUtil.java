package com.vxgzh.maoxiandao.utils;

import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.AccessToken;
import com.vxgzh.maoxiandao.common.Account;
import com.vxgzh.maoxiandao.common.VxUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 获取微信access_token
 *
 * @Author: lmh
 * @CreateTime: 2020-07-19
 * @Description:
 */
public class VxAccessTokenUtil {
    public static final String url= VxUrl.BASE_SUPPORT_GET_ACCESS_TOKEN_URL;
    private static Map<String,AccessToken> map = new ConcurrentHashMap<>();
    private static Date updateTime = new Date();
    private static Lock lock = new ReentrantLock();
    private static final Logger log = LoggerFactory.getLogger(VxAccessTokenUtil.class);

    static {
        flushAccessToken();
    }

    /**
     * 自定义获取AccessToke
     * @param APPID
     * @param APPSECRET
     * @return
     */
    public static AccessToken getAccessToken(String APPID,String APPSECRET) {
        String urlStr = url.replaceAll("APPID", APPID).replaceAll("APPSECRET", APPSECRET);
        String json = HttpUtil.httpsGet(urlStr);
        Gson gson = new Gson();
        AccessToken accessToken = gson.fromJson(json, AccessToken.class);
        return accessToken;
    }

    /**
     * 获取AccessToke
     * @return
     */
    public static String getAccessToken(){
        check();
        AccessToken accessToken = map.get("accessToken");
        if (accessToken == null) {
            log.error("access_token is null");
            return null;
        }
        String access_token = accessToken.getAccess_token();
        return access_token;
    }

    /**
     * 检查是否过期
     */
    private static void check() {
        long l = System.currentTimeMillis() - updateTime.getTime();
        if (l < 6000*1000) {
            // 时间充足
            return;
        }
        log.info("token存在时长(ms)：" + l);
        // 重新刷新
        if (lock.tryLock()) {
            try {
                log.info("开始刷新token");
                flushAccessToken();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 刷新token
     */
    public static void flushAccessToken(){
        AccessToken accessToken = getAccessToken(Account.APPID, Account.APPSECRET);
        if (accessToken != null) {
            map.put("accessToken",accessToken);
            updateTime = new Date();
            log.info("new token: " + accessToken);
        }
    }

}