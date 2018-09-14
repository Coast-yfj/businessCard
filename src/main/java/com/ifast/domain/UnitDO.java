package com.ifast.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    public UnitDO() {

    }

    public UnitDO(Long userId) {
        this.userId = userId;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /** 主键 */
    @TableId
    private Long id;
    /** 名称 */
    private String name;
    /** 规模 */
    private String scale;
    /** 简介 */
    private String introduction;
    /**用户id*/
    private Long userId;

    /**
     * 固定电话
     */
    private String fixedTel;

    /**
     * 网址
     */
    private String url;
    /**
     * 类型
     */
    private String type;

    @TableField(exist = false)
    private List<ProductDO> productDOList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ProductDO> getProductDOList() {
        return productDOList;
    }

    public void setProductDOList(List<ProductDO> productDOList) {
        this.productDOList = productDOList;
    }

    public String getFixedTel() {
        return fixedTel;
    }

    public void setFixedTel(String fixedTel) {
        this.fixedTel = fixedTel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 设置：主键
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * 获取：主键
     */
    public Long getId() {
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
