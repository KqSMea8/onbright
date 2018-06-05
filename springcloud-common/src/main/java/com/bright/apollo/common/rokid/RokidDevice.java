package com.bright.apollo.common.rokid;

import java.io.Serializable;

public class RokidDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private String deviceId;//device serialId
    private String name;//device name
    private RokidRequestAction actions;
    private RokidSwitchState state;
    private RokidAuth userAuth;
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
    public RokidRequestAction getActions() {
        return actions;
    }
    public void setActions(RokidRequestAction actions) {
        this.actions = actions;
    }
    public RokidSwitchState getState() {
        return state;
    }
    public void setState(RokidSwitchState state) {
        this.state = state;
    }
    public RokidAuth getUserAuth() {
        return userAuth;
    }
    public void setUserAuth(RokidAuth userAuth) {
        this.userAuth = userAuth;
    }
    @Override
    public String toString() {
        return "{type=" + type + ", deviceId=" + deviceId + ", name=" + name + ", actions=" + actions
                + ", state=" + state + ", userAuth=" + userAuth + "}";
    }
}

