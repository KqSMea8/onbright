package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.dao.user.mapper.TUserAliDeviceMapper;
import com.bright.apollo.service.UserAliDevService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月17日  
 *@Version:1.1.0  
 */
@Service
public class UserAliDevServiceImpl implements UserAliDevService{

	@Autowired
	private TUserAliDeviceMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserAliDevService#deleteUserAliDev(java.lang.String)  
	 */
	@Override
	public void deleteUserAliDev(String deviceSerialId) {
		mapper.deleteUserAliDev(deviceSerialId);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserAliDevService#addUserAliDev(com.bright.apollo.common.entity.TUserAliDevice)  
	 */
	@Override
	public void addUserAliDev(TUserAliDevice tUserAliDev) {
		mapper.addUserAliDev(tUserAliDev);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserAliDevService#queryAliUserId(java.lang.String)  
	 */
	@Override
	public List<TUserAliDevice> queryAliUserId(String deviceSerialId) {
		return mapper.queryAliUserId(deviceSerialId);
		
	}

}
