package com.bright.apollo.common.rokid;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RokidLedDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Expose
    private String type;
    @Expose
    private String deviceId;
    @Expose
    private String name;
    @Expose
    private RokidLedAction actions;
    @Expose
    private RokidLedState state;
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
    public RokidLedAction getActions() {
        return actions;
    }
    public void setActions(RokidLedAction actions) {
        this.actions = actions;
    }
    public RokidLedState getState() {
        return state;
    }
    public void setState(RokidLedState state) {
        this.state = state;
    }
    @Override
    public String toString() {
        return "{type=" + type + ", deviceId=" + deviceId + ", name=" + name + ", actions=" + actions
                + ", state=" + state + "}";
    }
}
