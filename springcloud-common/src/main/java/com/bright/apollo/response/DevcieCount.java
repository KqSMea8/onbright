package com.bright.apollo.response;

import java.io.Serializable;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月29日  
 *@Version:1.1.0  
 */
public class DevcieCount implements Serializable{
	/**  
	 *   
	 */
	private static final long serialVersionUID = -5198494139613428830L;
	private String type;
	private Integer count;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
