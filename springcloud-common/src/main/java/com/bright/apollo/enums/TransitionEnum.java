package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum TransitionEnum {

	light("灯类设备","01"),

	switchs("插座开关","04"),

	slider("开合类设备","05");

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	TransitionEnum(String name,String v) {
		this.name = name;
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static TransitionEnum getRegion(String name) {
		for (TransitionEnum typeEnum : TransitionEnum.values()) {
			if (typeEnum.name().equals(name)) {
				return typeEnum;
			}
		}
		return null;
	}

}