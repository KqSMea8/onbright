package com.bright.apollo.hystrix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月17日  
 *@Version:1.1.0  
 */
@Component
public class HystrixFeignSceneFallback extends BasicHystrixFeignFallback implements FeignSceneClient {
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignSceneFallback.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignSceneClient#getScene(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<SceneInfo> getScene(Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignSceneClient#updateScene(java.lang.Integer,
	 * com.bright.apollo.response.SceneInfo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TScene> updateScene(TScene scene) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignSceneClient#deleteScene(java.lang.Integer)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteScene(Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignSceneClient#addScene(com.bright.apollo.
	 * response.SceneInfo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<SceneInfo> addSceneInfo(SceneInfo info) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#deleteSceneCondition(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteSceneCondition(Integer sceneNumber, Integer condtionId) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#deleteSceneAction(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public ResponseObject<?> deleteSceneAction(Integer sceneNumber, Integer actionId) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#getSceneByUserAndPage(java.lang.Integer, java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<SceneInfo>> getSceneByUserAndPage(Integer userId, Integer pageIndex, Integer pageSize) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#addLocalScene(com.bright.apollo.response.SceneInfo)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<SceneInfo> addLocalScene(SceneInfo info) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#getScenesByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TScene>> getScenesByOboxSerialId(String oboxSerialId) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#getSceneConditionsBySceneNumber(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TSceneCondition>> getSceneConditionsBySceneNumber(Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#deleteSceneConditionBySceneNumber(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteSceneConditionBySceneNumber(Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#deleteSceneActionsBySceneNumber(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteSceneActionsBySceneNumber(Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}


	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#deleteSceneByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteSceneByOboxSerialId(String oboxSerialId) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#addScene(com.bright.apollo.common.entity.TScene)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TScene> addScene(TScene tScene) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#addSceneAction(com.bright.apollo.common.entity.TSceneAction)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addSceneAction(TSceneAction tSceneAction) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#addSceneCondition(com.bright.apollo.common.entity.TSceneCondition)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addSceneCondition(TSceneCondition tSceneCondition) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#getScenesByOboxSerialIdAndSceneNumber(java.lang.String, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TScene> getScenesByOboxSerialIdAndSceneNumber(String oboxSerialId, Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#getSceneBySceneNumber(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TScene> getSceneBySceneNumber(Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#getSceneConditionsBySceneNumberAndConditionGroup(java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TSceneCondition>> getSceneConditionsBySceneNumberAndConditionGroup(Integer sceneNumber,
			Integer conditionGroup) {
		logger.warn("===scene server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignSceneClient#getSceneActionsBySceneNumber(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TSceneAction>> getSceneActionsBySceneNumber(Integer sceneNumber) {
		logger.warn("===scene server is break===");
		return serverError();
	}
}
