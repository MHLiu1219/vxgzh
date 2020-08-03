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
public class AccessToken extends ErrorMsg{
    private String access_token;
    /**
     * 有效时间
     */
    private Integer expires_in;
}