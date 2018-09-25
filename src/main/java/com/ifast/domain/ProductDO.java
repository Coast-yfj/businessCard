package com.ifast.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.ifast.api.pojo.domain.ImgDO;


/**
 * <pre>
 *
 * </pre>
 * <small> 2018-08-19 19:10:02 | Aron</small>
 */
@TableName("tb_product")
public class ProductDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 详情
     */
    private String item;
    /**
     * 图片路径(废)
     */
//    private String path;
    /**
     * 公司id
     */
    private Long unitId;

    @TableField(exist = false)
    private List<String> imgIds;
    @TableField(exist = false)
    private List<ImgDO> imgs;

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
     * 设置：详情
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * 获取：详情
     */
    public String getItem() {
        return item;
    }

 /*   *//**
     * 设置：图片路径
     *//*
    public void setPath(String path) {
        this.path = path;
    }

    *//**
     * 获取：图片路径
     *//*
    public String getPath() {
        return path;
    }*/

    /**
     * 设置：公司id
     */
    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    /**
     * 获取：公司id
     */
    public Long getUnitId() {
        return unitId;
    }

    public List<String> getImgIds() {
        return imgIds;
    }

    public void setImgIds(List<String> imgIds) {
        this.imgIds = imgIds;
    }

    public List<ImgDO> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImgDO> imgs) {
        this.imgs = imgs;
    }
}
