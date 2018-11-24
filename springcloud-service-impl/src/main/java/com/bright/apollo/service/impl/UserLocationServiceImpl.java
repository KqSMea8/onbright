package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TUserLocation;
import com.bright.apollo.dao.user.mapper.TUserLocationMapper;
import com.bright.apollo.service.UserLocationService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
@Service
public class UserLocationServiceImpl implements UserLocationService{
	@Autowired
	private TUserLocationMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserLocationService#addUserLocation(com.bright.apollo.common.entity.TUserLocation)  
	 */
	@Override
	public int addUserLocation(TUserLocation tUserLocation) {
		return mapper.addUserLocation(tUserLocation);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserLocationService#deleteUserLocation(java.lang.Integer)  
	 */
	@Override
	public void deleteUserLocation(Integer location) {
		mapper.deleteUserLocation(location);
		
	}

}
