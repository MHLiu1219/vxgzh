package com.vxgzh.maoxiandao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author MHLiu
 * @create 2020/9/14 20:45 星期一
 * @Description 百度ai接口信息类
 */
@Component
@TableName(value = "baidu_card")//指定表名
@Data
public class BaiduCard implements Serializable {
    private static final long serialVersionUID = 2630340480495319983L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 卡号
     */
    @TableField(value = "card_no")
    private String cardNo;

    /**
     * 次数
     */
    @TableField(value = "count")
    private Integer count;
    /**
     * 接口可调用次数
     */
    @TableField(value = "type")
    private Integer type;
    /**
     * 接口可调用次数
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}












