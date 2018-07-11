package com.bright.apollo.request;

import java.io.Serializable;
import java.util.List;

import com.bright.apollo.common.entity.TScene;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月20日  
 *@Version:1.1.0  	
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SceneDTO extends TScene implements Serializable{

	/**  
	 *   
	 */
	private static final long serialVersionUID = 1L;

	
	public SceneDTO(TScene scene) {
		setOboxSerialId(scene.getOboxSerialId());
		setSceneNumber(scene.getSceneNumber());
		setOboxSceneNumber(scene.getOboxSceneNumber());
		setMessageAlter(scene.getMsgAlter());
		setSceneGroup(scene.getSceneGroup());
		setSceneName(scene.getSceneName());
		setSceneType(scene.getSceneType());
		setSceneStatus(scene.getSceneStatus());
	}
	

	/**
	 * scene config
	 */
	@JsonProperty(value="actions")
	private List<SceneActionDTO> actions;
	@JsonProperty(value="conditions")
	private List<List<SceneConditionDTO>> conditions;
	
	public List<SceneActionDTO> getActions() {
		return actions;
	}

	public void setActions(List<SceneActionDTO> actions) {
		this.actions = actions;
	}
	
	public List<List<SceneConditionDTO>> getConditions() {
		return conditions;
	}
	
	public void setConditions(List<List<SceneConditionDTO>> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return "SceneDTO [actions=" + actions + ", conditions=" + conditions
				+ "]";
	}
	

}
