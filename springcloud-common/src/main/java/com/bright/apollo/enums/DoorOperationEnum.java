package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */
public enum DoorOperationEnum {

	open("00"),
	
	del("01"),
	
	add("02"),
	
	modifyPwd("03"),
	
	modifyFingerprint("04");
	
	private String value;
	
	private DoorOperationEnum (String value){
		this.value=value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

}
