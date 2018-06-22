package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum TransitionChildrenEnum {

	singleColor("单色灯","01"),

	warmCool("冷暖色灯","02"),

	colorful("三色灯","03"),

	singleswitch("一路开关智能插座","01"),

	curtain("窗帘","01");


	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	TransitionChildrenEnum(String name, String v) {
		this.name = name;
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static TransitionChildrenEnum getRegion(String name) {
		for (TransitionChildrenEnum typeEnum : TransitionChildrenEnum.values()) {
			if (typeEnum.name().equals(name)) {
				return typeEnum;
			}
		}
		return null;
	}

}