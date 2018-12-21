package com.bright.apollo.service;

import com.bright.apollo.common.entity.TLocationScene;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月27日  
 *@Version:1.1.0  
 */
public interface LocationSceneService {

	/**  
	 * @param tLocationScene  
	 * @Description:  
	 */
	int addSceneLocation(TLocationScene tLocationScene);

	/**  
	 * @param sceneNumber
	 * @param location  
	 * @Description:  
	 */
	void deleteSceneLocation(Integer sceneNumber, Integer location);

	/**  
	 * @param sceneNumber 
	 * @param userName 
	 * @return  
	 * @Description:  
	 */
	TLocationScene queryLocationSceneByUserNameAndSceneName(String userName, Integer sceneNumber);

}
