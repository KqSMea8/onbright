package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TOboxDeviceConfigExample;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.service.DeviceService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Service
public class DeviceServiceImpl implements DeviceService {
//	@SuppressWarnings("rawtypes")
//	@Autowired
//	private DeviceBusiness deviceBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExample(java.lang.
	 * Object)
	 */
	@Override
	@Deprecated
	public <T, E> T handlerExample(E e) {
		List<T> selectByExample = handlerExampleToList(e);
		if (selectByExample != null && selectByExample.size() > 0)
			return selectByExample.get(0);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExampleToList(java.
	 * lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public <T, E> List<T> handlerExampleToList(E e) {
//		return deviceBusiness.selectByExample(e);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#queryTById(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public <T> T queryTById(T t) {
//		if (t != null)
//			return (T) deviceBusiness.selectByPrimaryKey(t);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceService#queryDeviceBySerialId(java.lang.String)  
	 */
	@Override
	@Deprecated
	public TOboxDeviceConfig queryDeviceBySerialId(String serialId) {
		TOboxDeviceConfigExample example=new TOboxDeviceConfigExample();
		example.or().andDeviceSerialIdEqualTo(serialId);
		return handlerExample(example);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceService#updateDeviceBySerialId(com.bright.apollo.common.entity.TOboxDeviceConfig)  
	 */
	@Override
	@Deprecated
	public void updateDeviceBySerialId(TOboxDeviceConfig device) {
//		deviceBusiness.updateDeviceBySerialId(device);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceService#addDevice(com.bright.apollo.common.entity.TOboxDeviceConfig)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int addDevice(TOboxDeviceConfig device) {
		  
//		return deviceBusiness.insertSelective(device);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceService#deleteDeviceBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public void deleteDeviceBySerialId(String serialId) {
		TOboxDeviceConfigExample example=new TOboxDeviceConfigExample();
		example.or().andDeviceSerialIdEqualTo(serialId);
//		deviceBusiness.deleteByExample(example);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceService#queryDeviceByUserId(java.lang.Integer, java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TOboxDeviceConfig> queryDeviceByUserId(Integer userId, Integer pageIndex, Integer pageSize) {
		  
//		return deviceBusiness.queryDeviceByUserId(userId,pageIndex*pageSize,(pageIndex+1)*pageSize);
		return  null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.DeviceService#queryCountOboxByUserId(java.lang.Integer)  
	 */
	@Override
	@Deprecated
	public int queryCountDeviceByUserId(Integer userId) {
	 
//		return deviceBusiness.queryCountDeviceByUserId(userId);
		return 0;
	}
 
}
