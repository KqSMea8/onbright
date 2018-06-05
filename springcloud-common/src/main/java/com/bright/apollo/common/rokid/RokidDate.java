package com.bright.apollo.common.rokid;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RokidDate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Expose
    private String type;
    @Expose
    private String deviceId;
    @Expose
    private String name;
    @Expose
    private RokidAction actions;
    @Expose
    private RokidState state;
    @Expose
    private boolean offline;
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
   public RokidAction getActions() {
        return actions;
    }
    public void setActions(RokidAction actions) {
        this.actions = actions;
    }
    public RokidState getState() {
        return state;
    }
    public void setState(RokidState state) {
        this.state = state;
    }
    public boolean isOffline() {
        return offline;
    }
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
    @Override
    public String toString() {
        return "{type=" + type + ", deviceId=" + deviceId + ", name=" + name
                + ", offline=" + offline + "}";
    }
}
