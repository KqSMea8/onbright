package com.bright.apollo.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TYaoKongYunBrand {

    @JsonIgnore
    private Date lastOpTime;
    @Expose
    @SerializedName("bid")
    @JsonProperty(value="bid")
    private Integer bId;

    @Expose
    @SerializedName("name")
    @JsonProperty(value="name")
    private String name;

    @JsonIgnore
    private Integer deviceType;

    @Expose
    @SerializedName("common")
    @JsonProperty(value="common")
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
