package com.vxgzh.maoxiandao.job;

import com.vxgzh.maoxiandao.utils.BaiduAccessTokenUtil;
import com.vxgzh.maoxiandao.utils.VxAccessTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * FlushAccessTokenJobs
 *
 * @Author: lmh
 * @CreateTime: 2020-09-19
 * @Description:
 */
@Component
@EnableScheduling
public class FlushAccessTokenJobs {
    private static final Logger log = LoggerFactory.getLogger(VxAccessTokenUtil.class);

    @Scheduled(cron = "0 0 * * * ?")
    public void flushVx(){
        log.info("定时刷新微信access-token");
        VxAccessTokenUtil.flushAccessToken();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduled(){
        log.info("定时刷新百度access-token");
        BaiduAccessTokenUtil.flushAccessToken();
    }
}