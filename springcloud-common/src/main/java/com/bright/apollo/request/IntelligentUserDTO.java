package com.bright.apollo.request;

import java.io.Serializable;

import com.bright.apollo.common.entity.TIntelligentFingerUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
public class IntelligentUserDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = -5188570341220053319L;
 
	private String nickName;
 
 
	private Integer identity;
 
	private String mobile;
 
	private String pin;
 
	private java.lang.Integer exist;//remark:;length:10; not null,default:null

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public java.lang.Integer getExist() {
		return exist;
	}

	public void setExist(java.lang.Integer exist) {
		this.exist = exist;
	}

	/**  
	 *   
	 */
	public IntelligentUserDTO() {
		super();
	}
	
	/**  
	 *   
	 */
	public IntelligentUserDTO(TIntelligentFingerUser user) {
		super();
		if(user!=null){
			setExist(user.getExistForce());
			setPin(user.getPin());
			setIdentity(user.getIdentity());
			setMobile(user.getMobile());
			setNickName(user.getNickName());
		}
	}
	
}
