package com.vxgzh.maoxiandao.bean;

import lombok.Data;

/**
 * Image
 *
 * @Author: lmh
 * @CreateTime: 2020-07-24
 * @Description:
 */
@Data
public class Image {
    private String media_id;
    public Image(){

    }
    public Image(String media_id){
        this.media_id = media_id;
    }
}