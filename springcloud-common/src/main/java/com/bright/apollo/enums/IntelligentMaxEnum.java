package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月8日  
 *@Version:1.1.0  
 */
public enum IntelligentMaxEnum {

	MIN(0),
	MAX(1);
 
	
	private Integer value;
	private IntelligentMaxEnum(Integer value) {
 
		this.value = value;
 
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}

	 
 

}
