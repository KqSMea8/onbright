package com.bright.apollo.request;

import java.io.Serializable;
import java.util.List;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TServerGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月22日  
 *@Version:1.1.0  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDTO extends TServerGroup implements Serializable{

	
	/**  
	 *   
	 */
	private static final long serialVersionUID = -1048679776584511102L;


	public GroupDTO(TServerGroup tServerGroup) {
		setId(tServerGroup.getId());
		setGroupName(tServerGroup.getGroupName());
		setGroupChildType(tServerGroup.getGroupChildType());
		setGroupState(tServerGroup.getGroupState());
		setGroupType(tServerGroup.getGroupType());
		setGroupStyle(tServerGroup.getGroupStyle());
	}
	@JsonProperty(value="group_member")
	private List<TOboxDeviceConfig> groupMember;
	@JsonProperty(value="obox_serial_id")
	private java.lang.String oboxSerialId;
	@JsonProperty(value="groupAddr")
	private java.lang.String oboxGroupAddr;
	
	
	public List<TOboxDeviceConfig> getGroupMember() {
		return groupMember;
	}
	
	public void setGroupMember(List<TOboxDeviceConfig> groupMember) {
		this.groupMember = groupMember;
	}
	
	public java.lang.String getOboxSerialId() {
		return oboxSerialId;
	}
	
	public void setOboxSerialId(java.lang.String oboxSerialId) {
		this.oboxSerialId = oboxSerialId;
	}

	public void setOboxGroupAddr(java.lang.String oboxGroupAddr) {
		this.oboxGroupAddr = oboxGroupAddr;
	}
	
	public java.lang.String getOboxGroupAddr() {
		return oboxGroupAddr;
	}

	@Override
	public String toString() {
		return "GroupDTO [groupMember=" + groupMember + ", oboxSerialId=" + oboxSerialId + ", oboxGroupAddr="
				+ oboxGroupAddr + "]";
	}

}
