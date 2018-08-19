package com.ifast.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;



/**
 * 
 * <pre>
 * 
 * </pre>
 * <small> 2018-08-19 22:42:09 | Aron</small>
 */
 @TableName("tb_attention")
public class AttentionDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /**  */
    @TableId
    private Integer id;
    /** 用户id */
    private Integer mid;
    /** 关注人id */
    private Integer tid;

    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：用户id
     */
    public void setMid(Integer mid) {
        this.mid = mid;
    }
    /**
     * 获取：用户id
     */
    public Integer getMid() {
        return mid;
    }
    /**
     * 设置：关注人id
     */
    public void setTid(Integer tid) {
        this.tid = tid;
    }
    /**
     * 获取：关注人id
     */
    public Integer getTid() {
        return tid;
    }
}
