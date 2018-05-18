package com.bright.apollo.service;

import com.bright.apollo.common.entity.TDeviceChannel;

public interface DeviceChannelService {
    void deleteDeviceChannel(int deviceId);

    void addDeviceChannel(TDeviceChannel deviceChannel);

    void delectDeviceChannelByOboxId(int oboxId);


}
