package com.vxgzh.maoxiandao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.vxgzh.maoxiandao.bean.BaiduAccess;
import com.vxgzh.maoxiandao.bean.BaiduAccessLog;
import com.vxgzh.maoxiandao.bean.BaiduCard;
import com.vxgzh.maoxiandao.bean.WjmUser;
import com.vxgzh.maoxiandao.mapper.BaiduAccessLogMapper;
import com.vxgzh.maoxiandao.mapper.BaiduAccessMapper;
import com.vxgzh.maoxiandao.mapper.BaiduCardMapper;
import com.vxgzh.maoxiandao.mapper.WjmUserMapper;
import com.vxgzh.maoxiandao.service.BaiduAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * UserServiceImpl
 *
 * @Author: 郭思钊
 * @CreateTime: 2020-07-25
 * @Description:
 */
@Service
public class BaiduAccessServiceImpl implements BaiduAccessService {

    @Autowired
    private BaiduAccessMapper baiduAccessMapper;
    @Autowired
    private BaiduAccessLogMapper baiduAccessLogMapper;
    @Autowired
    private WjmUserMapper wjmUserMapper;
    @Autowired
    private BaiduCardMapper baiduCardMapper;

    @Override
    public List<BaiduAccess> getAllBaiduAccess() {
        QueryWrapper<BaiduAccess> wrapper = new QueryWrapper<>();
        return baiduAccessMapper.selectList(wrapper);
    }

    @Override
    public BaiduAccess getBaiduAccessByUuid(String key) {
        QueryWrapper<BaiduAccess> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid",key);
        BaiduAccess baiduAccess = baiduAccessMapper.selectOne(wrapper);
        if (baiduAccess == null) {
            return this.createAccess(key);
        }
        return baiduAccess;
    }

    @Override
    public void addRemain(String key, Integer number) {
        number = number == null ? 0 : number;
        BaiduAccess baiduAccess = this.getBaiduAccessByUuid(key);
        baiduAccess.setRemain(baiduAccess.getRemain() + number);
        QueryWrapper<BaiduAccess> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", key);
        int update = baiduAccessMapper.update(baiduAccess, wrapper);
        if (update == 0) {
            createAccess(key);
            addRemain(key,number);
        }
    }

    @Override
    public int chanceAccess(String key, int i) {
        BaiduAccess baiduAccess = new BaiduAccess();
        baiduAccess.setStatus(i);
        QueryWrapper<BaiduAccess> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", key);
        return baiduAccessMapper.update(baiduAccess, wrapper);
    }

    @Override
    public BaiduAccess createAccess(String key) {
        QueryWrapper<WjmUser> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", key);
        WjmUser wjmUser = wjmUserMapper.selectOne(wrapper);
        BaiduAccess baiduAccess = new BaiduAccess();
        baiduAccess.setUserId(wjmUser.getId());
        baiduAccess.setUuid(key);
        baiduAccess.setStatus(1);
        baiduAccess.setCount(0);
        baiduAccess.setRemain(0);
        baiduAccessMapper.insert(baiduAccess);
        return baiduAccess;
    }

    @Override
    public void imageSegtUpdateAccess(String key) {
        BaiduAccess baiduAccess = this.getBaiduAccessByUuid(key);
        baiduAccess.setCount(baiduAccess.getCount() + 1);
        baiduAccess.setRemain(baiduAccess.getRemain() - 1);
        baiduAccessMapper.updateById(baiduAccess);
        // 插入日志
        BaiduAccessLog baiduAccessLog = new BaiduAccessLog();
        baiduAccessLog.setAccessId(baiduAccess.getId());
        baiduAccessLogMapper.insert(baiduAccessLog);
    }

    @Override
    public void addCards(List<BaiduCard> baiduCards) {
        for (BaiduCard baiduCard : baiduCards) {
            baiduCardMapper.insert(baiduCard);
        }
    }

    @Override
    public String addRemainByCard(String key, String cardNo) {
        QueryWrapper<BaiduCard> wrapper = new QueryWrapper<>();
        wrapper.eq("card_no",cardNo);
        BaiduCard baiduCard = baiduCardMapper.selectOne(wrapper);
        if (baiduCard == null) {
            return cardNo + "不存在,或者已经被使用";
        }
        if (baiduCard.getStatus() == 1) {
            return cardNo + "已经被使用";
        }
        addRemain(key,baiduCard.getCount());
        baiduCard.setStatus(1);
        baiduCardMapper.updateById(baiduCard);
        return "success";
    }

    @Override
    public String getCards(BaiduCard baiduCard) {

        QueryWrapper<BaiduCard> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(baiduCard.getCardNo())) {
            wrapper.eq("card_no", baiduCard.getCardNo());
        }
        if (baiduCard.getCount() != null) {
            wrapper.eq("count", baiduCard.getCount());
        }
        if (baiduCard.getStatus() != null) {
            wrapper.eq("status", baiduCard.getStatus());
        }
        if (baiduCard.getType() != null) {
            wrapper.eq("type", baiduCard.getType());
        }
        if (baiduCard.getId() != null) {
            wrapper.eq("id", baiduCard.getId());
        }
        List<BaiduCard> baiduCards = baiduCardMapper.selectList(wrapper);
        return new Gson().toJson(baiduCards);
    }
}