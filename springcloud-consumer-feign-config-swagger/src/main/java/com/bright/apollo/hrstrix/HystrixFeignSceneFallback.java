package com.bright.apollo.hrstrix;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;

@Component
public class HystrixFeignSceneFallback extends BasicHystrixFeignFallback implements FeignSceneClient {
	private Logger logger = Logger.getLogger(getClass());

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
	@Override
	public ResponseObject<SceneInfo> updateScene(Integer sceneNumber, SceneInfo info) {
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
	public ResponseObject<SceneInfo> addScene(SceneInfo info) {
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

	@Override
	public ResponseObject deleteSceneByOboxSerialId(String oboxSerialId) {
		logger.warn("===scene server is break===");
		return serverError();
	}

}