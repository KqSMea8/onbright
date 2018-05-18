package com.bright.apollo.enums;

public enum SystemEnum {
    system(5,"系统消息”"),//系统消息
    scene(0,"场景推送消息");//系统消息子类型

    private int value;

    private String content;

    private SystemEnum(int v,String content) {
        this.value = v;
        this.content=content;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
