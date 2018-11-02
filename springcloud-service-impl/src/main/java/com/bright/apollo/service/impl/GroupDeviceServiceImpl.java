package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bright.apollo.common.entity.TGroupDevice;
import com.bright.apollo.dao.device.mapper.GroupDeviceMapper;
import com.bright.apollo.service.GroupDeviceService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
public class GroupDeviceServiceImpl implements GroupDeviceService{
	@Autowired
	private GroupDeviceMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.GroupDeviceService#queryDeviceGroupByGroupId(java.lang.Integer)  
	 */
	@Override
	public List<TGroupDevice> queryDeviceGroupByGroupId(Integer groupId) {
		  
		return mapper.queryDeviceGroupByGroupId(groupId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.GroupDeviceService#queryDeviceGroup(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public TGroupDevice queryDeviceGroup(Integer groupId, String deviceSerialId) {
 		return mapper.queryDeviceGroup(groupId,deviceSerialId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.GroupDeviceService#addDeviceGroup(com.bright.apollo.common.entity.TGroupDevice)  
	 */
	@Override
	public int addDeviceGroup(TGroupDevice groupDevice) {
		 
		return mapper.addDeviceGroup(groupDevice);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.GroupDeviceService#deleteDeviceGroup(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public int deleteDeviceGroup(Integer groupId, String deviceSerialId) {  
		return mapper.deleteDeviceGroup(groupId,deviceSerialId);
	}
}
