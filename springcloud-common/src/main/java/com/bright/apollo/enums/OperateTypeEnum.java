package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
public enum OperateTypeEnum {


	set("01"),
	
	coverChild("02"),
	
	addChild("03"),
	
	removeChild("04"),
	
	rename("05"),
	
	action("06"),
	
	delete("00");
	
	private String value;
	
	private OperateTypeEnum(String v) {
		// TODO Auto-generated constructor stub
		this.value = v;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public static OperateTypeEnum getEnum(String operateType) {
		for (OperateTypeEnum typeEnum : OperateTypeEnum.values()) {
			if (typeEnum.getValue().equals(operateType)) {
				return typeEnum;
			}
		}
		return null;
	}

}
