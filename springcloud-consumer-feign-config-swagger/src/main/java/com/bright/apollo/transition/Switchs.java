package com.bright.apollo.transition;

import org.json.JSONArray;

public class Switchs extends TMallDeviceAdapter{

    private String[] switchActions;

    private JSONArray switchProperties;

    public String[] getSwitchActions() {
        return switchActions;
    }

    public void setSwitchActions(String[] switchActions) {
        this.switchActions = switchActions;
    }

    public JSONArray getSwitchProperties() {
        return switchProperties;
    }

    public void setSwitchProperties(JSONArray switchProperties) {
        this.switchProperties = switchProperties;
    }
}
