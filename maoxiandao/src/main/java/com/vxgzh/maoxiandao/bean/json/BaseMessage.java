package com.vxgzh.maoxiandao.bean.json;

import lombok.Data;

/**
 * BaseMessage
 *
 * @Author: lmh
 * @CreateTime: 2020-07-19
 * @Description:
 */
@Data
public class BaseMessage {
    private String touser;
    private String msgtype;
    {
        this.setMsgtype(this.getClass().getDeclaredFields()[0].getName());
    }
}