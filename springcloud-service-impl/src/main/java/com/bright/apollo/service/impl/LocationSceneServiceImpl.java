package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TLocationScene;
import com.bright.apollo.dao.device.mapper.LocationSceneMapper;
import com.bright.apollo.service.LocationSceneService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月27日  
 *@Version:1.1.0  
 */
@Service
public class LocationSceneServiceImpl implements LocationSceneService{

	@Autowired
	private LocationSceneMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationSceneService#addSceneLocation(com.bright.apollo.common.entity.TLocationScene)  
	 */
	@Override
	public int addSceneLocation(TLocationScene tLocationScene) {
 		return mapper.addSceneLocation(tLocationScene);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationSceneService#deleteSceneLocation(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public void deleteSceneLocation(Integer sceneNumber, Integer location) {
		mapper.deleteSceneLocation(sceneNumber,location);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.LocationSceneService#queryLocationSceneByUserNameAndSceneName()  
	 */
	@Override
	public TLocationScene queryLocationSceneByUserNameAndSceneName(String userName, Integer sceneNumber) {
		 
		return mapper.queryLocationSceneByUserNameAndSceneName(userName,sceneNumber);
	}
}
