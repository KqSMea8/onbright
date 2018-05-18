package com.bright.apollo.service.impl;

import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import com.bright.apollo.dao.scene.mapper.TSceneMapper;
import org.springframework.stereotype.Service;

//import com.bright.apollo.business.SceneBusiness;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneActionExample;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TSceneConditionExample;
//import com.bright.apollo.common.entity.TSceneExample;
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
//	@SuppressWarnings("rawtypes")
//	@Autowired
//	private SceneBusiness sceneBusiness;

	private TSceneMapper sceneMapper;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.base.BasicService#handlerExample(com.bright.
	 * apollo.business.base.BasicBusiness, java.lang.Object)
	 */
	@Override
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
	public <T, E> List<T> handlerExampleToList(E e) {
//		return sceneBusiness.selectByExample(e);
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
	public <T> T queryTById(T t) {
//		if (t != null)
//			return (T) sceneBusiness.selectByPrimaryKey(t);
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
//		return sceneBusiness.updateByPrimaryKeySelective(tScene);
		return sceneMapper.updateScene(tScene);
//		return 0;
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
	public int deleteSceneBySceneNumber(Integer sceneNumber) {
//		TSceneExample example = new TSceneExample();
//		example.or().andSceneNumberEqualTo(sceneNumber);
//		sceneBusiness.deleteByExample(example);
//		TSceneActionExample actionExample = new TSceneActionExample();
//		actionExample.or().andSceneNumberEqualTo(sceneNumber);
//		sceneBusiness.deleteByExample(actionExample);
//		TSceneConditionExample conditionExample = new TSceneConditionExample();
//		conditionExample.or().andSceneNumberEqualTo(sceneNumber);
//		return sceneBusiness.deleteByExample(conditionExample);
		return 0;
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
//		return sceneBusiness.insertSelective(scene);
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
	public <T> void batchAdd(List<T> list) {
//		sceneBusiness.batchAdd(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.service.SceneService#querySceneInfoBySceneNumber(java.
	 * lang.Integer)
	 */
	@Override
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
	public List<TSceneAction> queryActionsBySceneNumber(Integer sceneNumber) {
		TSceneActionExample example = new TSceneActionExample();
		example.or().andSceneNumberEqualTo(sceneNumber);
//		return sceneBusiness.selectByExample(example);
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
	public List<TSceneCondition> queryConditionsBySceneNumber(Integer sceneNumber) {
		TSceneConditionExample example = new TSceneConditionExample();
		example.or().andSceneNumberEqualTo(sceneNumber);
//		return sceneBusiness.selectByExample(example);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.SceneService#batchUpdate(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> void batchUpdate(List<T> list) {
//		sceneBusiness.batchUpdate(list);
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
	public int deleteSceneConditionById(Integer condtionId) {
//		return sceneBusiness.deleteByPrimaryKey(new TSceneCondition(condtionId));
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
	public int deleteSceneConditionBySceneNumber(Integer sceneNumber) {
		TSceneConditionExample conditionExample = new TSceneConditionExample();
		conditionExample.or().andSceneNumberEqualTo(sceneNumber);
//		return sceneBusiness.deleteByExample(conditionExample);
		return 0;
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
	public int deleteSceneActionById(Integer actionId) {
//		return sceneBusiness.deleteByPrimaryKey(new TSceneAction(actionId));
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
	public int deleteSceneActionBySceneNumber(Integer sceneNumber) {
		TSceneActionExample actionExample = new TSceneActionExample();
		actionExample.or().andSceneNumberEqualTo(sceneNumber);
//		return sceneBusiness.deleteByExample(actionExample);
		return 0;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#querySceneByUserId(java.lang.Integer, java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TScene> querySceneByUserId(Integer userId, Integer pageIndex, Integer pageSize) {
// 		return sceneBusiness.querySceneByUserId(userId,pageIndex*pageSize,(pageIndex+1)*pageSize);
		return null;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.SceneService#queryCountSceneByUserId(java.lang.Integer)  
	 */
	@Override
	public int queryCountSceneByUserId(Integer userId) {
// 		return sceneBusiness.queryCountSceneByUserId(userId);
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
		return sceneMapper.getTSceneByOboxSerialIdAndSceneNumber(oboxSerialId,sceneNumber);
	}

	@Override
	public void deleteSceneByOboxSerialIdAndSceneNum(String oboxSerialId, int oboxSceneNumber) {
		sceneMapper.deleteSceneByOboxSerialIdAndSceneNum(oboxSerialId,oboxSceneNumber);
	}

	@Override
	public TScene getSceneBySceneNumber(int sceneNumber) {
		return sceneMapper.getSceneBySceneNumber(sceneNumber);
	}


}
