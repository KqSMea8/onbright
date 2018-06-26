package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum ColorEnum {

	Red("Red","ff0000"),
	Yellow("Yellow","ffff00"),
	Blue("Blue","0000ff"),
	Green("Green","00ff00"),
	White("White","ffffff"),
	Black("Black","000000"),
	Cyan("Cyan","00ffff"),
	Purple("Purple","9900ff"),
	Orange("Orange","ff9900");

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	ColorEnum(String name, String v) {
		this.name = name;
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static ColorEnum getRegion(String name) {
		for (ColorEnum typeEnum : ColorEnum.values()) {
			if (typeEnum.name().equals(name)) {
				return typeEnum;
			}
		}
		return null;
	}

}