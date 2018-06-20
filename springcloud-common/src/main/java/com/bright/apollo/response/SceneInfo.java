package com.bright.apollo.response;

import java.io.Serializable;
import java.util.List;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月17日  
 *@Version:1.1.0  
 */
@Deprecated
public class SceneInfo implements Serializable  {

	/**  
	 *   
	 */
	private static final long serialVersionUID = -2340670150645451322L;
	
	private TScene scene;
	
	private List<TSceneAction> actions;
	
	private List<TSceneCondition> conditions;

	public TScene getScene() {
		return scene;
	}

	public void setScene(TScene scene) {
		this.scene = scene;
	}

	public List<TSceneAction> getActions() {
		return actions;
	}

	public void setActions(List<TSceneAction> actions) {
		this.actions = actions;
	}

	public List<TSceneCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<TSceneCondition> conditions) {
		this.conditions = conditions;
	}
	
	
}
