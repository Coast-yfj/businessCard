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
@TableName("app_active")
public class ActiveDO implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = -2961783559548963737L;

    @TableId
    private Long id;
    private String startTtime;
    private String endTime;
    private Date createTime;
    private Long createUserId;
    private String title;
    //sheng
    private String province;
    //市
    private String city;
    //县
    private String county;
//    类型
    private String type;
    private String address;
    //经度
    private String longitude;
    //weidu
    private String latitude;
    //详细内容
    private String content;
    //0：未暂停，1：已暂停
    private String stop;

    @TableField(exist = false)
    private List<Long> imgIds;

    @TableField(exist = false)
    private List<ImgDO> imgs;

    @TableField(exist = false)
    private List<String> users;

    @TableField(exist = false)
    private int userTotal;

    @TableField(exist = false)
    private int pageSize = 10;
    @TableField(exist = false)
    private  int pageNo = 1;

    //当前时间
    @TableField(exist = false)
    private String day;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public List<ImgDO> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImgDO> imgs) {
        this.imgs = imgs;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public int getUserTotal() {
        return userTotal;
    }

    public void setUserTotal(int userTotal) {
        this.userTotal = userTotal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public List<Long> getImgIds() {
        return imgIds;
    }

    public void setImgIds(List<Long> imgIds) {
        this.imgIds = imgIds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getStartTtime() {
        return startTtime;
    }

    public void setStartTtime(String startTtime) {
        this.startTtime = startTtime;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }
}
