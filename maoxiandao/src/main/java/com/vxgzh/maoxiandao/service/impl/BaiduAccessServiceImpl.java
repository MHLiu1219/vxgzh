package com.vxgzh.maoxiandao.service.impl;

import com.vxgzh.maoxiandao.bean.BaiduAccess;
import com.vxgzh.maoxiandao.mapper.BaiduAccessMapper;
import com.vxgzh.maoxiandao.service.BaiduAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<BaiduAccess> getAllBaiduAccess() {
        return null;
    }

    @Override
    public BaiduAccess getBaiduAccessByUuid(String key) {
        return null;
    }

    @Override
    public int addRemain(String key, Integer number) {
        return 0;
    }

    @Override
    public int chanceAccess(String key, int i) {
        return 0;
    }

    @Override
    public void createAccess(String key) {

    }
}