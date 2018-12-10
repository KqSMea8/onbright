package com.bright.apollo.response;

import java.io.Serializable;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月27日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceDTO extends TOboxDeviceConfig implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = -8294630709743738141L;
	public DeviceDTO(){
		super();
	}
	public DeviceDTO(TOboxDeviceConfig tOboxDeviceConfig) {
		// TODO Auto-generated constructor stub
		setDeviceSerialId(tOboxDeviceConfig.getDeviceSerialId());
		setDeviceChildType(tOboxDeviceConfig.getDeviceChildType());
		setDeviceType(tOboxDeviceConfig.getDeviceType());
		setDeviceRfAddr(tOboxDeviceConfig.getDeviceRfAddr());
		setDeviceState(tOboxDeviceConfig.getDeviceState());
		setOboxSerialId(tOboxDeviceConfig.getOboxSerialId());
		setDeviceId(tOboxDeviceConfig.getDeviceId());
	}
	
	 
	@JsonProperty("x_axis")
	private java.lang.Integer xAxis;//
	
	 
	@JsonProperty("y_axis")
	private java.lang.Integer yAxis;//
	
	public java.lang.Integer getxAxis() {
		return xAxis;
	}
	
	public void setxAxis(java.lang.Integer xAxis) {
		this.xAxis = xAxis;
	}
	
	public java.lang.Integer getyAxis() {
		return yAxis;
	}
	
	public void setyAxis(java.lang.Integer yAxis) {
		this.yAxis = yAxis;
	}

}
