package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TDeviceChannel;
import com.bright.apollo.dao.device.mapper.TDeviceChannelMapper;
import com.bright.apollo.service.DeviceChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceChannelServiceImpl  implements DeviceChannelService {

    @Autowired
    private TDeviceChannelMapper deviceChannelMapper;

    @Override
    public void deleteDeviceChannel(int deviceId) {
        deviceChannelMapper.deleteDeviceChannel(deviceId);
    }

    @Override
    public void addDeviceChannel(TDeviceChannel deviceChannel) {
        deviceChannelMapper.addDeviceChannel(deviceChannel);
    }

    @Override
    public void delectDeviceChannelByOboxId(int oboxId) {

    }
}
