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
 * @Description 百度ai接口信息类
 */
@Component
@TableName(value = "baidu_access")//指定表名
@Data
public class BaiduAccess implements Serializable {
    private static final long serialVersionUID = 2630340480495319983L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;
    /**
     * 用户的密钥
     */
    @TableField(value = "uuid")
    private String uuid;
    /**
     * 接口请求总次数
     */
    @TableField(value = "count")
    private Integer count;
    /**
     * 接口可调用次数
     */
    @TableField(value = "remain")
    private Integer remain;
    /**
     * 接口状态：0-关闭，1-开启
     */
    @TableField(value = "status")
    private Integer status;
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












