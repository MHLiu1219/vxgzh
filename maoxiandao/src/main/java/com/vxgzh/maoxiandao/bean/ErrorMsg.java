package com.vxgzh.maoxiandao.bean;

import lombok.Data;

/**
 * 微信接口返回错误信息
 *
 * @Author: lmh
 * @CreateTime: 2020-07-29
 * @Description:
 */
@Data
public class ErrorMsg {
    private String errcode;//全局返回码
    private String errmsg;//信息说明
}