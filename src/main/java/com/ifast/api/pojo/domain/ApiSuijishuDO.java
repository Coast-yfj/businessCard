package com.ifast.api.pojo.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * </pre>
 * 
 * <small> 2018年4月27日 | Aron</small>
 */
@TableName("tb_suijishu")
public class ApiSuijishuDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = -864188199991582716L;


    @TableId
    private Long id;
    private Long  userId;
    private String suijishu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSuijishu() {
        return suijishu;
    }

    public void setSuijishu(String suijishu) {
        this.suijishu = suijishu;
    }
}
