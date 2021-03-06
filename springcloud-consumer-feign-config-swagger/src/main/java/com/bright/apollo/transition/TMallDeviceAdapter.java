package com.bright.apollo.transition;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TYaokonyunKeyCode;
import com.bright.apollo.enums.AirconditionEnum;
import com.bright.apollo.enums.ColorEnum;
import com.bright.apollo.enums.TmallIRFUNEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.redis.RedisBussines;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.tool.ByteHelper;
import com.zz.common.util.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class TMallDeviceAdapter implements ThirdPartyTransition{

    private ReentrantLock lock = new ReentrantLock();

    private TOboxDeviceConfig oboxDeviceConfig;

    private TMallTemplate tMallTemplate;

    private Map<String, Object> playloadMap;

    public RedisBussines getRedisBussines() {
        return redisBussines;
    }

    public void setRedisBussines(RedisBussines redisBussines) {
        this.redisBussines = redisBussines;
    }

    private RedisBussines redisBussines;

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    private Map<String,Object> header;

    private List<TYaokonyunKeyCode> yaokonyunKeyCodeList;

    public TMallDeviceAdapter(TOboxDeviceConfig oboxDeviceConfig,TMallTemplate tMallTemplate){
        this.oboxDeviceConfig = oboxDeviceConfig;
        this.tMallTemplate = tMallTemplate;
    }

    public TMallDeviceAdapter(Map<String, Object> map,TMallTemplate tMallTemplate,TOboxDeviceConfig oboxDeviceConfig,Map<String,Object> header){
        this.playloadMap = map;
        this.tMallTemplate = tMallTemplate;
        this.oboxDeviceConfig = oboxDeviceConfig;
        this.header = header;
    }

    public TMallDeviceAdapter(Map<String, Object> map, TMallTemplate tMallTemplate, List<TYaokonyunKeyCode> yaokonyunKeyCodeList, Map<String,Object> header){
        this.playloadMap = map;
        this.tMallTemplate = tMallTemplate;
        this.yaokonyunKeyCodeList = yaokonyunKeyCodeList;
        this.header = header;
    }

    public TMallDeviceAdapter(){

    }

    private JSONArray properties;

//    private String[] action;

    private String deviceId;

    private String deviceName;

    private String deviceType;

    private String zone;

    private String brand;

    private String model;

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    private String queryName;

    public static String lighticon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/light.png";

    public static String remotelighticon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/remotelight.png";

    public static String singleOutleticon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/singleOut.png";//插座

    public static String mutipleOutleticon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/panel.png";//开关

    public static String doorLockicon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/doorlock.png";

    public static String aerosolizericon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/aerosolizer.png";//烟雾传感器

    public static String environmentsensoricon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/environmentsensor.png";//环境传感器

    public static String gatemagnetismicon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/gatemagnetism.png";//门磁

    public static String humanIRicon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/humanIR.png";//人体红外

    public static String humituresensoricon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/humituresensor.png";//温湿度传感器

    public static String lightsensoricon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/lightsensor.png";//光感传感器

    public static String radaricon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/radar.png";//雷达传感器

    public static String watersensoricon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/watersensor.png";//水浸传感器

    public static String curtainicon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/curtain.png";//窗帘

    public static String tvicon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/tv.png";//电视

    public static String airconditionicon = "https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/aircondition.png";//空调


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

//    public String[] getAction() {
//        return action;
//    }
//
//    public void setAction(String[] action) {
//        this.action = action;
//    }

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


    private void propertiesTransition(JSONObject dfMap,String[] properties) {
        String[] propertyString = null;
        for(String property : properties){
            propertyString = property.split("-");
            try {
                dfMap.put(propertyString[0],propertyString[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //设置设备参数
    private void setProperty(TMallDeviceAdapter tMallDeviceAdapter,TOboxDeviceConfig oboxDeviceConfig){
        tMallDeviceAdapter.setDeviceId(oboxDeviceConfig.getDeviceSerialId());
        tMallDeviceAdapter.setDeviceType(oboxDeviceConfig.getDeviceType());
        tMallDeviceAdapter.setBrand("on-bright");
        tMallDeviceAdapter.setZone("");
        String deviceType = oboxDeviceConfig.getDeviceType();
        String childType = oboxDeviceConfig.getDeviceChildType();
        if(deviceType.equals("light")){
            tMallDeviceAdapter.setModel("灯");
            tMallDeviceAdapter.setDeviceName("灯");
            tMallDeviceAdapter.setIcon(lighticon);
        }else if(deviceType.equals("outlet")&&(childType.equals("01"))){
            tMallDeviceAdapter.setModel("单孔插座");
            tMallDeviceAdapter.setDeviceName("单孔插座");
            tMallDeviceAdapter.setIcon(singleOutleticon);
        }else if(deviceType.equals("switch")&&(childType.equals("02")||childType.equals("15")||childType.equals("51"))){
            tMallDeviceAdapter.setModel("一键开关");
            tMallDeviceAdapter.setDeviceName("一键开关");
            tMallDeviceAdapter.setIcon(mutipleOutleticon);
        }else if(deviceType.equals("switch")&&(childType.equals("16")||childType.equals("2a"))){
            tMallDeviceAdapter.setModel("两键键开关");
            tMallDeviceAdapter.setDeviceName("两键开关");
            tMallDeviceAdapter.setIcon(mutipleOutleticon);
        }else if(deviceType.equals("switch")&&(childType.equals("17")||childType.equals("2b")||childType.equals("53"))){
            tMallDeviceAdapter.setModel("三键开关");
            tMallDeviceAdapter.setDeviceName("三键开关");
            tMallDeviceAdapter.setIcon(mutipleOutleticon);
        }else if(deviceType.equals("smart-gating")){
            tMallDeviceAdapter.setModel("门锁");
            tMallDeviceAdapter.setDeviceName("门锁");
            tMallDeviceAdapter.setIcon(doorLockicon);
        }else if(deviceType.equals("curtain")){
            tMallDeviceAdapter.setModel("窗帘");
            tMallDeviceAdapter.setDeviceName("窗帘");
            tMallDeviceAdapter.setIcon(curtainicon);
        }else if(deviceType.equals("sensor")){
            if(childType.equals("0b")){//温湿器
                tMallDeviceAdapter.setModel("传感器");
                tMallDeviceAdapter.setDeviceName("传感器");
                tMallDeviceAdapter.setIcon(humituresensoricon);
            }else if(childType.equals("15")){//门磁
                tMallDeviceAdapter.setModel("传感器");
                tMallDeviceAdapter.setDeviceName("传感器");
                tMallDeviceAdapter.setIcon(gatemagnetismicon);
            }
        }else if(deviceType.equals("remotelight")){
            tMallDeviceAdapter.setDeviceId(oboxDeviceConfig.getDeviceSerialId()+"_1");
            tMallDeviceAdapter.setModel("遥控灯1");
            tMallDeviceAdapter.setDeviceName("遥控灯1");
            tMallDeviceAdapter.setIcon(remotelighticon);
        }else if(deviceType.equals("television")){
            tMallDeviceAdapter.setBrand(oboxDeviceConfig.getBrandName());
            tMallDeviceAdapter.setModel("电视机");
            tMallDeviceAdapter.setDeviceName("电视机");
            tMallDeviceAdapter.setIcon(tvicon);
        }else if(deviceType.equals("aircondition")){
            tMallDeviceAdapter.setBrand(oboxDeviceConfig.getBrandName());
            tMallDeviceAdapter.setModel("空调");
            tMallDeviceAdapter.setDeviceName("空调");
            tMallDeviceAdapter.setIcon(airconditionicon);
        }else{
            tMallDeviceAdapter.setModel("");
            tMallDeviceAdapter.setIcon(icon);
        }
    }

    //设置灯设备动作和属性
    private void setLight(TMallDeviceAdapter tMallDeviceAdapter,String[] defaultActions,JSONObject dfMap ){
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
    }

    //设置电视动作和属性
    private void setTV(TMallDeviceAdapter tMallDeviceAdapter,String[] defaultActions,JSONObject dfMap ){
        JSONArray jsonArray = new JSONArray();
        String[] tvProperties = tMallTemplate.getTvProperties().split("\\|");
        propertiesTransition(dfMap,tvProperties);
        jsonArray.put(dfMap);
        tMallDeviceAdapter.setProperties(jsonArray);
    }

    //设置空调动作和属性
    private void setAircondition(TMallDeviceAdapter tMallDeviceAdapter,String[] defaultActions,JSONObject dfMap ){
        JSONArray jsonArray = new JSONArray();
        String[] airconditionProperties = tMallTemplate.getAirconditionProperties().split("\\|");
        propertiesTransition(dfMap,airconditionProperties);
        jsonArray.put(dfMap);
        tMallDeviceAdapter.setProperties(jsonArray);
    }

    //发现设备转换
    private TMallDeviceAdapter transition(TMallTemplate tMallTemplate, TOboxDeviceConfig oboxDeviceConfig){
        String deviceType = oboxDeviceConfig.getDeviceType();
        String deviceChildType = oboxDeviceConfig.getDeviceChildType();
        String deviceState = oboxDeviceConfig.getDeviceState();
        String[] defaultActions = tMallTemplate.getDefaultAction().split("\\|");
        String[] defaultProperties = tMallTemplate.getDefaultProperties().split("\\|");
        JSONObject dfMap = new JSONObject();
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
            propertiesTransition(dfMap,defaultProperties);
            if(deviceChildType.equals("01")){//插座
                oboxDeviceConfig.setDeviceType("outlet");
                SingleSwitch singleswitch = new SingleSwitch();
                setProperty(singleswitch,oboxDeviceConfig);
//                singleswitch.setAction(new String[0]);
                jsonArray.put(dfMap);
                singleswitch.setProperties(jsonArray);
                return singleswitch;
            }else if(deviceChildType.equals("02")||deviceChildType.equals("15")||deviceChildType.equals("51")){//一路开关
                oboxDeviceConfig.setDeviceType("switch");
                SingleSwitch singleswitch = new SingleSwitch();
                setProperty(singleswitch,oboxDeviceConfig);
//                singleswitch.setAction(new String[0]);
                jsonArray.put(dfMap);
                singleswitch.setProperties(jsonArray);
                return singleswitch;
            }else if(deviceChildType.equals("16")||deviceChildType.equals("2a")){//2路开关
                oboxDeviceConfig.setDeviceType("switch");
                DoubleSwitch doubleswitch = new DoubleSwitch();
                setProperty(doubleswitch,oboxDeviceConfig);
//                doubleswitch.setAction(new String[0]);
                jsonArray.put(dfMap);
                doubleswitch.setProperties(jsonArray);
                return doubleswitch;
            }else if(deviceChildType.equals("2b")||deviceChildType.equals("17")||deviceChildType.equals("53")){//3路开关
                oboxDeviceConfig.setDeviceType("switch");
                MutipleSwitch mutipleswitch = new MutipleSwitch();
                setProperty(mutipleswitch,oboxDeviceConfig);
//                mutipleswitch.setAction(new String[0]);
                jsonArray.put(dfMap);
                mutipleswitch.setProperties(jsonArray);
                return mutipleswitch;
            }
        }else if(deviceType.equals("05")){//开合类设备
            propertiesTransition(dfMap,defaultProperties);
            oboxDeviceConfig.setDeviceType("curtain");
            Curtain curtain = new Curtain();
            setProperty(curtain,oboxDeviceConfig);
//            curtain.setAction(new String[0]);
            jsonArray.put(dfMap);
            curtain.setProperties(jsonArray);
            return curtain;
        }else if(deviceType.equals("0b")){
            propertiesTransition(dfMap,defaultProperties);
            if(deviceChildType.equals("0b")){//温湿器
                oboxDeviceConfig.setDeviceType("sensor");
                Humituresensor humituresensor = new Humituresensor();
                setProperty(humituresensor,oboxDeviceConfig);
//                humituresensor.setAction(new String[0]);
                jsonArray.put(dfMap);
                humituresensor.setProperties(jsonArray);
                return humituresensor;
            }else if(deviceChildType.equals("15")){//门磁
                oboxDeviceConfig.setDeviceType("sensor");
                Gatemagnetism gatemagnetism = new Gatemagnetism();
                setProperty(gatemagnetism,oboxDeviceConfig);
//                gatemagnetism.setAction(new String[0]);
                jsonArray.put(dfMap);
                gatemagnetism.setProperties(jsonArray);
                return gatemagnetism;
            }
        }else if(deviceType.equals("15")){
            propertiesTransition(dfMap,defaultProperties);
            oboxDeviceConfig.setDeviceType("smart-gating");
            DoorLock doorLock = new DoorLock();
            setProperty(doorLock,oboxDeviceConfig);
//            doorLock.setAction(new String[0]);
            jsonArray.put(dfMap);
            doorLock.setProperties(jsonArray);
            return doorLock;
        }else if(deviceType.equals("16")){
            oboxDeviceConfig.setDeviceType("remotelight");
            propertiesTransition(dfMap,defaultProperties);
            RemoteLight remoteLight = new RemoteLight();
            setProperty(remoteLight,oboxDeviceConfig);
            setLight(remoteLight,defaultActions,dfMap);
            return remoteLight;
        }else if(deviceType.equals("2_2")){//电视机
            oboxDeviceConfig.setDeviceType("television");
            propertiesTransition(dfMap,defaultProperties);
            TV tv = new TV();
            setProperty(tv,oboxDeviceConfig);
            setTV(tv,defaultActions,dfMap);
            return tv;
        }else if(deviceType.equals("7_7")){//空调
            oboxDeviceConfig.setDeviceType("aircondition");
            propertiesTransition(dfMap,defaultProperties);
            Aircondition aircondition = new Aircondition();
            setProperty(aircondition,oboxDeviceConfig);
            setAircondition(aircondition,defaultActions,dfMap);
            return aircondition;
        }

        return null;
    }

    private TMallDeviceAdapter query(TMallTemplate tMallTemplate, TOboxDeviceConfig oboxDeviceConfig,Map<String,Boolean> isQuery) throws JSONException {
        String deviceType = oboxDeviceConfig.getDeviceType();
        String childType = oboxDeviceConfig.getDeviceChildType();
        String deviceState = oboxDeviceConfig.getDeviceState();
        TMallDeviceAdapter deviceAdapter = new TMallDeviceAdapter();
        JSONArray jsonArray = new JSONArray();
        Map<String,Object> playloadMap = this.playloadMap;
        String deviceId = (String)playloadMap.get("deviceId");
        String queryName = this.getQueryName();
        Iterator iterator = isQuery.entrySet().iterator();
        if(queryName.equals("Query")){
            while (iterator.hasNext()){
                Map.Entry<String,Boolean> entry = (Map.Entry<String,Boolean>)iterator.next();
                isQuery.put(entry.getKey(),true);
            }
        }else{
            while (iterator.hasNext()){
                Map.Entry<String,Boolean> entry = (Map.Entry<String,Boolean>)iterator.next();
                if(entry.getKey().equals(queryName)){
                    isQuery.put(entry.getKey(),true);
                }
            }
        }
        if(deviceType.equals("01")){//灯
            if(isQuery.get("Query").equals(true)){
                jsonArray.put(queryOnOff(deviceState));
            }
            if(childType.equals("02")){
                if(isQuery.get("QueryBrightness").equals(true)){
                    jsonArray.put(setWarmLightQueryBrightnessProperty(deviceState));
                }
                if(isQuery.get("QueryColorTemperature").equals(true)){
                    jsonArray.put(setWarmLightQueryTemperatureProperty(deviceState));
                }
            }
        }else if(deviceType.equals("04")){//插座/开关
            if(isQuery.get("Query").equals(true)){
                if(childType.equals("17")||childType.equals("2b")||childType.equals("53")
                        ||childType.equals("16")||childType.equals("2a")||childType.equals("53")){//开关
                    jsonArray.put(querySwitchOnOff(deviceState,deviceId));
                }else{//插座
                    jsonArray.put(queryOnOff(deviceState));
                }
            }
        }else if(deviceType.equals("15")){//门锁
            if(isQuery.get("Query").equals(true)){
                jsonArray.put(queryDoorLockOnOff(deviceState));
            }
        }else if(deviceType.equals("0b")){//传感器
            if(childType.equals("0b")){//温湿器
                jsonArray.put(queryTemperature(deviceState));
                jsonArray.put(queryHumidity(deviceState));
            }else if(childType.equals("15")){//门磁
                jsonArray.put(queryGatemagnetismOnOff(deviceState));
            }
        }else if(deviceType.equals("05")){//窗帘
            jsonArray.put(queryOnOff(deviceState));
        }
        deviceAdapter.setProperties(jsonArray);
        return deviceAdapter;
    }

    private JSONObject setWarmLightQueryBrightnessProperty(String deviceState) throws JSONException {
        JSONObject map = new JSONObject();
        String brightness = deviceState.substring(0,2);//亮度
        Integer brightnessInt = Integer.parseInt(brightness,16)-154;
        map.put("name","brightness");
        map.put("value",brightnessInt.toString());
        return map;
    }

    private JSONObject setWarmLightQueryTemperatureProperty(String deviceState) throws JSONException {
        JSONObject map = new JSONObject();
        String temperature = deviceState.substring(2,4);//色温
        Integer temperatureInt = Integer.parseInt(temperature,16)*3800/255+2700;
        map.put("name","colorTemperature");
        map.put("value",temperatureInt.toString());
        return map;
    }

    private JSONObject querySwitchOnOff(String deviceState,String deviceId) throws JSONException {
        JSONObject map = new JSONObject();
        String onoff = deviceState.substring(2,4);
        Integer middle = Integer.valueOf(onoff);
        String[] deviceIdArr = deviceId.split("_");
        String child = deviceIdArr[1];
        if(child.equals("1")){
            Integer state = middle & 0x01;
            setOnOffState(map,state);
        }else if(child.equals("2")){
            Integer state = middle & 0x04;
            setOnOffState(map,state);
        }else if(child.equals("3")){
            Integer state = middle & 0x10;
            setOnOffState(map,state);
        }
        return map;
    }

    private void setOnOffState(JSONObject map,Integer state) throws JSONException {
        if(state>0){
            map.put("name","powerstate");
            map.put("value","on");
        }else{
            map.put("name","powerstate");
            map.put("value","off");
        }
    }

    private JSONObject queryGatemagnetismOnOff(String deviceState) throws JSONException {
        JSONObject map = new JSONObject();
        String onoff = deviceState.substring(2,4);
        setOnOff(onoff,map);
        return map;
    }

    private void setOnOff(String onoff,JSONObject map) throws JSONException {
        if(onoff.equals("01")){
            map.put("name","powerstate");
            map.put("value","on");
        }else if(onoff.equals("00")){
            map.put("name","powerstate");
            map.put("value","off");
        }
    }

    private JSONObject queryOnOff(String deviceState) throws JSONException {
        JSONObject map = new JSONObject();
        String onoff = deviceState.substring(0,2);
        setOnOff(onoff,map);
        return map;
    }

    private JSONObject queryTemperature(String deviceState) throws JSONException {
        JSONObject map = new JSONObject();
        String temperature = deviceState.substring(2,4);//温度
        Integer temVal = Integer.valueOf(temperature,16)-30;
        map.put("name","temperature");
        map.put("value",temVal.toString());
        return map;
    }
    private JSONObject queryHumidity(String deviceState) throws JSONException {
        JSONObject map = new JSONObject();
        String humidity = deviceState.substring(6,8);//湿度
        Integer humVal = Integer.valueOf(humidity,16);
        map.put("name","humidity");
        map.put("value",humVal.toString());
        return map;
    }

    private JSONObject queryDoorLockOnOff(String deviceState) throws JSONException {
        JSONObject map = new JSONObject();
        String onoff = deviceState.substring(4,6);
        if(!onoff.equals("07")&&!onoff.equals("08")){
            map.put("name","powerstate");
            map.put("value","off");
        }else if(onoff.equals("07")||onoff.equals("08")){
            map.put("name","powerstate");
            map.put("value","on");
        }
        return map;
    }

    private Map<String,Object> irCompositeCommand(TMallTemplate tMallTemplate, List<TYaokonyunKeyCode> yaokonyunKeyCodeList,Map<String,Object> playloadMap,Map<String,Object> header){
        Map<String,Object> returnMap = new HashMap<String, Object>();
        String name = (String)header.get("name");
        String deviceType = (String)playloadMap.get("deviceType");
        String value = (String)playloadMap.get("value");
        String redisKey = (String)playloadMap.get("redisKey");
        if(redisKey==null||redisKey.equals("")){
            redisKey = "a_s2__u0_l0_p0";//空调默认值
        }
        String keyValue = "";
        TmallIRFUNEnum[] enums = TmallIRFUNEnum.values();
        for(int i=0;i<enums.length;i++){
            TmallIRFUNEnum e = enums[i];
            if(e.getName().equals(name)){
                if(name.equals(TmallIRFUNEnum.SetMode.getName())){//空调模式
                    String[] valArr = e.getValue().split(",");
                    for(String key: valArr){
                        if(key.equals(value)){
                            AirconditionEnum ae = AirconditionEnum.getRegion(value);
                            String[] redisArr = redisKey.split("_");
                            redisArr[0]=ae.getValue();
                            for(int k=0;k<redisArr.length;k++){
                                if(k==2&&ae.getName().equals("auto")){
                                    keyValue += "_";
                                }else{
                                    if(redisArr[k].equals("")){
                                        keyValue += "26_";
                                    }else {
                                        keyValue += redisArr[k]+"_";
                                    }
                                }
                            }
                            keyValue = keyValue.substring(0,keyValue.length()-1);
                            break;
                        }
                    }

                }else if(name.equals(TmallIRFUNEnum.SetWindSpeed.getName())){//空调风量
                    String[] valArr = e.getValue().split(",");
                    for(String val: valArr){
                        String[] enumValArr = val.split("\\|");
                        if(enumValArr[0].equals(value)){
                            String begin = redisKey.substring(0,2);
                            String end  = redisKey.substring(4,redisKey.length());
                            keyValue = begin + enumValArr[1]+end;
                        }
                    }
                }else if(name.equals(TmallIRFUNEnum.SetTemperature.getName())){//空调温度
                    String model = redisKey.substring(0,1);
                    if(!model.equals("a")&&!model.equals("d")){
                        String begin = redisKey.substring(0,5);
                        String end =  redisKey.substring(7,redisKey.length());
                        keyValue = begin +value + end;
                    }
                }else if(name.equals(TmallIRFUNEnum.OpenUpAndDownSwing.getName())){//空调上下扫风
                    String model = redisKey.substring(0,1);
                    if(model.equals("a")||model.equals("d")){
                        String begin = redisKey.substring(0,6);
                        String end =  redisKey.substring(8,redisKey.length());
                        keyValue = begin +"u1" + end;
                    }else{
                        String begin = redisKey.substring(0,8);
                        String end =  redisKey.substring(10,redisKey.length());
                        keyValue = begin +"u1" + end;
                    }
                }else if(name.equals(TmallIRFUNEnum.OpenLeftAndRightSwing.getName())){//空调左右扫风
                    String model = redisKey.substring(0,1);
                    if(model.equals("a")||model.equals("d")){
                        String begin = redisKey.substring(0,9);
                        String end =  redisKey.substring(11,redisKey.length());
                        keyValue = begin +"l1" + end;
                    }else{
                        String begin = redisKey.substring(0,11);
                        String end =  redisKey.substring(13,redisKey.length());
                        keyValue = begin +"l1" + end;
                    }
                }else{
                    keyValue = e.getValue();
                }
            }
        }

        returnMap.put("keyValue",keyValue);
        return  returnMap;
    }

    private Map<String,Object> compositeCommand(TMallTemplate tMallTemplate, TOboxDeviceConfig oboxDeviceConfig,Map<String,Object> playloadMap,Map<String,Object> header){
        String name = (String)header.get("name");
        String value = String.valueOf(playloadMap.get("value"));
        String deviceId = (String)playloadMap.get("deviceId");
        String deviceType = (String)playloadMap.get("deviceType");
        String lightProperties = tMallTemplate.getLightActions();
        String dfControllProperties = tMallTemplate.getDefaultAction();
        String lightControllProperties = tMallTemplate.getLightControllProperties();
        String tvProperties = tMallTemplate.getTvProperties();
        String airconditionProperties = tMallTemplate.getAirconditionProperties();
        String deviceState = oboxDeviceConfig.getDeviceState();
        String obdeviceType = oboxDeviceConfig.getDeviceType();
        String obChildType = oboxDeviceConfig.getDeviceChildType();
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
        String[] tvPropertiesArrays = tvProperties.split("\\|");
        for(String tvProperty : tvPropertiesArrays){
            propertiesLists.add(tvProperty);
        }
        String[] airConditionArrays = airconditionProperties.split("\\|");
        for(String airConditionProperty : airConditionArrays){
            propertiesLists.add(airConditionProperty);
        }

        for (String properties : propertiesLists){
            String[] propertyArr =  properties.split("-");
            if(deviceType.equals("light")){
                if(("light_"+name).equals(propertyArr[0])){
                    if(obdeviceType.equals("16")){//遥控灯
                        String[] ids = deviceId.split("_");
                        String idxex = ids[1];
                        Integer add = Integer.parseInt(idxex);
                        add = 0x01<<(add-1);
                        String addr = Integer.toHexString(add);
                        System.out.println("addr ================== "+addr);
                        if(addr.length()<2){
                            addr = "0"+addr;
                        }
                        if(name.equals("TurnOn")||name.equals("TurnOff")){
                            value = propertyArr[1];
                            deviceState = changeRemoteOnOffState(name,addr);
                        }else if(name.equals("SetBrightness")){
                            if(value.equals("max")){
                                value = "64";
                            }else if(value.equals("min")){
                                value = "01";
                            }else{
                                value =ByteHelper.int2HexString(Integer.valueOf(value));
                            }
                            deviceState = "03fd"+addr+"00"+value+"ff0001";
                        }else if(name.equals("SetColor")){
                            String colorname = ColorEnum.getRegion(value).getName();
                            value = ColorEnum.getRegion(value).getValue();
                            if(colorname.equals("Yellow")){
                                deviceState = "03fe"+addr+"00"+value+"01";
                                deviceState += "-03fd"+addr+"00ff000001";
                            }else if(colorname.equals("White")){
                                deviceState = "03fe"+addr+"00"+value+"01";
                                deviceState += "-03fd"+addr+"00ff640001";
                            }else{
                                deviceState = "03fe"+addr+"00"+value+"01";
                            }
                        }else if(name.equals("SetColorTemperature")){
                            if(value.equals("min")){
                                String middle = ByteHelper.int2HexString(0);
                                deviceState = "03fd"+addr+"00ff"+middle+"0001";
                            }else if(value.equals("max")){
                                String middle = ByteHelper.int2HexString(100);
                                deviceState = "03fd"+addr+"00ff"+middle+"0001";
                            }else{
                                Integer v = Integer.valueOf(value);
                                if(v>=0&&v<=100){//暖光
                                    String middle = ByteHelper.int2HexString(v);
                                    deviceState = "03fd"+addr+"00ff"+middle+"0001";
                                }else if(v<2700||v>6500){
                                    deviceState = null;
                                }else{
                                    String t = value.substring(0,2);
                                    Integer temperature = Integer.valueOf(t)-27;
                                    temperature = temperature*4;
                                    String middle = ByteHelper.int2HexString(temperature);
                                    deviceState = "03fd"+addr+"00ff"+middle+"0001";
                                }
                            }
                        }else if(name.equals("SetMode")){
                            if(value.equals("夜灯")||value.equals("nightLight")){
                                deviceState = "0208"+addr+"00";
                            }else if(value.indexOf("自然")>=0){
                                String middle = ByteHelper.int2HexString(50);
                                deviceState = "03fd"+addr+"00ff"+middle+"0001";
                            }
                        }
                    }else{//一般灯
                        if(name.equals("TurnOn")||name.equals("TurnOff")){
                            value = propertyArr[1];
                            deviceState = changeState(deviceState,value);
                        }
                        if(name.equals("AdjustUpBrightness")){
                            String step = deviceState.substring(0,2);
                            Integer middle = Integer.valueOf(step,16);
                            middle = middle+10;
                            if(middle>=254){
                                value =  "fe";
                            }else{
                                value =  ByteHelper.int2HexString(middle);
                            }
                            deviceState = changeState(deviceState,value);
                        }if(name.equals("AdjustDownBrightness")){
                            String step = deviceState.substring(0,2);
                            Integer middle = Integer.valueOf(step,16);
                            middle = middle - 10;
                            if(middle<=154){
                                value =  "9a";
                            }else{
                                value =  ByteHelper.int2HexString(middle);
                            }
                            deviceState = changeState(deviceState,value);
                        }else if(name.equals("SetBrightness")){
                            if(value.equals("max")){
                                value = "fe";
                            }else if(value.equals("min")){
                                value = "9a";
                            }else{
                                Integer val = Integer.valueOf(value);
                                val = val+154;
                                if(val<=154){
                                    val = 154;
                                }
                                if(val>=254){
                                    val = 154;
                                }
                                value =  ByteHelper.int2HexString(val);
                            }
                            deviceState = changeState(deviceState,value);
                        }else if(name.equals("SetColor")){
                            value = ColorEnum.getRegion(value).getValue();
                            deviceState = changeColorState(deviceState,value);
                        }else if(name.equals("SetColorTemperature")){
                            Integer v = Integer.valueOf(value);
                            if(v>=0&&v<=100){//暖光到冷光
                                v = v*25;
                                String middle = ByteHelper.int2HexString(v);
                                deviceState = changeColorTemperature(deviceState,middle);
                            }else if(v<2700||v>6500){
                                deviceState = null;
                            }else{
                                String t = value.substring(0,2);
                                Integer temperature = Integer.valueOf(t)-27;
                                temperature = temperature*6;
                                String middle = ByteHelper.int2HexString(temperature);
                                deviceState = changeColorTemperature(deviceState,middle);
                            }
                        }else if(name.equals("AdjustUpColorTemperature")){
                            String val = deviceState.substring(2,4);
                            Integer middle = Integer.valueOf(val,16);
                            middle = middle+10;
                            deviceState = changeColorTemperature(deviceState,ByteHelper.int2HexString(middle));
                        }else if(name.equals("AdjustDownColorTemperature")){
                            String val = deviceState.substring(2,4);
                            Integer middle = Integer.valueOf(val,16);
                            middle = middle-10;
                            deviceState = changeColorTemperature(deviceState,ByteHelper.int2HexString(middle));
                        }
                    }

                }
            }else if((name).equals(propertyArr[0])&&obdeviceType.equals("04")
                    &&(obChildType.equals("2b")||obChildType.equals("53")||obChildType.equals("2a")||obChildType.equals("51"))){//2键及以上的开关
                deviceState = changeMutipleOutLet(deviceState,value,deviceId,"24");
            }else if((name).equals(propertyArr[0])&&obdeviceType.equals("04")&&(obChildType.equals("17")
                    ||obChildType.equals("16"))){
                deviceState = changeMutipleOutLet(deviceState,value,deviceId,"12");
            }else if((name).equals(propertyArr[0])&&obdeviceType.equals("05")){//开闭合设备
                if(value.equals("off")){
                    value = "00";
                }else if(value.equals("on")){
                    value = "02";
                }
                deviceState = changeState(deviceState,value);
            }else if((name).equals(propertyArr[0])){
                if(value.equals("off")){
                    value = "00";
                }else if(value.equals("on")){
                    value = "01";
                }
                deviceState = changeState(deviceState,value);
            }

        }
        returnMap.put("deviceId",deviceId);
        returnMap.put("deviceState",deviceState);
        return returnMap;
    }

    private String changeMutipleOutLet(String deviceState ,String value,String deviceId,String partition){
        String[] deviceIdArr = deviceId.split("_");
        String middle = "";
        String child = "";
        String val = null;

        if(deviceIdArr.length>1){
            child = deviceIdArr[1];
        }else{
            return null;
        }
        String reVal = "";
        String beginStr = null;
        String endStr = null;
        if(partition.equals("24")){

            middle = deviceState.substring(2,4);
            beginStr = deviceState.substring(0,2);
            endStr = deviceState.substring(4,deviceState.length());
            if(value.equals("off")){
                val = andOpt(middle,child);
            }else if(value.equals("on")){
                val = orOpt(middle,child);
            }
            reVal = beginStr+val+endStr;
        }else if(partition.equals("12")){
            beginStr = deviceState.substring(0,2);
            endStr = deviceState.substring(2,deviceState.length());
            if(value.equals("off")){
                val = andOpt(beginStr,child);
            }else if(value.equals("on")){
                val = orOpt(beginStr,child);
            }
            reVal = val+endStr;
        }

        return reVal;
    }

    private String andOpt(String val,String child){
        String andM = "";
        if(child.equals("1")){
            andM = "FC";
        }else if(child.equals("2")){
            andM = "F3";
        }else if(child.equals("3")){
            andM = "CF";
        }
        return andM;
    }

    private String orOpt(String val,String child){
        String andM = "";
        if(child.equals("1")){
            andM = "FE";
        }else if(child.equals("2")){
            andM = "F7";
        }else if(child.equals("3")){
            andM = "DF";
        }
        return andM;
    }

    private String changeState(String deviceState,String value){
        String endStr = deviceState.substring(2,deviceState.length());
        return value+endStr;
    }

    private String changeRemoteOnOffState(String name,String position){
        if(name.equals("TurnOn")){
            return "0301"+position+"00";
        }else{
            return "0302"+position+"00";
        }
    }

    private String changeColorTemperature(String deviceState,String value){
        String beginStr = deviceState.substring(0,2);
        String endStr = deviceState.substring(4,deviceState.length());
        return beginStr+value+endStr;
    }

    private String changeColorState(String deviceState,String value){
        String beginStr = deviceState.substring(0,6);
        String endStr = deviceState.substring(12,deviceState.length());
        return beginStr+value+endStr;
    }

    private String changeRemoteLightColorState(String deviceState,String value){
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
        return compositeCommand(tMallTemplate,oboxDeviceConfig,playloadMap,header);
    }

    public Map<String,Object> TMall2IR(){
        return  irCompositeCommand(tMallTemplate,yaokonyunKeyCodeList,playloadMap,header);
    }

    public TMallDeviceAdapter queryDevice() throws JSONException {
        Map<String,Boolean> isQuery = new HashMap<String, Boolean>();
        isQuery.put("Query",false);
        isQuery.put("QueryBrightness",false);
        isQuery.put("QueryColorTemperature",false);
        return query(tMallTemplate,oboxDeviceConfig,isQuery);
    }

    @Override
    public String toString(){
        return "deviceId:"+getDeviceId()+"\n deviceName:"+getDeviceName()+"\n properties:"+getProperties()+
               "\n zone:"+getZone()+"\n model:"+getModel()+"\n brand:"+getBrand()+
                "\n icon:"+getIcon();
    }
}
