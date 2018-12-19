package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.common.entity.TUserSceneTemplate;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月19日  
 *@Version:1.1.0  
 */
public class UserSceneTemplateProvider {
	public String addTemplate(final TUserSceneTemplate tUserSceneTemplate) {
		return new SQL() {
			{
				INSERT_INTO("t_user_scene_template");
				if (!StringUtils.isEmpty(tUserSceneTemplate.getModelname())) {
					VALUES("modelName", "#{modelname}");
				}

				if (!StringUtils.isEmpty(tUserSceneTemplate.getUserId())) {
					VALUES("user_id", "#{userId}");
				}
				if (!StringUtils.isEmpty(tUserSceneTemplate.getSceneModel())) {
					VALUES("scene_model", "#{sceneModel}");
				}
				if (!StringUtils.isEmpty(tUserSceneTemplate.getDeviceModel())) {
					VALUES("device_model", "#{deviceModel}");
				}
				 
			}
		}.toString();
	}
}
