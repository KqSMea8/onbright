package com.bright.apollo.service;

import com.bright.apollo.common.entity.TAliDeviceConfig;

import java.util.List;

public interface AliDeviceConfigService {

    TAliDeviceConfig getAliDeviceConfigBySerializeId(String oboxSerialId);

    List<TAliDeviceConfig> getAliDeviceConfigByUserId(Integer userId);

    void update(TAliDeviceConfig aliDeviceConfig);

 
	/**  
	 * @param tAliDeviceConfig  
	 * @Description:  
	 */
	void addAliDevConfig(TAliDeviceConfig tAliDeviceConfig);

 
	void addAliDevice(TAliDeviceConfig aliDeviceConfig);

	void deleteAliDeviceConfig(String oboxSerialId);
 
}
