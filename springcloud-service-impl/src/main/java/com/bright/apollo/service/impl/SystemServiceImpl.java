package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TSystem;
import com.bright.apollo.dao.user.mapper.TSystemMapper;
import com.bright.apollo.service.SystemService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月18日  
 *@Version:1.1.0  
 */
@Service
public class SystemServiceImpl implements SystemService{

	@Autowired
	private TSystemMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SystemService#addSystem(com.bright.apollo.common.entity.TSystem)  
	 */
	@Override
	public int addSystem(TSystem tSystem) {
		return mapper.addSystem(tSystem);
	}

}
