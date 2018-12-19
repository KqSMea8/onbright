package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TUserSceneTemplate;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月19日  
 *@Version:1.1.0  
 */
public interface UserSceneTemplateService {

	/**  
	 * @param tUserSceneTemplate  
	 * @Description:  
	 */
	void addTemplate(TUserSceneTemplate tUserSceneTemplate);

	/**  
	 * @param userId
	 * @param modelName
	 * @return  
	 * @Description:  
	 */
	List<TUserSceneTemplate> queryUserSceneTemplateByUserIdAndModelName(Integer userId, String modelName);

	/**  
	 * @param userId
	 * @param modelName
	 * @param sceneModel
	 * @param deviceModel  
	 * @Description:  
	 */
	void updateTemplate(Integer userId, String modelName, String sceneModel, String deviceModel);

	/**  
	 * @param userId
	 * @param modelName  
	 * @Description:  
	 */
	void deleteTemplate(Integer userId, String modelName);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	List<TUserSceneTemplate> queryTemplateByUserId(Integer userId);

	 

}
