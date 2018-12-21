package com.bright.apollo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.redis.RedisBussines;

import java.util.List;

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
	/**  
	 * @param deviceSerialId
	 * @return  
	 * @Description:  
	 */
	public String getIntelligentForgetPwdKey(String serialId) {
		 
		return redisBussines.get(String.valueOf(serialId)+"_sk");
	}
	/**  
	 * @param key
	 * @return  
	 * @Description:  
	 */
	public String getIntelligentForgetPwd(String key) {
		return redisBussines.get(String.valueOf(key)+"_sp");
	}

	public String getIrTestCodeAppKeyBrandIdDeviceType(String key) {
		return redisBussines.get(key);
	}

	public void setIRDeviceInfoList(String listName,List list){
		redisBussines.setList(listName,list);
	}

	public List getIRDeviceInfoList(String listName){
		return redisBussines.getList(listName);
	}

	public void addIrTestCodeSerialId(String  serialId,String index) {
		redisBussines.setValueWithExpire("serialId_"+index, serialId, medium_time);
	}

	public void addIrIndexBySerialId(String  serialId,String index) {
		redisBussines.setValueWithExpire("index_"+serialId, index, medium_time);
	}

	public void addIrTestCodeKeyNameType(String index, String  keyNameType) {
		redisBussines.setValueWithExpire("keyNameType_"+index, keyNameType, medium_time);
	}

	public void addIrTestCodeKeyName(String index, String  keyName) {
		redisBussines.setValueWithExpire("keyName_"+index, keyName, medium_time);
	}

	public void addIrIndex(String index) {
		redisBussines.setValueWithExpire("index_"+index, index, medium_time);
	}

	public void addIrBrandId(String index,String brandId) {
		redisBussines.setValueWithExpire("brandId_"+index, brandId, medium_time);
	}

	public void addIrBrandIdBySerialId(String serialId,String brandId) {
		redisBussines.setValueWithExpire("brandId_"+serialId, brandId, medium_time);
	}

	public void addIrDeviceType(String index,String deviceType) {
		redisBussines.setValueWithExpire("deviceType_"+index, deviceType, medium_time);
	}

	public void addIrDeviceTypeBySerialId(String serialId,String deviceType) {
		redisBussines.setValueWithExpire("deviceType_"+serialId, deviceType, medium_time);
	}

	public void addIrDeviceTypeByIRName(String serialId,String name) {
		redisBussines.setValueWithExpire("irRemote_"+serialId, name, medium_time);
	}

	/**
	 * @param key
	 * @return  
	 * @Description:  
	 */
	public String getValue(String key) {
		 
		return redisBussines.get(String.valueOf(key));
	}
	/**  
	 * @param key
	 * @Description:  
	 */
	public void deleteKey(String key) {
		redisBussines.delete(key);
		
	}
}
