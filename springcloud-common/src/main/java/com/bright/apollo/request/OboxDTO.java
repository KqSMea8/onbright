package com.bright.apollo.request;

import java.io.Serializable;
import java.util.List;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月22日  
 *@Version:1.1.0  
 */
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
	
	 
 	private List<TOboxDeviceConfig> deviceConfigs;
	
	 
 	private List<GroupDTO> groupConfigs;
	
	 
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
