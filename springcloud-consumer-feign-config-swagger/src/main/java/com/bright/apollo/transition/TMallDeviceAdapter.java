package com.bright.apollo.transition;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.ColorEnum;
import com.bright.apollo.tool.ByteHelper;
import com.zz.common.util.ArrayUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TMallDeviceAdapter implements ThirdPartyTransition{

    private TOboxDeviceConfig oboxDeviceConfig;

    private TMallTemplate tMallTemplate;

    private Map<String, Object> playloadMap;

    public TMallDeviceAdapter(TOboxDeviceConfig oboxDeviceConfig,TMallTemplate tMallTemplate){
        this.oboxDeviceConfig = oboxDeviceConfig;
        this.tMallTemplate = tMallTemplate;
    }

    public TMallDeviceAdapter(Map<String, Object> map,TMallTemplate tMallTemplate,TOboxDeviceConfig oboxDeviceConfig){
        this.playloadMap = map;
        this.tMallTemplate = tMallTemplate;
        this.oboxDeviceConfig = oboxDeviceConfig;
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

    public static String lighticon = "https://github.com/onbright-canton/onbrightConfig/blob/master/tmallImg/light.png";

    public static String singleOutleticon = "https://github.com/onbright-canton/onbrightConfig/blob/master/tmallImg/singleOut.png";

    public static String mutipleOutleticon = "https://github.com/onbright-canton/onbrightConfig/blob/master/tmallImg/mutipleOutlet.png";

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String icon = "https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/uploads/image.png";

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


    private void propertiesTransition(Map<String,Object> dfMap,String[] properties){
        String[] propertyString = null;
        for(String property : properties){
            propertyString = property.split("-");
            dfMap.put(propertyString[0],propertyString[1]);
        }
    }

    //转换动作
    private String[] actionsTransition(String[] specialAction,String[] defaultAction){
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

    //设置设备参数
    private void setProperty(TMallDeviceAdapter tMallDeviceAdapter,TOboxDeviceConfig oboxDeviceConfig){
        tMallDeviceAdapter.setDeviceId(oboxDeviceConfig.getDeviceSerialId());
        tMallDeviceAdapter.setDeviceName(oboxDeviceConfig.getDeviceId());
        tMallDeviceAdapter.setDeviceType(oboxDeviceConfig.getDeviceType());
        tMallDeviceAdapter.setBrand("on-bright");
        tMallDeviceAdapter.setZone("");
        String deviceType = oboxDeviceConfig.getDeviceType();
        String childType = oboxDeviceConfig.getDeviceChildType();
        if(deviceType.equals("light")){
            tMallDeviceAdapter.setModel("灯");
            tMallDeviceAdapter.setIcon(lighticon);
        }else if(deviceType.equals("outlet")&&(childType.equals("01")||childType.equals("02"))){
            tMallDeviceAdapter.setModel("单孔插座");
            tMallDeviceAdapter.setIcon(singleOutleticon);
        }else if(deviceType.equals("outlet")&&(childType.equals("17")||childType.equals("2b"))){
            tMallDeviceAdapter.setModel("多孔插座");
            tMallDeviceAdapter.setIcon(mutipleOutleticon);
        }else{
            tMallDeviceAdapter.setModel("");
            tMallDeviceAdapter.setIcon(icon);
        }
    }

//    private void setMutipleOutLetExtends(TMallDeviceAdapter tMallDeviceAdapter,Mutipleswitch mutipleswitch){//3路开关配置3个单路开关
//        for(int i=1;i<=3;i++){
//            Singleswitch singleswitch = new Singleswitch();
//            singleswitch.setDeviceId(mutipleswitch.getDeviceId()+"_"+i);
//            singleswitch.setDeviceName(mutipleswitch.getDeviceName());
//            singleswitch.setDeviceType("04");
//            singleswitch.setModel("单孔插座");
//            singleswitch.setIcon(singleOutleticon);
//        }
//
//    }

    //设置灯设备动作和属性
    private void setLight(TMallDeviceAdapter tMallDeviceAdapter,String[] defaultActions,Map<String,Object> dfMap ){
        JSONArray jsonArray = new JSONArray();
        String[] lightActions = tMallTemplate.getLightActions().split("\\|");
        String[] lightProperties = tMallTemplate.getLightProperties().split("\\|");
        if(tMallDeviceAdapter instanceof ColorLight == false){
            for(int i=0;i<lightActions.length;i++){
                String lightAction = lightActions[i];
                if(lightAction.equals("SetColor")){
                    lightActions = (String[]) ArrayUtils.remove(lightActions,i);
                }
            }
            for(int i=0;i<lightProperties.length;i++){
                String lightProperty = lightProperties[i];
                if(lightProperty.indexOf("color")>=0){
                    lightProperties = (String[])  ArrayUtils.remove(lightProperties,i);
                }
            }
        }
        propertiesTransition(dfMap,lightProperties);
        jsonArray.put(dfMap);
        tMallDeviceAdapter.setProperties(jsonArray);
        tMallDeviceAdapter.setAction(actionsTransition(lightActions,defaultActions));
    }

    //发现设备转换
    private TMallDeviceAdapter transition(TMallTemplate tMallTemplate, TOboxDeviceConfig oboxDeviceConfig){
        String deviceType = oboxDeviceConfig.getDeviceType();
        String deviceChildType = oboxDeviceConfig.getDeviceChildType();
        String deviceState = oboxDeviceConfig.getDeviceState();
        String[] defaultActions = tMallTemplate.getDefaultAction().split("\\|");
        String[] defaultProperties = tMallTemplate.getDefaultProperties().split("\\|");
        Map<String,Object> dfMap = new HashMap<String, Object>();
        JSONArray jsonArray = new JSONArray();
        if(deviceType.equals("01")){//灯设备
            oboxDeviceConfig.setDeviceType("light");
            String onOff = deviceState.substring(0,2);
            for(int i=0;i<defaultProperties.length;i++){
                String def = defaultProperties[i];
                if(onOff.equals("ff")&&def.indexOf("off")>=0){
                    defaultProperties = (String[]) ArrayUtils.remove(defaultProperties,i);
                }else if(onOff.equals("00")&&def.indexOf("on")>=0){
                    defaultProperties = (String[]) ArrayUtils.remove(defaultProperties,i);
                }
            }
            propertiesTransition(dfMap,defaultProperties);
            if(deviceChildType.equals("01")){//单色灯
                SingleLight singleLight = new SingleLight();
                setProperty(singleLight,oboxDeviceConfig);
                setLight(singleLight,defaultActions,dfMap);
                return singleLight;
            }else if(deviceChildType.equals("02")){//冷暖色灯
                WarmCollLight warmCollLight = new WarmCollLight();
                setProperty(warmCollLight,oboxDeviceConfig);
                setLight(warmCollLight,defaultActions,dfMap);
                return warmCollLight;
            }else if(deviceChildType.equals("03")){//彩灯
                ColorLight colorLight = new ColorLight();
                setProperty(colorLight,oboxDeviceConfig);
                setLight(colorLight,defaultActions,dfMap);
                return colorLight;
            }
        }else if(deviceType.equals("04")){//智能插座/开关
            oboxDeviceConfig.setDeviceType("outlet");
            propertiesTransition(dfMap,defaultProperties);
            if(deviceChildType.equals("01")||deviceChildType.equals("02")){//一路开关
                Singleswitch singleswitch = new Singleswitch();
                setProperty(singleswitch,oboxDeviceConfig);
                singleswitch.setAction(defaultActions);
                jsonArray.put(dfMap);
                singleswitch.setProperties(jsonArray);
                return singleswitch;
            }else if(deviceChildType.equals("2b")||deviceChildType.equals("17")){//3路开关
                Mutipleswitch mutipleswitch = new Mutipleswitch();
                setProperty(mutipleswitch,oboxDeviceConfig);
                mutipleswitch.setAction(defaultActions);
                jsonArray.put(dfMap);
                mutipleswitch.setProperties(jsonArray);
                return mutipleswitch;
            }
        }else if(deviceType.equals("05")){//开合类设备
            propertiesTransition(dfMap,defaultProperties);
            if(deviceChildType.equals("01")){//窗帘
                oboxDeviceConfig.setDeviceType("curtain");
                Curtain curtain = new Curtain();
                setProperty(curtain,oboxDeviceConfig);
                curtain.setAction(defaultActions);
                jsonArray.put(dfMap);
                curtain.setProperties(jsonArray);
                return curtain;
            }
        }

        return null;
    }

    private Map<String,Object> compositeCommand(TMallTemplate tMallTemplate, TOboxDeviceConfig oboxDeviceConfig,Map<String,Object> playloadMap){
        String attribute = (String)playloadMap.get("attribute");
        String value = (String)playloadMap.get("value");
        String deviceId = (String)playloadMap.get("deviceId");
        String deviceType = (String)playloadMap.get("deviceType");
        String lightProperties = tMallTemplate.getLightProperties();
        String dfControllProperties = tMallTemplate.getDefaultControllProperties();
        String lightControllProperties = tMallTemplate.getLightControllProperties();
        String deviceState = oboxDeviceConfig.getDeviceState();
        List<String> propertiesLists = new ArrayList<String>();
        Map<String,Object> returnMap = new HashMap<String, Object>();
        String[] dfControllArrays = dfControllProperties.split("\\|");
        for (String dfControll: dfControllArrays){
            propertiesLists.add(dfControll);
        }
        String[] lightControllArrays = lightControllProperties.split("\\|");
        for (String lightControllPropertiy:lightControllArrays){
            propertiesLists.add(lightControllPropertiy);
        }
        String[] lightPropertiesArrays = lightProperties.split("\\|");
        for (String lightProperty:lightPropertiesArrays){
            propertiesLists.add("light_"+lightProperty);
        }

        for (String properties : propertiesLists){
            String[] propertyArr =  properties.split("-");
            if(deviceType.equals("light")){
                if(("light_"+attribute+"_"+value).equals(propertyArr[0])){
                    value = propertyArr[1];
                    deviceState = changeState(deviceState,value);
                }else if(("light_"+attribute).equals(propertyArr[0])){
                    if(attribute.equals("brightnessStep")){
                        Integer val = Integer.valueOf(value);
                        value =  ByteHelper.int2HexString(val*255/100);
                        deviceState = changeState(deviceState,value);
                    }else if(attribute.equals("brightness")){
                        if(value.equals("max")){
                            value = "ff";
                        }else if(value.equals("min")){
                            value = "00";
                        }else{
                            Integer val = Integer.valueOf(value);
                            value =  ByteHelper.int2HexString(val*255/100);
                        }
                        deviceState = changeState(deviceState,value);
                    }else if(attribute.equals("color")){
                        value = ColorEnum.getRegion(value).getValue();
                        deviceState = changeColorState(deviceState,value);
                    }

                }
            }else if((attribute+"_"+value).equals(propertyArr[0])){
                value = propertyArr[1];
                deviceState = changeState(deviceState,value);
            }
        }
        returnMap.put("deviceId",deviceId);
        returnMap.put("deviceState",deviceState);
        return returnMap;
    }

    private String changeState(String deviceState,String value){
        String endStr = deviceState.substring(2,deviceState.length());
        return value+endStr;
    }

    private String changeColorState(String deviceState,String value){
        String beginStr = deviceState.substring(0,6);
        String endStr = deviceState.substring(12,deviceState.length());
        return beginStr+value+endStr;
    }

    @Override
    public TMallDeviceAdapter onbright2TMall() {
        return transition(tMallTemplate,oboxDeviceConfig);
    }

    @Override
    public Map<String,Object> TMall2Obright() {
        return compositeCommand(tMallTemplate,oboxDeviceConfig,playloadMap);
    }

    @Override
    public String toString(){
        return "deviceId:"+getDeviceId()+"\n deviceName:"+getDeviceName()+"\n properties:"+getProperties()+
               "\n action:"+getAction()+"\n zone:"+getZone()+"\n model:"+getModel()+"\n brand:"+getBrand()+
                "\n icon:"+getIcon();
    }
}
