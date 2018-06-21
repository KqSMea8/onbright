package com.bright.apollo.request;

import java.io.Serializable;
import java.util.List;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月20日  
 *@Version:1.1.0  
 */
public class SceneConditionDTO implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 1L;

	
	private java.lang.String condition;//
 
	private java.lang.String conditionType;//
	
 
	private java.lang.String conditionID;//
	
 
	private java.lang.String deviceSerialId;//remark:;length:10; not null,default:null
	
 
	private java.lang.String oboxSerialId;//remark:;length:128; not null,default:null
	
 
	private java.lang.String deviceRfAddr;//remark:;length:128; not null,default:null
	
	
 
	private java.lang.String deviceType;//remark:;length:128; not null,default:null
 
	private java.lang.String deviceChildType;//remark:;length:128; not null,default:null
	
 
	private List<String> oboxs;
 
	public java.lang.String getCondition() {
		return condition;
	}
	
	public void setCondition(java.lang.String condition) {
		this.condition = condition;
	}
	
	public java.lang.String getConditionID() {
		return conditionID;
	}
	
	public void setConditionID(java.lang.String conditionID) {
		this.conditionID = conditionID;
	}
	
	public java.lang.String getDeviceRfAddr() {
		return deviceRfAddr;
	}
	
	public void setDeviceRfAddr(java.lang.String deviceRfAddr) {
		this.deviceRfAddr = deviceRfAddr;
	}
	
	public java.lang.String getDeviceSerialId() {
		return deviceSerialId;
	}
	
	public void setDeviceSerialId(java.lang.String deviceSerialId) {
		this.deviceSerialId = deviceSerialId;
	}
	
	public java.lang.String getOboxSerialId() {
		return oboxSerialId;
	}
	
	public void setOboxSerialId(java.lang.String oboxSerialId) {
		this.oboxSerialId = oboxSerialId;
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
	
	public List<String> getOboxs() {
		return oboxs;
	}
	
	public void setOboxs(List<String> oboxs) {
		this.oboxs = oboxs;
	}
	
	public java.lang.String getConditionType() {
		return conditionType;
	}
	
	public void setConditionType(java.lang.String conditionType) {
		this.conditionType = conditionType;
	}
 

}
