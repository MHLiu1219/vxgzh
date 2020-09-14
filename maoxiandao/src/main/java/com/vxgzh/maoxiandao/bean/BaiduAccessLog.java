package com.vxgzh.maoxiandao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author MHLiu
 * @create 2020/9/14 20:45 星期一
 * @Description 百度ai接口调用日志
 */
@Component
@TableName(value = "baidu_access_log")//指定表名
@Data
public class BaiduAccessLog implements Serializable {

    private static final long serialVersionUID = 2646600138985423651L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 接口id
     */
    @TableField(value = "access_id")
    private Integer accessId;
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

}












