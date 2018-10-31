package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TDeviceChannel;

public interface DeviceChannelService {
    void deleteDeviceChannel(int deviceId);

    void addDeviceChannel(TDeviceChannel deviceChannel);

    void delectDeviceChannelByOboxId(int oboxId);

	/**  
	 * @param returnIndex
	 * @param oboxId
	 * @return  
	 * @Description:  
	 */
	TDeviceChannel getDeviceChannelById(Integer deviceId, Integer oboxId);

	/**  
	 * @param tDeviceChannel  
	 * @Description:  
	 */
	void updateDeviceChannel(TDeviceChannel tDeviceChannel);

	/**  
	 * @param deviceId
	 * @return  
	 * @Description:  
	 */
	List<TDeviceChannel> getDeivceChannelById(Integer deviceId);


}
