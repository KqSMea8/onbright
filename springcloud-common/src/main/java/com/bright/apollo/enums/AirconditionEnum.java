package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum AirconditionEnum {

	Auto("auto","a"),
	Cold("cold","r"),
	Dehumidification("dehumidification","d"),
	Airsupply("airsupply","w"),
	Heat("heat","h");

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	AirconditionEnum(String name, String v) {
		this.name = name;
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static AirconditionEnum getRegion(String name) {
		for (AirconditionEnum typeEnum : AirconditionEnum.values()) {
			if (typeEnum.getName().equals(name)) {
				return typeEnum;
			}
		}
		return null;
	}

}