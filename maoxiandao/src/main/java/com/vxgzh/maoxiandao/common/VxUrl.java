package com.vxgzh.maoxiandao.common;

/**
 * VxUrl
 *
 * @Author: lmh
 * @CreateTime: 2020-07-25
 * @Description:
 */
public class VxUrl {

    /**
     * 基础支持-获取access_token
     */
    public static final String BASE_SUPPORT_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq" +
            ".com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 发送信息-客服接口
     */
    public static final String SEND_MESSAGE_CUSTOMER_SERVICE_URL = "https://api.weixin.qq" +
            ".com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    /**
     * 素材管理-新增临时素材
     */
    public static final String MATERIAL_MANAGEMENT_ADD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
}
