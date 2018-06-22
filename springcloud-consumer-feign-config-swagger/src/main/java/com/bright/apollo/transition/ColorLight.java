package com.bright.apollo.transition;

import org.json.JSONArray;

public class ColorLight extends Light {

    private String[] colorLightActions;

    private JSONArray colorLightProperties;

    public String[] getColorLightActions() {
        return colorLightActions;
    }

    public void setColorLightActions(String[] colorLightActions) {
        this.colorLightActions = colorLightActions;
    }

    public JSONArray getColorLightProperties() {
        return colorLightProperties;
    }

    public void setColorLightProperties(JSONArray colorLightProperties) {
        this.colorLightProperties = colorLightProperties;
    }


}
