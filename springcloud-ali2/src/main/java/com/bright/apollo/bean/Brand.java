package com.bright.apollo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand {
    /**
     * 品牌ID
     */
    @Expose
    @SerializedName("bid")
    @JsonProperty(value="bid")
    private int bid ;

    /**
     * 被遥控设备品牌中文名称
     */
    @Expose
    @SerializedName("name")
    @JsonProperty(value="name")
    private String name ;

    /**
     * 常用品牌标识
     */
    @Expose
    @SerializedName("common")
    @JsonProperty(value="common")
    private int common;

    @JsonIgnore
    private int deviceType;
    public int getCommon() {
        return common;
    }

    public void setCommon(int common) {
        this.common = common;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
