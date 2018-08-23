package com.bright.apollo.common.entity;

public class TYaokonyunDevice {

    private Integer id;

    private Integer useTime;

    private String appId;

    private String deviceId;

    private String lastOpTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(String lastOpTime) {
        this.lastOpTime = lastOpTime;
    }
}
