package com.bright.apollo.common.rokid;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RokidSwitchDate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Expose
    private RokidSwitchState state;
    @Expose
    private String type;
    @Expose
    private String deviceId;
    @Expose
    private String name;
    @Expose
    private RokidSwitchAction actions;
    public RokidSwitchState getState() {
        return state;
    }
    public void setState(RokidSwitchState state) {
        this.state = state;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public RokidSwitchAction getActions() {
        return actions;
    }
    public void setActions(RokidSwitchAction actions) {
        this.actions = actions;
    }
    @Override
    public String toString() {
        return "{state=" + state + ", type=" + type + ", deviceId=" + deviceId + ", name=" + name
                + ", actions=" + actions + "}";
    }
}
