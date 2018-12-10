package com.bright.apollo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.service.UserDeviceService;
import com.bright.apollo.service.UserOboxService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月8日  
 *@Version:1.1.0  
 */
@Service
public class AsyncServiceImpl {

	@Autowired
	private UserOboxService userOboxService;
	@Autowired
	private UserDeviceService userDeviceService;
	/**  
	 * @param deviceSerialId
	 * @param oboxSerialId  
	 * @Description:  
	 */
	@Async
	public void addUserDeviceBySerialIdAndOboxSerialId(String deviceSerialId, String oboxSerialId) {
		if(StringUtils.isEmpty(deviceSerialId)||StringUtils.isEmpty(oboxSerialId))
			return;
		List<TUserObox> userOboxs = userOboxService.getUserOboxBySerialId(oboxSerialId);
		if(userOboxs==null||userOboxs.size()==0)
			return;
		List<TUserDevice> userDevices=new ArrayList<TUserDevice>();
		for (int i = 0; i < userOboxs.size(); i++) {
			TUserDevice tUserDevice=new TUserDevice();
			tUserDevice.setDeviceSerialId(deviceSerialId);
			tUserDevice.setUserId(userOboxs.get(i).getUserId());
			userDevices.add(tUserDevice);
		}
		userDeviceService.batchAddUserDevice(userDevices);
	}
	@Async
	public void testaddUserDeviceBySerialIdAndOboxSerialId() {
		List<TUserDevice> userDevices=new ArrayList<TUserDevice>();
		for (int i = 0; i < 3; i++) {
			TUserDevice tUserDevice=new TUserDevice();
			tUserDevice.setDeviceSerialId("adadada"+i);
			tUserDevice.setUserId(i+1);
			userDevices.add(tUserDevice);
		}
		 
		userDeviceService.batchAddUserDevice(userDevices);
	}
}
