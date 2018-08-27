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
			return keyExist;
		synchronized ("write"+oboxSerialId) {
			if(redisBussines.isKeyExist("write"+oboxSerialId))
				return true;
			redisBussines.setValueWithExpire("write"+oboxSerialId, "write",60l);
			return false;
		}
	}
	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	public void delWrite(String oboxSerialId) {
		try {
			if(redisBussines.isKeyExist("write"+oboxSerialId))
				redisBussines.delete("write"+oboxSerialId);
		} catch (Exception e) {
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
	/**  
	 * @param authToken  
	 * @Description:  
	 */
	public String getIntelligentSerialId(String authToken) {
		return redisBussines.get(authToken);
		
	}
	/**  
	 * @param serialId
	 * @param sessionKey  
	 * @Description:  
	 */
	public void createIntelligentForgetPwdKey(String serialId, String sessionKey) {
		redisBussines.setValueWithExpire(String.valueOf(serialId)+"_sk", sessionKey, token_time);
 	}
	/**  
	 * @param sessionKey
	 * @param pwd  
	 * @Description:  
	 */
	public void createIntelligentForgetPwd(String sessionKey, String pwd) {
		redisBussines.setValueWithExpire(String.valueOf(sessionKey)+"_sp", pwd, token_time);
		
	}
	
}
