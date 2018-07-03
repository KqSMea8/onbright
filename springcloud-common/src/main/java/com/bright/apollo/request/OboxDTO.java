package com.bright.apollo.request;

import java.io.Serializable;
import java.util.List;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
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
public class OboxDTO extends TObox implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 1L;

	
	public OboxDTO(TObox obox) {
		setOboxName(obox.getOboxName());
		setOboxSerialId(obox.getOboxSerialId());
		setOboxVersion(obox.getOboxVersion());
		setOboxPwd(obox.getOboxPwd());
		setOboxStatus(obox.getOboxStatus());
	}
	
	@JsonProperty(value="device_config")
 	private List<TOboxDeviceConfig> deviceConfigs;
	
	@JsonProperty(value="group_config")
 	private List<GroupDTO> groupConfigs;
	
	@JsonProperty(value="scene_config")
 	private List<SceneDTO> scenes;

	public List<TOboxDeviceConfig> getDeviceConfigs() {
		return deviceConfigs;
	}

	public void setDeviceConfigs(List<TOboxDeviceConfig> deviceConfigs) {
		this.deviceConfigs = deviceConfigs;
	}

	public List<SceneDTO> getScenes() {
		return scenes;
	}

	public void setScenes(List<SceneDTO> scenes) {
		this.scenes = scenes;
	}
	
	public List<GroupDTO> getGroupConfigs() {
		return groupConfigs;
	}
	
	public void setGroupConfigs(List<GroupDTO> groupConfigs) {
		this.groupConfigs = groupConfigs;
	}

	

}
