package com.bright.apollo.bean;

import com.bright.apollo.common.entity.MatchRemoteControl;
import com.google.gson.annotations.Expose;

import java.util.List;

public class MatchRemoteControlResult {

    @Expose
    private int sm ;

    /**
     * 返回的集合数目
     */
    @Expose
    private List<MatchRemoteControl> rs ;

    public int getSm() {
        return sm;
    }

    public void setSm(int sm) {
        this.sm = sm;
    }

    public List<MatchRemoteControl> getRs() {
        return rs;
    }

    public void setRs(List<MatchRemoteControl> rs) {
        this.rs = rs;
    }
}
