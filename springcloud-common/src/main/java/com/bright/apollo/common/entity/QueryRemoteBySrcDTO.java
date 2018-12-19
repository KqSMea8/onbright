package com.bright.apollo.common.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QueryRemoteBySrcDTO implements Serializable {
//    @Expose
//    @SerializedName("rc_command")
//    @JsonProperty(value="rc_command")
//    private HashMap<String,KeyCode> rcCommand;
    private static final long serialVersionUID = -1l;

    public JSONArray getKeys() {
        return keys;
    }

    public void setKeys(JSONArray keys) {
        this.keys = keys;
    }

    @Expose
    @SerializedName("keys")
    @JsonProperty(value="keys")
    private JSONArray keys;

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    @Expose
    @SerializedName("brandId")
    @JsonProperty(value="brandId")
    private Integer brandId;//遥控器品牌Id

    public JSONArray getExtendsKeys() {
        return extendsKeys;
    }

    public void setExtendsKeys(JSONArray extendsKeys) {
        this.extendsKeys = extendsKeys;
    }

    @Expose
    @SerializedName("extendsKeys")
    @JsonProperty(value="extendsKeys")
    private JSONArray extendsKeys;

    @Expose
    @SerializedName("rid")
    @JsonProperty(value="rid")
    private String rid;//遥控id
    @Expose
    @SerializedName("name")
    @JsonProperty(value="name")
    private String name;//名字
    @Expose
    @SerializedName("deviceType")
    @JsonProperty(value="deviceType")
    private Integer deviceType;//类型
    @Expose
    @SerializedName("version")
    @JsonProperty(value="version")
    private String version;//版本
    @Expose
    @SerializedName("rmodel")
    @JsonProperty(value="rmodel")
    private String rmodel;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Expose
    @SerializedName("index")
    @JsonProperty(value="index")
    private Integer index;

    public QueryRemoteBySrcDTO() {
        super();
    }
    public QueryRemoteBySrcDTO(MatchRemoteControl matchRemoteControl) {
        super();
        this.rid=matchRemoteControl.getRid();
        this.name=matchRemoteControl.getName();
        this.deviceType=matchRemoteControl.gettId();
        this.version=matchRemoteControl.getVersion().toString();
        this.rmodel=matchRemoteControl.getRmodel();
        Map<String, KeyCode> keyCodeMap =matchRemoteControl.getRcCommand();
        Iterator<String> iterator = keyCodeMap.keySet().iterator();
        String key = null;
        while (iterator.hasNext()){
            key = iterator.next();
        }
        JSONArray keyArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key",key);
        keyArray.add(jsonObject);
        this.keys = keyArray;
        this.extendsKeys = new JSONArray();
    }

    public QueryRemoteBySrcDTO(Map<String,Object> map) {
        super();
        this.rid="";
        this.version=((Integer) map.get("version")).toString();
        this.rmodel=(String)map.get("rmodel");
        this.name=(String)map.get("name");
//        String key = yaokonyunKeyCode.getKeyName();
//        String extendsKey = yaokonyunKeyCode.getCustomName();
//        JSONArray keyArray = new JSONArray();
//        JSONArray extendsKeyArray = new JSONArray();
//        if(key!=null && !key.equals("")){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("key",key);
//            keyArray.add(jsonObject);
//        }
//        if(extendsKey !=null&&!extendsKey.equals("")){
//            JSONObject extendsJsonObject = new JSONObject();
//            extendsJsonObject.put("key",extendsKey);
//            extendsKeyArray.add(extendsJsonObject);
//        }
        this.keys = (JSONArray) map.get("keys");
        this.extendsKeys = (JSONArray) map.get("extendsKeys");
        this.index = (Integer) map.get("index");
        this.deviceType = (Integer) map.get("type");
        this.brandId = (Integer) map.get("brandType");
    }


    public String getRmodel() {
        return rmodel;
    }
    public void setRmodel(String rmodel) {
        this.rmodel = rmodel;
    }

//    public HashMap<String, KeyCode> getRcCommand() {
//        return rcCommand;
//    }
//    public void setRcCommand(HashMap<String, KeyCode> rcCommand) {
//        this.rcCommand = rcCommand;
//    }
    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
