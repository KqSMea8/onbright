package com.bright.apollo.enums;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */
public enum FingerWarnEnum {

	/*（１）撬门；
	（２）胁迫报警；
	（３）多次验证失败；
	（4）有人反锁（默认disable）；
	（5）门虚掩超过10秒（默认disable）；
	（6）电量低于10%。*/
	//撬门
	jimmy("c500","撬门",1),
	//胁迫
	stress("c509","胁迫报警",2),
	
	multiple_validation_failed("c508","多次验证失败",3),
	
	overdoor("c607","门虚掩超过10秒",4),
	
	back_lock("c604","有人反锁",5),
	
	low_betty("c507","电量低于10%",6);
	
	private String cmd;
	
	private String value;
	
	private Integer id;
	private FingerWarnEnum(String cmd,String v,Integer id) {
		this.cmd=cmd;
		this.value = v;
		this.id=id;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


}
