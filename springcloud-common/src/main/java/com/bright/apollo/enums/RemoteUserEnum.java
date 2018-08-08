package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月8日  
 *@Version:1.1.0  
 */
public enum RemoteUserEnum {

 
	
	add("01"),
	
	del("02"),
	
	update("03"),
	
	delAll("04");
		
	
	private String value;
	
 	
	private RemoteUserEnum() {
		
	}
	
	private RemoteUserEnum(String value ) {
		this.value = value;
 	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	 



}
