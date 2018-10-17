package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.dao.device.mapper.AliDeviceConfigMapper;
import com.bright.apollo.service.AliDeviceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliDeviceConfigServiceImpl implements AliDeviceConfigService {

    @Autowired
    private AliDeviceConfigMapper mapper;

    @Override
    public TAliDeviceConfig getAliDeviceConfigBySerializeId(String deviceSerialId) {
        return mapper.getAliDeviceConfigByDeviceSerialId(deviceSerialId);
    }

    @Override
    public void update(TAliDeviceConfig aliDeviceConfig) {
        mapper.update(aliDeviceConfig);
    }

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.AliDeviceConfigService#addAliDevConfig(com.bright.apollo.common.entity.TAliDeviceConfig)  
	 */
	@Override
	public void addAliDevConfig(TAliDeviceConfig tAliDeviceConfig) {
		mapper.addAliDevConfig(tAliDeviceConfig);
		
	}

 
}
