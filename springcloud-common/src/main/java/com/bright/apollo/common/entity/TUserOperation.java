package com.bright.apollo.common.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TUserOperation implements Serializable{

    /**  
	 *   
	 */
	private static final long serialVersionUID = -9118246422832525513L;
 
	private java.lang.Integer id;//remark:;length:10; not null,default:null
	
	@JsonProperty("device_type")
	private java.lang.String deviceType;
	
	@JsonProperty("device_child_type")
	private java.lang.String deviceChildType;
	
	
	@JsonProperty("device_serial_id")
	private java.lang.String deviceSerialId;//remark:;length:10; not null,default:null
	
	@Expose
	@SerializedName("device_status")
	@JsonProperty("device_state")
	private java.lang.String deviceState;//remark:;length:128; not null,default:null
	

	@JsonProperty("last_op_time")
	private java.util.Date lastOpTime;//remark:;length:19; not null,default:CURRENT_TIMESTAMPs
	
	@Expose
	@SerializedName("time")
 	private long time;
	
	@Expose
	@SerializedName("day")
	private String day;
	
	public java.lang.String getDeviceSerialId() {
		return deviceSerialId;
	}
	
	public void setDeviceSerialId(java.lang.String deviceSerialId) {
		this.deviceSerialId = deviceSerialId;
	}
	
	public void setDeviceState(java.lang.String deviceState) {
		this.deviceState = deviceState;
	}

	public java.lang.String getDeviceState() {
		return deviceState;
	}
	
	public java.util.Date getLastOpTime() {
		return lastOpTime;
	}
	
	public void setLastOpTime(java.util.Date lastOpTime) {
		this.lastOpTime = lastOpTime;
	}

	public java.lang.Integer getId() {
		return id;
	}
	
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public java.lang.String getDeviceChildType() {
		return deviceChildType;
	}
	
	public void setDeviceChildType(java.lang.String deviceChildType) {
		this.deviceChildType = deviceChildType;
	}
	
	public java.lang.String getDeviceType() {
		return deviceType;
	}
	
	public void setDeviceType(java.lang.String deviceType) {
		this.deviceType = deviceType;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}



}
