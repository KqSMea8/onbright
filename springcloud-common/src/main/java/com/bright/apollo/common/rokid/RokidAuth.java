package com.bright.apollo.common.rokid;

import java.io.Serializable;

public class RokidAuth implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;//userName
    private String userToken;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserToken() {
        return userToken;
    }
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    @Override
    public String toString() {
        return "{userId=" + userId + ", userToken=" + userToken + "}";
    }
}
