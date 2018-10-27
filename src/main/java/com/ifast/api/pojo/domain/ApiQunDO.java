package com.ifast.api.pojo.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <pre>
 * </pre>
 * 
 * <small> 2018年4月27日 | Aron</small>
 */
@TableName("tb_qun")
public class ApiQunDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = -2094263845731723033L;

    @TableId
    private Long id;
    private String openGId;
    private String userId;
    private Integer sort;
    /**
     * 姓名
     */
    @TableField(exist = false)
    private String name;



    /**
     * 昵称
     */
    @TableField(exist = false)
    private String nickName;

    /**
     *用户头像
     */
    @TableField(exist = false)
    private String avatarUrl;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    @TableField(exist = false)
    private String gender;

    /**
     * 用户所在城市
     */
    @TableField(exist = false)
    private String city;

    /**
     * 用户所在省份
     */
    @TableField(exist = false)
    private String province;

    /**
     * 用户所在国家
     */
    @TableField(exist = false)
    private String country;

    /**
     * 用户信息
     */
    @TableField(exist = false)
    private String language;

    /**
     * 职位
     */
    @TableField(exist = false)
    private String position;
    /**
     * 电话
     */
    @TableField(exist = false)
    private String phone;
    /**
     * 微信
     */
    @TableField(exist = false)
    private String wechat;
    /**
     * QQ
     */
    @TableField(exist = false)
    private String qq;
    /**
     * 邮箱
     */
    @TableField(exist = false)
    private String email;
    /**
     * 地址
     */
    @TableField(exist = false)
    private String address;

    @TableField(exist = false)
    private String dName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenGId() {
        return openGId;
    }

    public void setOpenGId(String openGId) {
        this.openGId = openGId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
