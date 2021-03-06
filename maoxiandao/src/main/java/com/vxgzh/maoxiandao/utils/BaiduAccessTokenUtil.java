package com.vxgzh.maoxiandao.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * BaiduAccessTokenUtil
 *
 * @Author: lmh
 * @CreateTime: 2020-08-29
 * @Description:
 */
public class BaiduAccessTokenUtil {

    public static final String url= "https://aip.baidubce.com/oauth/2.0/token" +
            "?grant_type=GRANT_TYPE&client_id=CLIENT_ID&client_secret=CLIENT_SECRET";
    private static final Map<String, String> map = new ConcurrentHashMap<>();
    private static Date updateTime = new Date();
    private static final Lock lock = new ReentrantLock();
    private static final Logger log = LoggerFactory.getLogger(VxAccessTokenUtil.class);


    private static void post(String grantType, String apiKey, String secretKey){
        String urlGet = url.replace("GRANT_TYPE",grantType)
                .replace("CLIENT_ID",apiKey)
                .replace("CLIENT_SECRET",secretKey);
        Gson gson = new Gson();
        String httpsPost = HttpUtil.httpsGet(urlGet);
        Map json = gson.fromJson(httpsPost, Map.class);
        map.putAll(json);
    }

    public static String getAccessToken(){
        // 检查是否快过期了,并刷新
        checkAndFlush();
        return map.get("access_token");
    }

    private static void checkAndFlush() {
        long l = System.currentTimeMillis() - updateTime.getTime();
        Object obj = map.get("expires_in");
        if (obj != null) {
            String s = obj.toString();
            String expires_in = s.substring(0, s.indexOf("."));
            if (!StringUtils.isEmpty(expires_in) && (Long.parseLong(expires_in) - 3600) * 1000 > l) {
                return;
            }
        }

        // 快过期或者已过期
        log.info("token存在时长(ms)：" + l);
        // 重新刷新
        if (lock.tryLock()) {
            try {
                log.info("开始刷新百度access-token");
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
        post("client_credentials","XuSXHOmxQv01TRVNE80hqECI","nd05wkNRgVSkSyjyZFZQrlbXDN4h1I7v");
    }


}























