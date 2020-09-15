package com.vxgzh.maoxiandao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vxgzh.maoxiandao.bean.BaiduCard;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BaiduCardMapper extends BaseMapper<BaiduCard> {
}
