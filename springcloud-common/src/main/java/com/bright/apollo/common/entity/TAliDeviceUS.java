package com.bright.apollo.common.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TAliDeviceUS {

    private Integer Id;

    private String productKey;

    private String deviceName;

    private String deviceSecret;

    private String deviceSerialId;

    private Integer offline;

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

    public String getDeviceSerialId() {
        return deviceSerialId;
    }

    public void setDeviceSerialId(String deviceSerialId) {
        this.deviceSerialId = deviceSerialId;
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

    private Date lastOpTime;
}
