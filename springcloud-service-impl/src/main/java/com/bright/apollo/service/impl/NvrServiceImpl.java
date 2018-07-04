package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.dao.device.mapper.TNvrMapper;
import com.bright.apollo.service.NvrService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月28日  
 *@Version:1.1.0  
 */
@Service
public class NvrServiceImpl implements NvrService{
 
	@Autowired
	private TNvrMapper nvrMapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.NvrService#getNvrByIP(java.lang.String)  
	 */
	@Override
	public TNvr getNvrByIP(String ip) {
		
		return nvrMapper.getNvrByIP(ip);
	}
 
}
