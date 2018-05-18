package com.bright.apollo.bean;

public class PushSystemMsg {
    private Integer type;//异常类型
    private Integer childType;//异常类型
    private Integer id;//设备Id  或者场景id
    private Integer userId;
    private String content;//发送内容
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getChildType() {
        return childType;
    }
    public void setChildType(Integer childType) {
        this.childType = childType;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public PushSystemMsg(Integer type, Integer childType, Integer id,
                         Integer userId, String content) {
        super();
        this.type = type;
        this.childType = childType;
        this.id = id;
        this.userId = userId;
        this.content = content;
    }
}
