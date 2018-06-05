package com.bright.apollo.common.rokid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

public class RokidRequestAction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Expose
    @SerializedName("switch")
    private String[] switching;
    private String[] color_temperature;
    private String[] brightness;
    public String[] getSwitching() {
        return switching;
    }
    public void setSwitching(String[] switching) {
        this.switching = switching;
    }
    public String[] getColor_temperature() {
        return color_temperature;
    }
    public void setColor_temperature(String[] color_temperature) {
        this.color_temperature = color_temperature;
    }
    public String[] getBrightness() {
        return brightness;
    }
    public void setBrightness(String[] brightness) {
        this.brightness = brightness;
    }
    @Override
    public String toString() {
        return "{switching=" + Arrays.toString(switching) + ", color_temperature="
                + Arrays.toString(color_temperature) + ", brightness=" + Arrays.toString(brightness) + "}";
    }
}
