package com.bright.apollo.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KeyCode implements Serializable {

    private static final long serialVersionUID = -2222l;

    /**
     * 键显示名称
     */
    @Expose(serialize = false)
    private String kn;

    public String getKn() {
        return kn;
    }

    public void setKn(String kn) {
        this.kn = kn;
    }

    public String getSrcCode() {
        return srcCode;
    }

    public void setSrcCode(String srcCode) {
        this.srcCode = srcCode;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 键原始码库
     */
    @Expose
    @SerializedName("src")
    @JsonIgnore
    private String srcCode;

    /**
     * 短码
     */
    @Expose(serialize = false)
    @SerializedName("short")
    @JsonProperty(value="short")
    private String shortCode;

    /**
     * 排序
     */
    @Expose(serialize = false)
    private int order;
}
