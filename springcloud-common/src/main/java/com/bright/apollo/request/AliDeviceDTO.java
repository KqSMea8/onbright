package com.bright.apollo.request;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AliDeviceDTO extends TAliDeviceConfig implements Serializable {

    private static final long serialVersionUID = 111111111L;

    public AliDeviceDTO() {
        // TODO Auto-generated constructor stub
    }

    public AliDeviceDTO(TAliDeviceConfig tAliDeviceConfig) {
        // TODO Auto-generated constructor stub
        setDeviceSerialId(tAliDeviceConfig.getDeviceSerialId());
        setType(tAliDeviceConfig.getType());
        setName(tAliDeviceConfig.getName());
        deviceAction = JSONArray.parseArray(tAliDeviceConfig.getAction());
        deviceState = JSONArray.parseArray(tAliDeviceConfig.getState());
    }

    @Expose
    @SerializedName("action")
    @JsonProperty(value="action")
    private JSONArray deviceAction;//

    @Expose
    @SerializedName("state")
    @JsonProperty(value="state")
    private JSONArray deviceState;//

    @Expose
    @SerializedName("online")
    @JsonProperty(value="online")
    private boolean online;

    public JSONArray getDeviceAction() {
        return deviceAction;
    }

    public void setDeviceAction(JSONArray deviceAction) {
        this.deviceAction = deviceAction;
    }

    public JSONArray getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(JSONArray deviceState) {
        this.deviceState = deviceState;
    }

    public void setOnline(Integer online) {
        if (online != 0) {
            this.online = false;
        }else {
            this.online = true;
        }
    }
}
