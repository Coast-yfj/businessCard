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
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
 @TableName("tb_unit")
public class UnitDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /** 主键 */
    @TableId
    private Integer id;
    /** 名称 */
    private String name;
    /** 规模 */
    private String scale;
    /** 简介 */
    private String introduction;
    /**
     * 用户id
     */
    @TableField(value = "userId")
    private String userId;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 获取：名称
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：规模
     */
    public void setScale(String scale) {
        this.scale = scale;
    }
    /**
     * 获取：规模
     */
    public String getScale() {
        return scale;
    }
    /**
     * 设置：简介
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    /**
     * 获取：简介
     */
    public String getIntroduction() {
        return introduction;
    }
}
