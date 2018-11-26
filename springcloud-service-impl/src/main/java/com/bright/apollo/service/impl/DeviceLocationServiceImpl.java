package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TDeviceLocation;
import com.bright.apollo.dao.device.mapper.TDeviceLocationMapper;
import com.bright.apollo.service.DeviceLocationService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
@Service
public class DeviceLocationServiceImpl implements DeviceLocationService{
	@Autowired
	private TDeviceLocationMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceLocationService#addDeviceLocation(com.bright.apollo.common.entity.TDeviceLocation)  
	 */
	@Override
	public int addDeviceLocation(TDeviceLocation tDeviceLocation) {
		// TODO Auto-generated method stub  
		return mapper.addDeviceLocation(tDeviceLocation);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceLocationService#queryDevicesByLocation(java.lang.Integer)  
	 */
	@Override
	public List<TDeviceLocation> queryDevicesByLocation(Integer location) {
		return mapper.queryDevicesByLocation(location);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceLocationService#deleteDeviceLocation(java.lang.Integer)  
	 */
	@Override
	public void deleteDeviceLocation(Integer id) {
		mapper.deleteDeviceLocation(id);
		
	}
}
