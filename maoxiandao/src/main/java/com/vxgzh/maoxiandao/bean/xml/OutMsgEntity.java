package com.vxgzh.maoxiandao.bean.xml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OutMsgEntity
 *
 * @Author: lmh
 * @CreateTime: 2020-07-19
 * @Description:
 */
@Data
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutMsgEntity {
    // 发送方的账号
    @JsonProperty("FromUserName")
    protected String FromUserName;
    // 接收方的账号(OpenID)
    @JsonProperty("ToUserName")
    protected String ToUserName;
    // 消息创建时间
    @JsonProperty("CreateTime")
    protected Long CreateTime;
    /**
     * 消息类型
     * text 文本消息
     * image 图片消息
     * voice 语音消息
     * video 视频消息
     * music 音乐消息
     * news 图文消息
     */
    @JsonProperty("MsgType")
    protected String MsgType;
    // 图片消息媒体id，可以调用多媒体文件下载接口拉取数据
    @XmlElementWrapper(name="Image")
    @JsonProperty("MediaId")
    private String[] MediaId ;
    // 文本内容
    @JsonProperty("Content")
    private String Content;

    /**
     * 获取发送类型图片的xml
     * @return
     */
    public String getImageXml(){
        return "<xml>\n" +
                "  <ToUserName><![CDATA["+ToUserName+"]]></ToUserName>\n" +
                "  <FromUserName><![CDATA["+FromUserName+"]]></FromUserName>\n" +
                "  <CreateTime>"+System.currentTimeMillis()+"</CreateTime>\n" +
                "  <MsgType><![CDATA[image]]></MsgType>\n" +
                "  <Image>\n" +
                "    <MediaId><![CDATA["+MediaId+"]]></MediaId>\n" +
                "  </Image>\n" +
                "</xml>";
    }

    /**
     * 获取发送类型为文本的xml
     * @return
     */
    public String getTextXml(){
        return "<xml>\n" +
                "  <ToUserName><![CDATA["+ToUserName+"]]></ToUserName>\n" +
                "  <FromUserName><![CDATA["+FromUserName+"]]></FromUserName>\n" +
                "  <CreateTime>"+System.currentTimeMillis()+"</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA["+Content+"]]></Content>\n" +
                "</xml>";
    }
}