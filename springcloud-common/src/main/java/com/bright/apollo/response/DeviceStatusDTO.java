package com.bright.apollo.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月28日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceStatusDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = -8142235804090666L;
	@JsonProperty
	private long time;

	@JsonProperty
	private java.lang.String status;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public java.lang.String getStatus() {
		return status;
	}

	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	
}
