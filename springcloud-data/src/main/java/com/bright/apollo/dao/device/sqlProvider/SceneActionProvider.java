package com.bright.apollo.dao.device.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerWarn;
import com.bright.apollo.common.entity.TSceneAction;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月13日  
 *@Version:1.1.0  
 */
public class SceneActionProvider {

	public String addAction(final TSceneAction sceneAction) {
		return new SQL() {
			{
				INSERT_INTO("t_scene_action");
				if (!StringUtils.isEmpty(sceneAction.getSceneNumber())) {
					VALUES("scene_number", "#{sceneNumber}");
				}
				if (!StringUtils.isEmpty(sceneAction.getActionid())) {
					VALUES("actionID", "#{actionid}");
				}
				if (!StringUtils.isEmpty(sceneAction.getAction())) {
					VALUES("action", "#{action}");
				}
				if (!StringUtils.isEmpty(sceneAction.getNodeType())) {
					VALUES("node_type", "#{nodeType}");
				}
				if (!StringUtils.isEmpty(sceneAction.getPreset())) {
					VALUES("preSet", "#{preset}");
				}
			}
		}.toString();
	}

}
