package com.bright.apollo.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TDeviceStatus;
import com.bright.apollo.constant.SubTableConstant;
import com.bright.apollo.dao.device.mapper.TDeviceStatusServiceMapper;
import com.bright.apollo.service.DeviceStatusService;
import com.bright.apollo.tool.DateHelper;
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
		return mapper.queryDeviceStatusByCount(serialId,start,count);
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
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceStatusService#addDeviceStatus(com.bright.apollo.common.entity.TDeviceStatus)  
	 */
	@Override
	public void addDeviceStatus(TDeviceStatus tDeviceStatus) {
		 
		mapper.addDeviceStatus(tDeviceStatus);
	}

}
