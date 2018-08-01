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
	public void saveAddLocalSceneInfo(String sceneName,String oboxSerialId,String sceneGroup,int oboxSceneNumber,int sceneNumebr) {
		redisBussines.setValueWithExpire(sceneName+oboxSerialId+sceneGroup+oboxSceneNumber, sceneNumebr+"",60);
	}
	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	public void deleteWrite(String oboxSerialId) {
		redisBussines.delete("write"+oboxSerialId);
	}
}
