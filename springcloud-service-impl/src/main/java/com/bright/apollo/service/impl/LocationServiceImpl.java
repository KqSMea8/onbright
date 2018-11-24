package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.dao.device.mapper.TLocationMapper;
import com.bright.apollo.service.LocationService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
@Service
public class LocationServiceImpl implements LocationService{
	@Autowired
	private TLocationMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationService#queryLocationByUserId(java.lang.String, java.lang.String, java.lang.Integer)  
	 */
	@Override
	public TLocation queryLocationByUserId(String building, String room, Integer userId) {
	 
		return mapper.queryLocat1ionByUserId(building,room,userId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationService#addLocation(com.bright.apollo.common.entity.TLocation)  
	 */
	@Override
	public int addLocation(TLocation tLocation) {
		return mapper.addLocation(tLocation);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationService#queryLocationByUserIdAndId(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public TLocation queryLocationByUserIdAndId(Integer userId, Integer location) {
 		return mapper.queryLocationByUserIdAndId(userId,location);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationService#deleteLocation(java.lang.Integer)  
	 */
	@Override
	public void deleteLocation(Integer location) {
		mapper.deleteLocation(location);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationService#updateLocation(com.bright.apollo.common.entity.TLocation)  
	 */
	@Override
	public void updateLocation(TLocation tLocation) {
		mapper.updateLocation(tLocation);
		
	}
}
