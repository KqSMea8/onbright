package com.bright.apollo.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceType {

    @Expose
    @SerializedName("t")
    @JsonProperty(value="t")
    private int tid ;

    /**
     * 设备型号中文名称
     */
    @Expose
    private String name ;

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
