package com.bright.apollo.common.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public class QueryRemoteBySrcDTO2 implements Serializable {
//    @Expose
//    @SerializedName("rc_command")
//    @JsonProperty(value="rc_command")
//    private HashMap<String,KeyCode> rcCommand;
    private static final long serialVersionUID = -2l;

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

    @Expose
    @SerializedName("brandType")
    @JsonProperty(value="brandType")
    private Integer brandType;//遥控器品牌Id

    public Integer getBrandType() {
        return brandType;
    }

    public void setBrandType(Integer brandType) {
        this.brandType = brandType;
    }

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
    private Integer type;//类型
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

    public QueryRemoteBySrcDTO2() {
        super();
    }


    public QueryRemoteBySrcDTO2(MatchRemoteControl matchRemoteControl) {
        super();
        this.rid=matchRemoteControl.getRid();
        this.name=matchRemoteControl.getName();
        this.type=matchRemoteControl.gettId();
        this.version=matchRemoteControl.getVersion().toString();
        this.rmodel=matchRemoteControl.getRmodel();
        Map<String, KeyCode> keyCodeMap =matchRemoteControl.getRcCommand();
        JSONArray keyArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("keyCodeMap",keyCodeMap);
        keyArray.add(jsonObject);
        this.keys = keyArray;
        this.extendsKeys = new JSONArray();
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
