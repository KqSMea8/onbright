package com.bright.apollo.transition;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class TMallDeviceAdapter implements ThirdPartyTransition{

    private TOboxDeviceConfig oboxDeviceConfig;

    private TMallTemplate tMallTemplate;

    private Map<String, Object> map;

    public TMallDeviceAdapter(TOboxDeviceConfig oboxDeviceConfig,TMallTemplate tMallTemplate){
        this.oboxDeviceConfig = oboxDeviceConfig;
        this.tMallTemplate = tMallTemplate;
        this.brand = "XXX";
        this.model = "XXX";
        this.zone = "XXX";
    }

    public TMallDeviceAdapter(Map<String, Object> map){
        this.map = map;
    }

    public TMallDeviceAdapter(){

    }

    private JSONArray properties;

    private String[] action;

    private String deviceId;

    private String deviceName;

    private String deviceType;

    private String zone;

    private String brand;

    private String model;

    private String icon = "https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/image.png";

    public JSONArray getProperties() {
        return properties;
    }

    public void setProperties(JSONArray properties) {
        this.properties = properties;
    }

    public String[] getAction() {
        return action;
    }

    public void setAction(String[] action) {
        this.action = action;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private void PropertiesTransition(Map<String,Object> dfMap,String[] properties){
        String[] propertyString = null;
        for(String property : properties){
            propertyString = property.split("-");
            dfMap.put(propertyString[0],propertyString[1]);
        }
    }

    private String[] ActionsTransition(String[] specialAction,String[] defaultAction){
        int idx = 0;
        String[] actions = new String[specialAction.length+defaultAction.length];
        for(String action : specialAction){
            actions[idx] = action;
            idx++;
        }
        for(String action : defaultAction){
            actions[idx] = action;
            idx++;
        }
        return actions;
    }

    private void SetLight(TMallDeviceAdapter tMallDeviceAdapter,String[] defaultActions,Map<String,Object> dfMap ){
        JSONArray jsonArray = new JSONArray();
        String[] lightActions = tMallTemplate.getLightActions().split("\\|");
        String[] lightProperties = tMallTemplate.getLightProperties().split("\\|");
        PropertiesTransition(dfMap,lightProperties);
        jsonArray.put(dfMap);
        tMallDeviceAdapter.setProperties(jsonArray);
        tMallDeviceAdapter.setAction(ActionsTransition(lightActions,defaultActions));
    }

    private TMallDeviceAdapter transition(TMallTemplate tMallTemplate, TOboxDeviceConfig oboxDeviceConfig){
        String deviceType = oboxDeviceConfig.getDeviceType();
        String deviceChildType = oboxDeviceConfig.getDeviceChildType();
        String[] defaultActions = tMallTemplate.getDefaultAction().split("\\|");
        String[] defaultProperties = tMallTemplate.getDefaultProperties().split("\\|");
        Map<String,Object> dfMap = new HashMap<String, Object>();
        PropertiesTransition(dfMap,defaultProperties);
        this.setDeviceId(oboxDeviceConfig.getDeviceSerialId());
        this.setDeviceName(oboxDeviceConfig.getDeviceId());
        JSONArray jsonArray = new JSONArray();
        if(deviceType.equals("01")&&deviceChildType.equals("01")){//单色灯
            SingleLight singleLight = new SingleLight();
            SetLight(singleLight,defaultActions,dfMap);
            return singleLight;
        }else if(deviceType.equals("01")&&deviceChildType.equals("02")){//冷暖色灯
            WarmCollLight warmCollLight = new WarmCollLight();
            SetLight(warmCollLight,defaultActions,dfMap);
            return warmCollLight;
        }else if(deviceType.equals("01")&&deviceChildType.equals("03")){//彩灯
            ColorLight colorLight = new ColorLight();
            SetLight(colorLight,defaultActions,dfMap);
            return colorLight;
        }else if(deviceType.equals("04")&&deviceChildType.equals("01")){//1路开关
            Singleswitch singleswitch = new Singleswitch();
            singleswitch.setAction(defaultActions);
            jsonArray.put(dfMap);
            singleswitch.setProperties(jsonArray);
            return singleswitch;
        }else if(deviceType.equals("05")&&deviceChildType.equals("01")){//窗帘
            Curtain curtain = new Curtain();
            curtain.setAction(defaultActions);
            jsonArray.put(dfMap);
            curtain.setProperties(jsonArray);
            return curtain;
        }
        return null;
    }

    @Override
    public TMallDeviceAdapter onbright2TMall() {
        return transition(tMallTemplate,oboxDeviceConfig);
    }

    @Override
    public TOboxDeviceConfig TMall2Obright() {
        return null;
    }

    @Override
    public String toString(){
        return "deviceId:"+getDeviceId()+"\n deviceName:"+getDeviceName()+"\n properties:"+getProperties()+
               "\n action:"+getAction();
    }
}
