package com.bright.apollo.bean;

public class PushExceptionMsg {
    private Integer type;//异常类型
    private Integer childType;//异常类型
    private Integer id;//设备Id  或者场景id
    private String state;//设备异常状态
    private String url;//图片url
    private Integer userId;
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public Integer getChildType() {
        return childType;
    }
    public void setChildType(Integer childType) {
        this.childType = childType;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public PushExceptionMsg() {
        super();
    }
    public PushExceptionMsg(Integer type, Integer childType, Integer id, String state) {
        super();
        this.type = type;
        this.childType = childType;
        this.id = id;
        this.state = state;
    }
    public PushExceptionMsg(Integer type, Integer childType, Integer id,Integer userId ,String state,String url) {
        super();
        this.type = type;
        this.childType = childType;
        this.id = id;
        this.state = state;
        this.url=url;
        this.userId=userId;
    }
}
