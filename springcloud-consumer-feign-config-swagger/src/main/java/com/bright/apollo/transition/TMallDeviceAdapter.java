package com.bright.apollo.transition;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import org.json.JSONArray;

import java.util.Map;

public class TMallDeviceAdapter implements ThirdPartyTransition{

    private TOboxDeviceConfig oboxDeviceConfig;

    private Map<String, Object> map;

    public TMallDeviceAdapter(TOboxDeviceConfig oboxDeviceConfig){
        this.oboxDeviceConfig = oboxDeviceConfig;
    }

    public TMallDeviceAdapter(Map<String, Object> map){
        this.map = map;
    }

    public TMallDeviceAdapter(){

    }

    private JSONArray defaultProperties;

    private String[] defaultAction;

    public JSONArray getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(JSONArray defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public String[] getDefaultAction() {
        return defaultAction;
    }

    public void setDefaultAction(String[] defaultAction) {
        this.defaultAction = defaultAction;
    }

    @Override
    public TMallDeviceAdapter onbright2TMall() {
        return null;
    }

    @Override
    public TOboxDeviceConfig TMall2Obright() {
        return null;
    }
}
