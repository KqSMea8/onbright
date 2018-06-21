package com.bright.apollo.request;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月20日  
 *@Version:1.1.0  
 */
public class TSceneAction implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 1L;

	
 
	private java.lang.String action;//
	
	 
	private java.lang.String nodeType;//
	
	 
	private java.lang.String actionName;//
	 
	private java.lang.String deviceSerialId;//remark:;length:10; not null,default:null
	
	 
	private java.lang.String oboxSerialId;//remark:;length:128; not null,default:null
	
	 
	private java.lang.String deviceRfAddr;//remark:;length:128; not null,default:null
	
 
	private java.lang.String oboxGroupAddr;//remark:;length:128; not null,default:null
	
	 
	private java.lang.String groupId;//remark:;length:128; not null,default:null
	
 
	private java.lang.String deviceType;//remark:;length:128; not null,default:null
	
 
	private java.lang.String deviceChildType;//remark:;length:128; not null,default:null
	
 
	private java.lang.Integer sceneNumber;//remark:;length:128; not null,default:null
	
 
	private java.lang.Integer preSet;//remark:;length:128; not null,default:null
	
	private java.lang.Integer actionType = 1;
	
	public java.lang.Integer getActionType() {
		return actionType;
	}
	
	public void setActionType(java.lang.Integer actionType) {
		this.actionType = actionType;
	}
	
	public void setAction(java.lang.String action) {
		this.action = action;
	}
	
	public java.lang.String getAction() {
		return action;
	}
	
	public void setActionName(java.lang.String actionName) {
		this.actionName = actionName;
	}
	
	public java.lang.String getActionName() {
		return actionName;
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
	
	public java.lang.String getGroupId() {
		return groupId;
	}
	
	public void setGroupId(java.lang.String groupId) {
		this.groupId = groupId;
	}
	
	
	public java.lang.String getOboxSerialId() {
		return oboxSerialId;
	}
	
	public void setOboxSerialId(java.lang.String oboxSerialId) {
		this.oboxSerialId = oboxSerialId;
	}
	
	public void setDeviceChildType(java.lang.String deviceChildType) {
		this.deviceChildType = deviceChildType;
	}
	
	public java.lang.String getDeviceChildType() {
		return deviceChildType;
	}
	
	public void setDeviceType(java.lang.String deviceType) {
		this.deviceType = deviceType;
	}
	
	public java.lang.String getDeviceType() {
		return deviceType;
	}

	public java.lang.String getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(java.lang.String nodeType) {
		this.nodeType = nodeType;
	}
	
	public java.lang.String getOboxGroupAddr() {
		return oboxGroupAddr;
	}
	
	public void setOboxGroupAddr(java.lang.String oboxGroupAddr) {
		this.oboxGroupAddr = oboxGroupAddr;
	}
	
	public java.lang.Integer getSceneNumber() {
		return sceneNumber;
	}
	
	public void setSceneNumber(java.lang.Integer sceneNumber) {
		this.sceneNumber = sceneNumber;
	}

	@Override
	public String toString() {
		return "SceneActionDTO [action=" + action + ", nodeType=" + nodeType
				+ ", actionName=" + actionName + ", deviceSerialId="
				+ deviceSerialId + ", oboxSerialId=" + oboxSerialId
				+ ", deviceRfAddr=" + deviceRfAddr + ", oboxGroupAddr="
				+ oboxGroupAddr + ", groupId=" + groupId + ", deviceType="
				+ deviceType + ", deviceChildType=" + deviceChildType
				+ ", sceneNumber=" + sceneNumber + ", preSet=" + preSet
				+ ", actionType=" + actionType + "]";
	}
	

}
