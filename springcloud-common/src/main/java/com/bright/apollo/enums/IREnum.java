package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum IREnum {

	Auto("a","11"),//自动
	Refrigeration ("r","21"),//制冷
	Dry("d","31"),//抽湿
	Wind("w","41"),//送风
	Heat("h","51"),//制热
	On("on","01"),//开机
	Off("off","00"),//关机
	None("_","ff"),//无效
	SpeedAutoV1("a","00"),//v1风量自动
	SpeedAutoV3("s0","00"),//v3风量自动
	SpeedS1("s1","01"),//1档
	SpeedS2("s2","02"),//2档
	SpeedS3("s3","03"),//3档
	UpSwingOpen("u1","01"),//上下摆风开启
	UpSwingOff("u0","00"),//上下摆风关闭
	LeftSwingOn("l1","01"),//左右摆风开启
	LeftSwingOff("l0","00");//左右摆风关闭

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	IREnum(String name, String v) {
		this.name = name;
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static IREnum getRegion(String name) {
		for (IREnum typeEnum : IREnum.values()) {
			if (typeEnum.name().equals(name)) {
				return typeEnum;
			}
		}
		return null;
	}

}