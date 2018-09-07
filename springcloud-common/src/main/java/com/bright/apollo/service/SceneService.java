package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.response.SceneInfo;
import com.bright.apollo.service.base.BasicService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月2日  
 *@Version:1.1.0  
 */
public interface SceneService extends BasicService{
	public int updateScene(TScene tScene);
	
	public void deleteSceneBySceneNumber(Integer sceneNumber);
	/**  
	 * @param scene  
	 * @Description:  
	 */
	public int addScene(TScene scene);
	/**  
	 * @param <T>
	 * @param list  
	 * @Description:  
	 */
	@Deprecated
	public <T> void batchAdd(List<T> list);
	/**  
	 * @param sceneNumber
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	public SceneInfo querySceneInfoBySceneNumber(Integer sceneNumber);
	/**
	 * @param list  
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	public List<TSceneAction> queryActionsBySceneNumber(Integer sceneNumber);
	/**
	 * @param list  
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	public List<TSceneCondition> queryConditionsBySceneNumber(Integer sceneNumber);
	/**  
	 * @param <T>
	 * @param conditions  
	 * @Description:  
	 */
	@Deprecated
	public <T> void batchUpdate(List<T> list);
	/**  
	 * @param condtionId  
	 * @Description:  
	 */
	@Deprecated
	public int deleteSceneConditionById(Integer condtionId);
	/**  
	 * @param sceneNumber  
	 * @Description:  
	 */
	public void deleteSceneConditionBySceneNumber(Integer sceneNumber);
	/**  
	 * @param actionId  
	 * @Description:  
	 */
	@Deprecated
	public int deleteSceneActionById(Integer actionId);
	/**  
	 * @param sceneNumber  
	 * @Description:  
	 */
	public void deleteSceneActionBySceneNumber(Integer sceneNumber);
	/**  
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	public List<TScene> querySceneByUserId(Integer userId, Integer pageIndex, Integer pageSize);
	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	public int queryCountSceneByUserId(Integer userId);

	List<TScene> getSceneByOboxSerialId(String oboxSerialId);

	void deleteSceneBySceneNum(int sceneNumber);

	void deleteSceneByOboxSerialId(String oboxSerialId);

	TScene getTSceneByOboxSerialIdAndSceneNumber(String oboxSerialId,int sceneNumber);

	void deleteSceneByOboxSerialIdAndSceneNum(String oboxSerialId,int oboxSceneNumber);

	TScene getSceneBySceneNumber(int sceneNumber);

	List<TScene> getALlScene();
	/**  
	 * @param sceneNumber
	 * @return  
	 * @Description:  
	 */
	List<TSceneCondition> getConditionsBySceneNumber(Integer sceneNumber);

	/**  
	 * @param tSceneAction  
	 * @Description:  
	 */
	int addSceneAction(TSceneAction tSceneAction);

	/**  
	 * @param tSceneCondition  
	 * @Description:  
	 */
	int addSceneCondition(TSceneCondition tSceneCondition);

	/**  
	 * @param sceneNumber
	 * @param conditionGroup
	 * @return  
	 * @Description:  
	 */
	public List<TSceneCondition> getSceneConditionBySceneNumberAndGroup(Integer sceneNumber,
			Integer conditionGroup);

	/**  
	 * @param sceneNumber
	 * @return  
	 * @Description:  
	 */
	public List<TSceneAction> getSceneActionsBySceneNumber(Integer sceneNumber);

	/**  
	 * @param tSceneAction  
	 * @Description:  
	 */
	public void updateSceneAction(TSceneAction tSceneAction);

	/**  
	 * @param sceneNumber
	 * @param actionId  
	 * @Description:  
	 */
	public void deleteSceneActionBySceneNumberAndActionId(Integer sceneNumber, String actionId);

	/**  
	 * @param oboxSerialId
	 * @param oboxSceneNumber
	 * @return  
	 * @Description:  
	 */
	public TScene getTSceneByOboxSerialIdAndOboxSceneNumber(String oboxSerialId, Integer oboxSceneNumber);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	public List<TScene> getSceneByUserId(Integer userId);

	/**  
	 * @param userId
	 * @param start
	 * @param count
	 * @return  
	 * @Description:  
	 */
	public List<TScene> getSceneByUserIdAndPage(Integer userId, int start, int count);

	/**  
	 * @param serialId
	 * @param nodeType
	 * @return  
	 * @Description:  
	 */
	public int deleteSceneActionByActionId(String serialId, String nodeType);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	public int deleteSceneConditionBySerialId(String serialId);
 
}
