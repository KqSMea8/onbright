package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.dao.device.mapper.AliDeviceConfigMapper;
import com.bright.apollo.service.AliDeviceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliDeviceConfigServiceImpl implements AliDeviceConfigService {

    @Autowired
    private AliDeviceConfigMapper mapper;

    @Override
    public TAliDeviceConfig getAliDeviceConfigBySerializeId(String deviceSerialId) {
        return mapper.getAliDeviceConfigByDeviceSerialId(deviceSerialId);
    }

    @Override
    public List<TAliDeviceConfig> getAliDeviceConfigByUserId(Integer userId) {
        return mapper.getAliDeviceConfigByUserId(userId);
    }

    @Override
    public void update(TAliDeviceConfig aliDeviceConfig) {
        mapper.update(aliDeviceConfig);
    }


    @Override
    public void addAliDevice(TAliDeviceConfig aliDeviceConfig) {
        mapper.addAliDeviceConfig(aliDeviceConfig);
    }

    @Override
    public void deleteAliDeviceConfig(String oboxSerialId) {
        mapper.deleteAliDeviceConfig(oboxSerialId);
    }

    /* (non-Javadoc)
	 * @see com.bright.apollo.service.AliDeviceConfigService#addAliDevConfig(com.bright.apollo.common.entity.TAliDeviceConfig)  
	 */
	@Override
	public void addAliDevConfig(TAliDeviceConfig tAliDeviceConfig) {
		mapper.addAliDeviceConfig(tAliDeviceConfig);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.AliDeviceConfigService#queryWifyDeviceByUserIdAndSerialId(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public TAliDeviceConfig queryWifyDeviceByUserIdAndSerialId(Integer userId, String serialId) {
		 
		return mapper.queryWifyDeviceByUserIdAndSerialId(userId,serialId);
	}
}
