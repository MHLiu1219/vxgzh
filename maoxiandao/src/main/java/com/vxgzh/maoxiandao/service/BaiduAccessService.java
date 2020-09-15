package com.vxgzh.maoxiandao.service;

import com.vxgzh.maoxiandao.bean.BaiduAccess;
import com.vxgzh.maoxiandao.bean.BaiduCard;

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
     */
    void addRemain(String key, Integer number);

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
     * @return
     */
    BaiduAccess createAccess(String key);

    /**
     * 自动识别后更新接口信息
     * @param key
     */
    void imageSegtUpdateAccess(String key);

    /**
     * 预设卡号表
     * @param baiduCards
     */
    void addCards(List<BaiduCard> baiduCards);

    /**
     * 根据卡号充值
     * @param key
     * @param cardNo
     * @return
     */
    String addRemainByCard(String key, String cardNo);

    /**
     * 条件查询卡表
     * @param baiduCard
     * @return
     */
    String getCards(BaiduCard baiduCard);
}
