package com.bright.apollo.bean;

import java.io.Serializable;

public class PushMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte type;
    private String token;
    private String cmd;
    private String serialId;//obox serialId
    private String data;
    private boolean onLine;//false offline  true online
    private String appkey;
    private String request;
    private String system;
    private String irId;
    public String getIrId() {
        return irId;
    }
    public void setIrId(String irId) {
        this.irId = irId;
    }
    public byte getType() {
        return type;
    }
    public void setType(byte type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getSerialId() {
        return serialId;
    }
    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public boolean isOnLine() {
        return onLine;
    }
    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public String getAppkey() {
        return appkey;
    }
    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }

    public String getSystem() {
        return system;
    }
    public void setSystem(String system) {
        this.system = system;
    }
    @Override
    public String toString() {
        return "{type=" + type + ", token=" + token + ", cmd="
                + cmd + ", serialId=" + serialId + ", data=" + data
                + ", onLine=" + onLine + ", appkey=" + appkey + ", request="
                + request + "}";
    }
}
