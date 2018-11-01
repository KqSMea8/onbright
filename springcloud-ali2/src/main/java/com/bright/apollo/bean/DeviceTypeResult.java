package com.bright.apollo.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class DeviceTypeResult {

    @Expose
    private int sm ;

    /**
     * 返回的集合数目
     */
    @Expose
    private List<DeviceType> rs ;

    public int getSm() {
        return sm;
    }

    public void setSm(int sm) {
        this.sm = sm;
    }

    public List<DeviceType> getRs() {
        return rs;
    }

    public void setRs(List<DeviceType> rs) {
        this.rs = rs;
    }
}
