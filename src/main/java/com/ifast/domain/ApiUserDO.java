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
 * <small> 2018-08-19 19:10:03 | Aron</small>
 */
 @TableName("tb_user")
public class ApiUserDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /** 主键 */
    @TableId
    private Integer id;
    /** 姓名 */
    private String name;
    /** 公司id */
    private String unitId;
    /** 职位 */
    private String position;
    /** 电话 */
    private String phone;
    /** 微信 */
    private String wechat;
    /** QQ */
    private String qq;
    /** 邮箱 */
    private String email;
    /** 地址 */
    private String address;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：姓名
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 获取：姓名
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：公司id
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    /**
     * 获取：公司id
     */
    public String getUnitId() {
        return unitId;
    }
    /**
     * 设置：职位
     */
    public void setPosition(String position) {
        this.position = position;
    }
    /**
     * 获取：职位
     */
    public String getPosition() {
        return position;
    }
    /**
     * 设置：电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * 获取：电话
     */
    public String getPhone() {
        return phone;
    }
    /**
     * 设置：微信
     */
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
    /**
     * 获取：微信
     */
    public String getWechat() {
        return wechat;
    }
    /**
     * 设置：QQ
     */
    public void setQq(String qq) {
        this.qq = qq;
    }
    /**
     * 获取：QQ
     */
    public String getQq() {
        return qq;
    }
    /**
     * 设置：邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * 获取：邮箱
     */
    public String getEmail() {
        return email;
    }
    /**
     * 设置：地址
     */
    public void setAddress(String address) {
        this.address = address;
    }
    /**
     * 获取：地址
     */
    public String getAddress() {
        return address;
    }
}
