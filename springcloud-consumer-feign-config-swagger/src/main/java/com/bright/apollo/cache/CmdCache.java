package com.bright.apollo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.redis.RedisBussines;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月31日  
 *@Version:1.1.0  
 */
@Component
public class CmdCache {
	@Autowired
	private RedisBussines redisBussines;
	public String getLocalSceneInfo(String sceneName,String oboxSerialId,String sceneGroup,int oboxSceneNumber) {
		return redisBussines.get(sceneName+oboxSerialId+sceneGroup+oboxSceneNumber);
	}
	public void delLocalSceneInfo(String sceneName,String oboxSerialId,String sceneGroup,int oboxSceneNumber) {
		redisBussines.delete(sceneName+oboxSerialId+sceneGroup+oboxSceneNumber);
	}
}
