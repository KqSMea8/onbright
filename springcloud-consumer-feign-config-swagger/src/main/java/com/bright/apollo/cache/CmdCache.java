package com.bright.apollo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.redis.RedisBussines;
import com.zz.common.util.RandomUtils;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月31日  
 *@Version:1.1.0  
 */
@Component
public class CmdCache {
	private static int medium_time=5*60;
	private static int token_time = 60 * 60;
	@Autowired
	private RedisBussines redisBussines;
	public String getLocalSceneInfo(String sceneName,String oboxSerialId,String sceneGroup,int oboxSceneNumber) {
		return redisBussines.get(sceneName+oboxSerialId+sceneGroup+oboxSceneNumber);
	}
	public void delLocalSceneInfo(String sceneName,String oboxSerialId,String sceneGroup,int oboxSceneNumber) {
		redisBussines.delete(sceneName+oboxSerialId+sceneGroup+oboxSceneNumber);
	}
	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	public boolean setWrite(String oboxSerialId) {
		boolean keyExist = redisBussines.isKeyExist("write"+oboxSerialId);
		if(keyExist)
			return !keyExist;
		synchronized ("write"+oboxSerialId) {
			if(redisBussines.isKeyExist("write"+oboxSerialId))
				return false;
			redisBussines.setValueWithExpire("write"+oboxSerialId, "write",60l);
			return true;
		}
	}
	/**  
	 * @param mobile
	 * @param pin
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	public String getMobileValidateCode(String mobile, String pin, String serialId) {
		return redisBussines.get(mobile+"@"+pin+"$"+serialId);
	}
	/**  
	 * @param mobile
	 * @param pin
	 * @param serialId
	 * @param validateCode  
	 * @Description:  
	 */
	public void addMobileValidateCode(String mobile, String pin, String serialId, String validateCode) {
		redisBussines.setValueWithExpire(mobile+"@"+pin+"$"+serialId, validateCode, medium_time);
	}
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	public String addIntelligentToken(String serialId) {
		String uuid = RandomUtils.genUUID();
		redisBussines.setValueWithExpire(uuid, serialId, token_time);
 		return uuid;
	
	}
}
