package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月21日  
 *@Version:1.1.0  
 */
public enum AliIotDevTypeEnum {

	
	OBOX,
	
	DEVICE ;
	
 
	
	private AliIotDevTypeEnum() {
		
	}
	
 
	public static AliIotDevTypeEnum getType(String devType) {
		for (AliIotDevTypeEnum typeEnum : AliIotDevTypeEnum.values()) {
			if (typeEnum.name().equals(devType)) {
				return typeEnum;
			}
		}
		return null;
	}
 


}
