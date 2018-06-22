package com.bright.apollo.transition;

import org.json.JSONArray;

public class Light extends  TMallDeviceAdapter{

    private String[] lightActions;

    private JSONArray lightProperties;

    public String[] getLightActions() {
        return lightActions;
    }

    public void setLightActions(String[] lightActions) {
        this.lightActions = lightActions;
    }

    public JSONArray getLightProperties() {
        return lightProperties;
    }

    public void setLightProperties(JSONArray lightProperties) {
        this.lightProperties = lightProperties;
    }
}
