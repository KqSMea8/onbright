package com.bright.apollo.common.entity;

import java.util.Date;

public class TAliDevice {

    public TAliDevice(){}

    private Integer Id;

    private String productKey;

    private String deviceName;

    private String deviceSecret;

    private String oboxSerialId;

    private Integer offline;

    private Date lastOpTime;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSecret() {
        return deviceSecret;
    }

    public void setDeviceSecret(String deviceSecret) {
        this.deviceSecret = deviceSecret;
    }

    public String getOboxSerialId() {
        return oboxSerialId;
    }

    public void setOboxSerialId(String oboxSerialId) {
        this.oboxSerialId = oboxSerialId;
    }

    public Integer getOffline() {
        return offline;
    }

    public void setOffline(Integer offline) {
        this.offline = offline;
    }

    public Date getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }
}
