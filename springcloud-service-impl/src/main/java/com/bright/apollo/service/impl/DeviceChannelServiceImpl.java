package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TDeviceChannel;
import com.bright.apollo.dao.device.mapper.TDeviceChannelMapper;
import com.bright.apollo.service.DeviceChannelService;

import java.util.List;

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

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceChannelService#getDeviceChannelById(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public TDeviceChannel getDeviceChannelById(Integer deviceId, Integer oboxId) {
		   
		return deviceChannelMapper.getDeviceChannelById(deviceId,oboxId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceChannelService#updateDeviceChannel(com.bright.apollo.common.entity.TDeviceChannel)  
	 */
	@Override
	public void updateDeviceChannel(TDeviceChannel tDeviceChannel) {
		deviceChannelMapper.updateDeviceChannel(tDeviceChannel);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceChannelService#getDeivceChannelById(java.lang.Integer)  
	 */
	@Override
	public List<TDeviceChannel> getDeivceChannelById(Integer deviceId) {
 		return deviceChannelMapper.getDeivceChannelById(deviceId);
	}
}
