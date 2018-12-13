package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.bright.apollo.business.SceneBusiness;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.dao.scene.mapper.TSceneActionMapper;
import com.bright.apollo.dao.scene.mapper.TSceneConditionMapper;
import com.bright.apollo.dao.scene.mapper.TSceneMapper;
import com.bright.apollo.response.SceneInfo;
import com.bright.apollo.service.SceneService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@Service
public class SceneServiceImpl implements SceneService {
	// @SuppressWarnings("rawtypes")
	// @Autowired
	// private SceneBusiness sceneBusiness;
	@Autowired
	private TSceneMapper sceneMapper;
	@Autowired
	private TSceneConditionMapper tSceneConditionMapper;
	@Autowired
	private TSceneActionMapper tSceneActionMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExample(com.bright.
	 * apollo.business.base.BasicBusiness, java.lang.Object)
	 */
	@Override
	@Deprecated
	public <T, E> T handlerExample(E e) {
		List<T> selectByExample = handlerExampleToList(e);
		if (selectByExample != null && selectByExample.size() > 0)
			return selectByExample.get(0);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExampleToList(com.
	 * bright.apollo.business.base.BasicBusiness, java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	@Deprecated
	public <T, E> List<T> handlerExampleToList(E e) {
		// return sceneBusiness.selectByExample(e);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#queryTById(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public <T> T queryTById(T t) {
		// if (t != null)
		// return (T) sceneBusiness.selectByPrimaryKey(t);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#updateScene(com.bright.apollo.
	 * common.entity.TScene)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int updateScene(TScene tScene) {
		// return sceneBusiness.updateByPrimaryKeySelective(tScene);
		return sceneMapper.updateScene(tScene);
		// return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#deleteSceneBySceneNumber(java.lang
	 * .Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteSceneBySceneNumber(Integer sceneNumber) {
		sceneMapper.deleteSceneBySceneNum(sceneNumber);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#addScene(com.bright.apollo.common.
	 * entity.TScene)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int addScene(TScene scene) {
		return sceneMapper.addScene(scene);
		// return sceneBusiness.insertSelective(scene);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#batchAddSceneAction(java.util.
	 * List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public <T> void batchAdd(List<T> list) {
		// sceneBusiness.batchAdd(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#querySceneInfoBySceneNumber(java.
	 * lang.Integer)
	 */
	@Override
	@Deprecated
	public SceneInfo querySceneInfoBySceneNumber(Integer sceneNumber) {
		TScene scene = queryTById(new TScene(sceneNumber));
		if (scene == null)
			return null;
		SceneInfo info = new SceneInfo();
		info.setScene(scene);
		List<TSceneAction> actions = queryActionsBySceneNumber(sceneNumber);
		if (actions != null && !actions.isEmpty())
			info.setActions(actions);
		List<TSceneCondition> conditions = queryConditionsBySceneNumber(sceneNumber);
		if (conditions != null && !conditions.isEmpty())
			info.setConditions(conditions);
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#queryActionsBySceneNumber(java.
	 * lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TSceneAction> queryActionsBySceneNumber(Integer sceneNumber) {
	 
		// return sceneBusiness.selectByExample(example);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#queryConditionsBySceneNumber(java.
	 * lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TSceneCondition> queryConditionsBySceneNumber(Integer sceneNumber) {
 
		// return sceneBusiness.selectByExample(example);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.SceneService#batchUpdate(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public <T> void batchUpdate(List<T> list) {
		// sceneBusiness.batchUpdate(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#deleteSceneConditionById(java.lang
	 * .Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int deleteSceneConditionById(Integer condtionId) {
		// return sceneBusiness.deleteByPrimaryKey(new
		// TSceneCondition(condtionId));
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#deleteSceneConditionBySceneNumber(
	 * java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteSceneConditionBySceneNumber(Integer sceneNumber) {
		tSceneConditionMapper.deleteSceneConditionBySceneNum(sceneNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#deleteSceneActionById(java.lang.
	 * Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public int deleteSceneActionById(Integer actionId) {
		// return sceneBusiness.deleteByPrimaryKey(new TSceneAction(actionId));
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#deleteSceneActionBySceneNumber(
	 * java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteSceneActionBySceneNumber(Integer sceneNumber) {
		tSceneActionMapper.deleteSceneActionBySceneNumber(sceneNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.SceneService#querySceneByUserId(java.lang.
	 * Integer, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public List<TScene> querySceneByUserId(Integer userId, Integer pageIndex, Integer pageSize) {
		// return
		// sceneBusiness.querySceneByUserId(userId,pageIndex*pageSize,(pageIndex+1)*pageSize);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#queryCountSceneByUserId(java.lang.
	 * Integer)
	 */
	@Override
	@Deprecated
	public int queryCountSceneByUserId(Integer userId) {
		// return sceneBusiness.queryCountSceneByUserId(userId);
		return 0;
	}

	@Override
	public List<TScene> getSceneByOboxSerialId(String oboxSerialId) {
		return sceneMapper.getSceneByOboxSerialId(oboxSerialId);
	}

	@Override
	public void deleteSceneBySceneNum(int sceneNumber) {
		sceneMapper.deleteSceneBySceneNum(sceneNumber);
	}

	@Override
	public void deleteSceneByOboxSerialId(String oboxSerialId) {
		sceneMapper.deleteSceneByOboxSerialId(oboxSerialId);
	}

	@Override
	public TScene getTSceneByOboxSerialIdAndSceneNumber(String oboxSerialId, int sceneNumber) {
		return sceneMapper.getTSceneByOboxSerialIdAndSceneNumber(oboxSerialId, sceneNumber);
	}

	@Override
	public void deleteSceneByOboxSerialIdAndSceneNum(String oboxSerialId, int oboxSceneNumber) {
		sceneMapper.deleteSceneByOboxSerialIdAndSceneNum(oboxSerialId, oboxSceneNumber);
	}

	@Override
	public TScene getSceneBySceneNumber(int sceneNumber) {
		return sceneMapper.getSceneBySceneNumber(sceneNumber);
	}

	@Override
	public List<TScene> getALlScene() {
		return sceneMapper.getALlScene();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#getConditionsBySceneNumber(java.
	 * lang.Integer)
	 */
	@Override
	public List<TSceneCondition> getConditionsBySceneNumber(Integer sceneNumber) {

		return tSceneConditionMapper.getConditionsBySceneNumber(sceneNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#addSceneAction(com.bright.apollo.
	 * common.entity.TSceneAction)
	 */
	@Override
	public int addSceneAction(TSceneAction tSceneAction) {
		return tSceneActionMapper.addSceneAction(tSceneAction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.SceneService#addSceneCondition(com.bright.
	 * apollo.common.entity.TSceneCondition)
	 */
	@Override
	public int addSceneCondition(TSceneCondition tSceneCondition) {
		return tSceneConditionMapper.addSceneCondition(tSceneCondition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.SceneService#
	 * getSceneConditionsBySceneNumberAndConditionGroup(java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public List<TSceneCondition> getSceneConditionBySceneNumberAndGroup(Integer sceneNumber,
			Integer conditionGroup) {
		return tSceneConditionMapper.getSceneConditionBySceneNumberAndGroup(sceneNumber, conditionGroup);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#getSceneActionsBySceneNumber(java.lang.Integer)  
	 */
	@Override
	public List<TSceneAction> getSceneActionsBySceneNumber(Integer sceneNumber) {
		
		return tSceneActionMapper.getSceneActionBySceneNumber(sceneNumber);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#updateSceneAction(com.bright.apollo.common.entity.TSceneAction)  
	 */
	@Override
	public void updateSceneAction(TSceneAction tSceneAction) {
		tSceneActionMapper.updateSceneAction(tSceneAction);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#deleteSceneActionBySceneNumberAndActionId(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public void deleteSceneActionBySceneNumberAndActionId(Integer sceneNumber, String actionId) {
		  
		tSceneActionMapper.deleteSceneActionByBySceneNumberAndActionId(sceneNumber, actionId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#getTSceneByOboxSerialIdAndOboxSceneNumber(java.lang.String, java.lang.Integer)  
	 */
	@Override
	public TScene getTSceneByOboxSerialIdAndOboxSceneNumber(String oboxSerialId, Integer oboxSceneNumber) {
		 
		return sceneMapper.getTSceneByOboxSerialIdAndOboxSceneNumber(oboxSerialId,oboxSceneNumber);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#getSceneByUserId(java.lang.Integer)  
	 */
	@Override
	public List<TScene> getSceneByUserId(Integer userId) {
  
		return sceneMapper.getSceneByUserId(userId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#getSceneByUserIdAndPage(java.lang.Integer, int, int)  
	 */
	@Override
	public List<TScene> getSceneByUserIdAndPage(Integer userId, int start, int count) {
		 
		return sceneMapper.getSceneByUserIdAndPage(userId,start,count);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#deleteSceneActionByActionId(java.lang.String, java.lang.String)  
	 */
	@Override
	public int deleteSceneActionByActionId(String serialId, String nodeType) {
		return tSceneActionMapper.deleteSceneActionByActionId(serialId,nodeType);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#deleteSceneConditionBySerialId(java.lang.String)  
	 */
	@Override
	public int deleteSceneConditionBySerialId(String serialId) {
		 
		return tSceneConditionMapper.deleteSceneConditionBySerialId(serialId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#querySceneByLocation(java.lang.Integer)  
	 */
	@Override
	public List<TScene> querySceneByLocation(Integer location) {
 		return sceneMapper.querySceneByLocation(location);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#querySceneBySceneNumberAndUserId(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public TScene querySceneBySceneNumberAndUserId(Integer sceneNumber, Integer userId) {
		 
		return sceneMapper.querySceneBySceneNumberAndUserId(sceneNumber,userId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#deleteSceneConfitionByOboxSerialId(java.lang.String)  
	 */
	@Override
	public void deleteSceneConfitionByOboxSerialId(String oboxSerialId) {
		tSceneConditionMapper.deleteSceneConfitionByOboxSerialId(oboxSerialId);  
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#deleteSceneActionByOboxSerialId(java.lang.String)  
	 */
	@Override
	public void deleteSceneActionByOboxSerialId(String oboxSerialId) {
		tSceneActionMapper.deleteSceneActionByOboxSerialId(oboxSerialId);
		
	}

}
