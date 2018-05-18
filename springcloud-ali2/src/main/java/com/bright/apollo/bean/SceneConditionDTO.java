package com.bright.apollo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SceneConditionDTO {
    @Expose
    @SerializedName("condition")
    private java.lang.String condition;//

    @Expose
    @SerializedName("condition_type")
    private java.lang.String conditionType;//

    @Expose
    @SerializedName("conditionID")
    private java.lang.String conditionID;//

    @Expose
    @SerializedName("serialId")
    private java.lang.String deviceSerialId;//remark:;length:10; not null,default:null

    @Expose
    @SerializedName("obox_serial_id")
    private java.lang.String oboxSerialId;//remark:;length:128; not null,default:null

    @Expose
    @SerializedName("addr")
    private java.lang.String deviceRfAddr;//remark:;length:128; not null,default:null


    @Expose
    @SerializedName("device_type")
    private java.lang.String deviceType;//remark:;length:128; not null,default:null

    @Expose
    @SerializedName("device_child_type")
    private java.lang.String deviceChildType;//remark:;length:128; not null,default:null

    @SerializedName("oboxs")
    private List<String> oboxs;
    //针对定时任务
    @SerializedName("startTime")
    private Long startTime;
    @SerializedName("count")
    private int count;
    @SerializedName("interval")
    private int interval;
    public java.lang.String getCondition() {
        return condition;
    }

    public void setCondition(java.lang.String condition) {
        this.condition = condition;
    }

    public java.lang.String getConditionID() {
        return conditionID;
    }

    public void setConditionID(java.lang.String conditionID) {
        this.conditionID = conditionID;
    }

    public java.lang.String getDeviceRfAddr() {
        return deviceRfAddr;
    }

    public void setDeviceRfAddr(java.lang.String deviceRfAddr) {
        this.deviceRfAddr = deviceRfAddr;
    }

    public java.lang.String getDeviceSerialId() {
        return deviceSerialId;
    }

    public void setDeviceSerialId(java.lang.String deviceSerialId) {
        this.deviceSerialId = deviceSerialId;
    }

    public java.lang.String getOboxSerialId() {
        return oboxSerialId;
    }

    public void setOboxSerialId(java.lang.String oboxSerialId) {
        this.oboxSerialId = oboxSerialId;
    }

    public java.lang.String getDeviceChildType() {
        return deviceChildType;
    }

    public void setDeviceChildType(java.lang.String deviceChildType) {
        this.deviceChildType = deviceChildType;
    }

    public java.lang.String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(java.lang.String deviceType) {
        this.deviceType = deviceType;
    }

    public List<String> getOboxs() {
        return oboxs;
    }

    public void setOboxs(List<String> oboxs) {
        this.oboxs = oboxs;
    }

    public java.lang.String getConditionType() {
        return conditionType;
    }

    public void setConditionType(java.lang.String conditionType) {
        this.conditionType = conditionType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }


    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SceneConditionDTO [condition=" + condition + ", conditionType="
                + conditionType + ", conditionID=" + conditionID
                + ", deviceSerialId=" + deviceSerialId + ", oboxSerialId="
                + oboxSerialId + ", deviceRfAddr=" + deviceRfAddr
                + ", deviceType=" + deviceType + ", deviceChildType="
                + deviceChildType + ", oboxs=" + oboxs + ", startTime="
                + startTime + ", count=" + count + ", interval=" + interval
                + "]";
    }
}
