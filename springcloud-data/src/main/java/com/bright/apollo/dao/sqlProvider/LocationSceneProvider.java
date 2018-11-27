package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TLocationScene;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月27日  
 *@Version:1.1.0  
 */
public class LocationSceneProvider {
	public String addSceneLocation(final TLocationScene tLocationScene) {
		return new SQL() {
			{
				INSERT_INTO("t_location_scene");
				if (!StringUtils.isEmpty(tLocationScene.getLocationId())) {
					VALUES("location_id", "#{locationId}");
				}

				if (!StringUtils.isEmpty(tLocationScene.getSceneNumber())) {
					VALUES("scene_number", "#{sceneNumber}");
				}
			 
			}
		}.toString();
	}
}
