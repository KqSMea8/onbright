package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.dao.device.mapper.AliDeviceConfigMapper;
import com.bright.apollo.service.AliDeviceConfigService;
import org.springframework.beans.factory.annotation.Autowired;

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
}
