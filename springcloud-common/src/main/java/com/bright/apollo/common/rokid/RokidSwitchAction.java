package com.bright.apollo.common.rokid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

public class RokidSwitchAction implements Serializable,RokidAction {
    private static final long serialVersionUID = 1L;
    @Expose
    @SerializedName("switch")
    private String[] switching;
    public String[] getSwitching() {
        return switching;
    }
    public void setSwitching(String[] switching) {
        this.switching = switching;
    }
    @Override
    public String toString() {
        return "{switch=" + Arrays.toString(switching) + "}";
    }
}
