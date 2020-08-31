package com.vxgzh.maoxiandao.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Account
 *
 * @Author: lmh
 * @CreateTime: 2020-07-19
 * @Description:
 */
public class Account {
    //常量值，为了方便修改而不修改代码
    public static String APPID="wx74626a14d97c19cd";
    public static String APPSECRET="d7126bf63431cc2fa619c9ef429de7ba";

    public static final String TOKEN="weixinCourse";

    public static final String OPENID="o7a2zwtHYT435vHW_rh9XZANPCh8";//MHおじさん
    public static final String TEMPLETEID="fzpznIjhmHBlzSNy4mttsmOCW8qqJUIRvz5aKD8VADs";

    public static final Map<String,Integer> access = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("o7a2zwtpEeUsfPzDdCHtDXd4sZHk".length());
    }
}