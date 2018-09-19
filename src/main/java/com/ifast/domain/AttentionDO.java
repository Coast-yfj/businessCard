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
    private Long id;
    /** 用户id */
    private Long mid;
    /** 关注人id */
    private Long tid;
    //是否关注，0未关注。1：关注
    private String attention;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：用户id
     */
    public void setMid(Long mid) {
        this.mid = mid;
    }
    /**
     * 获取：用户id
     */
    public Long getMid() {
        return mid;
    }
    /**
     * 设置：关注人id
     */
    public void setTid(Long tid) {
        this.tid = tid;
    }
    /**
     * 获取：关注人id
     */
    public Long getTid() {
        return tid;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }
}
