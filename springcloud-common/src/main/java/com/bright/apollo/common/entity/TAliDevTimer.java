package com.bright.apollo.common.entity;

import java.util.Date;

public class TAliDevTimer {
    private Integer Id;

    private String deviceSerialId;

    private String timer;

    private String timerValue;

    private Integer isCountdown;

    private Integer state;

    private Date lastOpTime;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getDeviceSerialId() {
        return deviceSerialId;
    }

    public void setDeviceSerialId(String deviceSerialId) {
        this.deviceSerialId = deviceSerialId;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTimerValue() {
        return timerValue;
    }

    public void setTimerValue(String timerValue) {
        this.timerValue = timerValue;
    }

    public Integer getIsCountdown() {
        return isCountdown;
    }

    public void setIsCountdown(Integer isCountdown) {
        this.isCountdown = isCountdown;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(Date lastOpTime) {
        this.lastOpTime = lastOpTime;
    }
}
