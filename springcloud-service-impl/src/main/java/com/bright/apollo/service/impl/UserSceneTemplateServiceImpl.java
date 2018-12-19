package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TUserSceneTemplate;
import com.bright.apollo.dao.scene.mapper.TUserSceneTemplateMapper;
import com.bright.apollo.service.UserSceneTemplateService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月19日  
 *@Version:1.1.0  
 */
@Service
public class UserSceneTemplateServiceImpl implements UserSceneTemplateService{
	@Autowired
	private TUserSceneTemplateMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserSceneTemplateService#addTemplate(com.bright.apollo.common.entity.TUserSceneTemplate)  
	 */
	@Override
	public void addTemplate(TUserSceneTemplate tUserSceneTemplate) {
		mapper.addTemplate(tUserSceneTemplate);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserSceneTemplateService#queryUserSceneTemplateByUserIdAndModelName(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public List<TUserSceneTemplate> queryUserSceneTemplateByUserIdAndModelName(Integer userId, String modelName) {
		 
		return mapper.queryUserSceneTemplateByUserIdAndModelName(userId,modelName);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserSceneTemplateService#updateTemplate(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public void updateTemplate(Integer userId, String modelName, String sceneModel, String deviceModel) {
		mapper.updateTemplate(userId,modelName,sceneModel,deviceModel);  
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserSceneTemplateService#deleteTemplate(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public void deleteTemplate(Integer userId, String modelName) {
		mapper.deleteTemplate(userId,modelName);  
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserSceneTemplateService#queryTemplateByUserId(java.lang.Integer)  
	 */
	@Override
	public List<TUserSceneTemplate> queryTemplateByUserId(Integer userId) {
		  
		return mapper.queryTemplateByUserId(userId);
	}
}
