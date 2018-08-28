package com.bright.apollo.response;

import java.io.Serializable;

import com.bright.apollo.common.entity.TUserOperation;
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
public class TUserOperationDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = -8604761364317693632L;
	@JsonProperty
	private String device_status;
	@JsonProperty
	private Long time;

	public String getDevice_status() {
		return device_status;
	}

	public void setDevice_status(String device_status) {
		this.device_status = device_status;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	/**  
	 *   
	 */
	public TUserOperationDTO() {
		super();
	}

	/**  
	 *   
	 */
	public TUserOperationDTO(TUserOperation operation) {
		super();
		setTime(operation.getLastOpTime().getTime()/1000);
		setDevice_status(operation.getDeviceState());
	}

	
}
