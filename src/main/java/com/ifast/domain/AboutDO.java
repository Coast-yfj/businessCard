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
 * <small> 2018-09-04 14:59:58 | Aron</small>
 */
 @TableName("tb_about")
public class AboutDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /**  */
    @TableId
    private Long id;
    /**  */
    private String path;
    /**  */
    private String content;

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
     * 设置：
     */
    public void setPath(String path) {
        this.path = path;
    }
    /**
     * 获取：
     */
    public String getPath() {
        return path;
    }
    /**
     * 设置：
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * 获取：
     */
    public String getContent() {
        return content;
    }
}
