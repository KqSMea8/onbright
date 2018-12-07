package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum TmallIRFUNEnum {

	AdjustUpVolume("AdjustUpVolume","vol+"),
	AdjustDownVolume("AdjustDownVolume","vol-"),
	AdjustUpChannel("AdjustUpChannel","ch+"),
	AdjustDownChannel("AdjustDownChannel","ch-"),
	SetMode("SetMode","auto,cold,dehumidification,airsupply,heat"),
	SetWindSpeed("SetWindSpeed","1|s0,2|s1,3|s2,4|s3"),
	SetTemperature("SetTemperature","26"),
	OpenUpAndDownSwing("OpenUpAndDownSwing","1"),
	OpenLeftAndRightSwing("OpenLeftAndRightSwing","1");

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	TmallIRFUNEnum(String name,String v) {
		this.name = name;
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static TmallIRFUNEnum getRegion(String name) {
		for (TmallIRFUNEnum typeEnum : TmallIRFUNEnum.values()) {
			if (typeEnum.name().equals(name)) {
				return typeEnum;
			}
		}
		return null;
	}

}