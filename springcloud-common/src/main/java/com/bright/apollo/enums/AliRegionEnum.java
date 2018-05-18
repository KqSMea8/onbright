package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年5月4日  
 *@Version:1.1.0  
 */

public enum AliRegionEnum {
	
	SOURTHCHINA("cn-shanghai"),
	
	ASIA("ap-southease-2"),
	
	AMERICA("us-west-1");
	
	private String value;
	
	private AliRegionEnum(String v) {
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static AliRegionEnum getRegion(String name) {
		for (AliRegionEnum typeEnum : AliRegionEnum.values()) {
			if (typeEnum.name().equals(name)) {
				return typeEnum;
			}
		}
		return null;
	}

}