package com.vxgzh.maoxiandao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * User
 *
 * @Author: 郭思钊
 * @CreateTime: 2020-07-25
 * @Description:
 */
@Component
@TableName(value = "wjm_user")//指定表名
@Data
public class WjmUser implements Serializable {

    private static final long serialVersionUID = -6013930103462751307L;
    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
    @TableField(value = "name",exist = true)
    private String name;
    @TableField(value = "uuid",exist = true)
    private String uuid;
    @TableField(value = "code",exist = true)
    private String code;
//    @TableField(value = "create_time",exist = true)
//    private Date createTime;
//    @TableField(value = "delete_time",exist = true)
//    private Date deleteTime;

}