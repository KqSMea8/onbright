package com.bright.apollo.common.rokid;

import java.io.Serializable;

public class RokidRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private RokidAuth userAuth;
    private RokidExeAction action;
    private RokidDevice device;

    public RokidAuth getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(RokidAuth userAuth) {
        this.userAuth = userAuth;
    }

    public RokidDevice getDevice() {
        return device;
    }

    public void setDevice(RokidDevice device) {
        this.device = device;
    }

    public RokidExeAction getAction() {
        return action;
    }

    public void setAction(RokidExeAction action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "{userAuth=" + userAuth + ", action=" + action + ", device=" + device + "}";
    }
}
