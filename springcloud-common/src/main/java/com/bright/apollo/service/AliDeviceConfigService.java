package com.bright.apollo.service;

import com.bright.apollo.common.entity.TAliDeviceConfig;

public interface AliDeviceConfigService {

    TAliDeviceConfig getAliDeviceConfigBySerializeId(String oboxSerialId);

    void update(TAliDeviceConfig aliDeviceConfig);

	/**  
	 * @param tAliDeviceConfig  
	 * @Description:  
	 */
	void addAliDevConfig(TAliDeviceConfig tAliDeviceConfig);

 
}
