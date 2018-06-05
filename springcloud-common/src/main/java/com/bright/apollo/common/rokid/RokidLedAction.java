package com.bright.apollo.common.rokid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;

public class RokidLedAction implements Serializable,RokidAction {

    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("switch")
    private String[] switching;
    @Expose
    private String[] brightness;
    @Expose
    private String[] color_temperature;
    public String[] getSwitching() {
        return switching;
    }
    public void setSwitching(String[] switching) {
        this.switching = switching;
    }
    public String[] getBrightness() {
        return brightness;
    }
    public void setBrightness(String[] brightness) {
        this.brightness = brightness;
    }
    public String[] getColor_temperature() {
        return color_temperature;
    }
    public void setColor_temperature(String[] color_temperature) {
        this.color_temperature = color_temperature;
    }
    @Override
    public String toString() {
        return "{switching=" + Arrays.toString(switching) + ", brightness=" + Arrays.toString(brightness)
                + ", color_temperature=" + Arrays.toString(color_temperature) + "}";
    }
}
