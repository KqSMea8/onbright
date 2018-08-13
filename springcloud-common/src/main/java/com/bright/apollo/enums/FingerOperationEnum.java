package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */
public enum FingerOperationEnum {

	pwd(1),
	fingerprint(2),
	card(3),
	key(4),
	telecontrol(5),
	remote(6),
	modify_pwd(7),
	delete_user(8),
	close(9),
	register(10)
	;

 
	
	private int value;
	private FingerOperationEnum(int v) {
		this.value = v;

	}

 

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

 



}
