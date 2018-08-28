package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月28日  
 *@Version:1.1.0  
 */
public enum EnvironmentalSensorEnum {

	TVOC("0"),
	PM("1"),//PM2.5
	CO("2"),
	TEMPERATURE("3"),
	HUMIDITY("4"),
	CO2("5");
	
	private String value;
	private EnvironmentalSensorEnum(String value){
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	


}
