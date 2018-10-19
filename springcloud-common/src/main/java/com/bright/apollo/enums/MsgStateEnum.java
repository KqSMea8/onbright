package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月19日  
 *@Version:1.1.0  
 */
public enum MsgStateEnum {

	read(1),
	unread(0);
	private int value;
	private MsgStateEnum(int v) {
		// TODO Auto-generated constructor stub
		this.value = v;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	

}
