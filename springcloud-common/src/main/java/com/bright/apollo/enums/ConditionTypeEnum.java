package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月27日  
 *@Version:1.1.0  
 */
public enum ConditionTypeEnum {

	//时间／设备／遥控器/指纹机  00/01/02/03
	time("00"),
	
	device("01"),
	
	remoter("02"),
	
	fingerprint("03"),
	
	quartz("04")//定时扫描
	;
	
	private String value;
	
	private ConditionTypeEnum(String v) {
		// TODO Auto-generated constructor stub
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	
	}

}
