package com.vxgzh.maoxiandao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vxgzh.maoxiandao.bean.BaiduAccess;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BaiduAccessMapper extends BaseMapper<BaiduAccess> {
}
