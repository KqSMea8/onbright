package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TDeviceStatus;
import com.bright.apollo.constant.SubTableConstant;
import com.bright.apollo.dao.device.mapper.TDeviceStatusServiceMapper;
import com.bright.apollo.service.DeviceStatusService;
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月28日  
 *@Version:1.1.0  
 */
@Service
public class DeviceStatusServiceImpl implements DeviceStatusService{

	@Autowired
	private TDeviceStatusServiceMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceStatusService#queryDeviceStatusByCount(java.lang.String, int, int)  
	 */
	@Override
	public List<TDeviceStatus> queryDeviceStatusByCount(String serialId, int start, int count) {
		return mapper.queryDeviceStatusByCount(SubTableConstant.T_DEVICE_STATUS_MERGE,serialId,start,count);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceStatusService#queryDeviceStatusByData(java.lang.String, long, long)  
	 */
	@Override
	public List<TDeviceStatus> queryDeviceStatusByData(String serialId, long from, long to) {
		return mapper.queryDeviceStatusByData(serialId,from,to);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceStatusService#queryDeviceStatusByDataNoGroup(java.lang.String, long, long)  
	 */
	@Override
	public List<TDeviceStatus> queryDeviceStatusByDataNoGroup(String serialId, long from, long to) {
 		return mapper.queryDeviceStatusByDataNoGroup(serialId,from,to);
	}

}
