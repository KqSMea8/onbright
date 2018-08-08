package com.bright.apollo.common.entity;

import java.util.Date;

public class TYaoKongYunBrand {
    private Date lastOpTime;
    private Integer bId;
    private String name;
    private Integer deviceType;
    private Integer common;

    public Date getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    public Integer getbId() {
        return bId;
    }

    public void setbId(Integer bId) {
        this.bId = bId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getCommon() {
        return common;
    }

    public void setCommon(Integer common) {
        this.common = common;
    }
}
