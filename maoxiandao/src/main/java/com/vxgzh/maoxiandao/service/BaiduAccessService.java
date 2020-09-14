package com.vxgzh.maoxiandao.service;

import com.vxgzh.maoxiandao.bean.BaiduAccess;

import java.util.List;

public interface BaiduAccessService {

    /**
     * 获取所有的接口信息
     * @return
     */
    List<BaiduAccess> getAllBaiduAccess();

    /**
     * 根据用户密钥获取调用次数
     * @return
     * @param key
     */
    BaiduAccess getBaiduAccessByUuid(String key);

    /**
     * 增加接口可调用次数
     * @param key
     * @param number
     * @return
     */
    int addRemain(String key, Integer number);

    /**
     * 开启/关闭自动识别
     * @param key
     * @param i
     * @return
     */
    int chanceAccess(String key, int i);

    /**
     * 新增一个授权
     * @param key
     */
    void createAccess(String key);
}
