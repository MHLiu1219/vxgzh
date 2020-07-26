package com.vxgzh.maoxiandao.bean;

import lombok.Data;

/**
 * AccessToken
 *
 * @Author: lmh
 * @CreateTime: 2020-07-19
 * @Description:
 */
@Data
public class AccessToken {
    private String access_token;
    private String expires_in;
    private String errcode;
    private String errmsg;
}