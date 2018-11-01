package com.bright.apollo.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class BrandResult {
    /**
     * 总的条数
     */
    @Expose
    private int sm;

    /**
     * 所有品牌列表
     */
    @Expose
    private List<Brand> rs ;

    public int getSm() {
        return sm;
    }

    public void setSm(int sm) {
        this.sm = sm;
    }

    public List<Brand> getRs() {
        return rs;
    }

    public void setRs(List<Brand> rs) {
        this.rs = rs;
    }
}
