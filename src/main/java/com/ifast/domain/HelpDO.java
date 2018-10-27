package com.ifast.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;



/**
 * 
 * <pre>
 * @author
 * </pre>
 * <small> 2018-10-27 13:39:31 | Aron</small>
 */
 @TableName("tb_help")
public class HelpDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    /** 主键 */
    @TableId
    private Long id;
    /**  内容*/
    private String context;
    /**
     * 标题
     */
    private String title;

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
    public void setContext(String context) {
        this.context = context;
    }
    /**
     * 获取：
     */
    public String getContext() {
        return context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "HelpDO{" +
                "id=" + id +
                ", context='" + context + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
