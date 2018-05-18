package com.bright.apollo.enums;

public enum ExceptionEnum {
    socket(0,"插座"),//插座
    overcurrent(0,"过流异常"),//过流  插座子类型
    overcapacity(1,"过压异常"),//过载 插座子类型
    undervoltage(2,"欠压异常"),//欠压 插座子类型

    sensor(1,"传感器"),//传感器
    waterleaching(0,"水浸"),//传感器 水浸

    doorlock(2,"门锁"),//门锁
    lowbattery(0,"低电量异常"),//低电量 门锁子类型
    doohickey(1,"异常打开"),//异常开门 门锁子类型

    alldevice(3,"所有设备"),//所有设备
    unconnection(0,"断线异常"),//断线 所有设备子类型
    pic(1,"拍照"),//拍照

    securityscene(4,"安防场景"),//安防场景
    trigger(0,"触发"),//触发 安防场景子类型
    detectionopen(1,"检测忘记设防");//检测是否打开 安防场景子类型

    private int value;

    private String content;

    private ExceptionEnum(int v,String content) {
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
