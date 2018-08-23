package com.bright.apollo.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月23日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFingerDTO implements Serializable{

	
	 
	/**  
	 *   
	 */
	private static final long serialVersionUID = 6864778406063161077L;


	private java.lang.String name;//remark:用户名;length:128; not null,default:null
	
	 
	private java.lang.String pin;//remark:用户名;length:128; not null,default:null
	 
	private java.lang.String weight;
	public UserFingerDTO(String name,String pin,String weight) {
		// TODO Auto-generated constructor stub
		setName(name);
		setPin(pin);
		setWeight(weight);
	}
	
	public java.lang.String getName() {
		return name;
	}
	
	public void setName(java.lang.String name) {
		this.name = name;
	}
	
	public java.lang.String getPin() {
		return pin;
	}
	
	public void setPin(java.lang.String pin) {
		this.pin = pin;
	}

	public java.lang.String getWeight() {
		return weight;
	}

	public void setWeight(java.lang.String weight) {
		this.weight = weight;
	}


}
