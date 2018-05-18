package com.bright.apollo.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;
import com.bright.apollo.service.SceneService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@Controller
@RequestMapping("scene")
@RestController
public class SceneController {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SceneService sceneService;

	@RequestMapping(value = "/{sceneNumber}", method = RequestMethod.GET)
	public ResponseObject<SceneInfo> getScene(@PathVariable Integer sceneNumber) {
		ResponseObject<SceneInfo> res = new ResponseObject<SceneInfo>();
		try {
			SceneInfo info = sceneService.querySceneInfoBySceneNumber(sceneNumber);
			if (info == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
				res.setData(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	// get list by page

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/{sceneNumber}", method = RequestMethod.PUT)
	public ResponseObject updateScene(@PathVariable(required = true) Integer sceneNumber,
			@RequestBody(required = true) SceneInfo info) {
		ResponseObject res = new ResponseObject();
		try {
			TScene scene = info.getScene();
			if (scene != null)
				sceneService.updateScene(scene);
			List<TSceneCondition> conditions = info.getConditions();
			if (conditions != null && !conditions.isEmpty())
				sceneService.batchUpdate(conditions);
			List<TSceneAction> actions = info.getActions();
			if (actions != null && !actions.isEmpty())
				sceneService.batchUpdate(actions);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject deleteScene(@PathVariable Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			TScene tScene = sceneService.queryTById(new TScene(sceneNumber));
			if (tScene == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				sceneService.deleteSceneBySceneNumber(sceneNumber);
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteSceneCondition/{sceneNumber}/{condtionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneCondition(@PathVariable Integer sceneNumber, @PathVariable Integer condtionId) {
		ResponseObject res = new ResponseObject();
		try {
			if (condtionId != null && condtionId != 0) {
				TSceneCondition tSceneCondition = sceneService.queryTById(new TSceneCondition(condtionId));
				if (tSceneCondition == null) {
					res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
					res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					sceneService.deleteSceneConditionById(condtionId);
					res.setCode(ResponseEnum.Success.getCode());
					res.setMsg(ResponseEnum.Success.getMsg());
				}
			} else {
				// delete all
				if (sceneNumber == null || sceneNumber == 0) {
					res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
					res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					TScene tScene = sceneService.queryTById(new TScene(sceneNumber));
					if (tScene == null) {
						res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
						res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
					} else {
						sceneService.deleteSceneConditionBySceneNumber(sceneNumber);
						res.setCode(ResponseEnum.Success.getCode());
						res.setMsg(ResponseEnum.Success.getMsg());

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteSceneAction/{sceneNumber}/{actionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneAction(@PathVariable Integer sceneNumber, @PathVariable Integer actionId) {
		ResponseObject res = new ResponseObject();
		try {
			if (actionId != null && actionId != 0) {
				TSceneAction tSceneAction = sceneService.queryTById(new TSceneAction(actionId));
				if (tSceneAction == null) {
					res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
					res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					sceneService.deleteSceneActionById(actionId);
					res.setCode(ResponseEnum.Success.getCode());
					res.setMsg(ResponseEnum.Success.getMsg());
				}
			} else {
				// delete all
				TScene tScene = sceneService.queryTById(new TScene(sceneNumber));
				if (tScene == null) {
					res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
					res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					sceneService.deleteSceneActionBySceneNumber(sceneNumber);
					res.setCode(ResponseEnum.Success.getCode());
					res.setMsg(ResponseEnum.Success.getMsg());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/addScene", method = RequestMethod.POST)
	public ResponseObject<SceneInfo> addScene(@RequestBody(required = true) SceneInfo info) {
		ResponseObject<SceneInfo> res = new ResponseObject<SceneInfo>();
		try {
			TScene scene = info.getScene();
			SceneInfo sceneInfo = new SceneInfo();
			if (scene.getSceneNumber() != null && scene.getSceneNumber().intValue() != 0) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			scene.setSceneNumber(null);
			scene.setLastOpTime(null);
			int sceneNumber = sceneService.addScene(scene);
			scene.setSceneNumber(sceneNumber);
			sceneInfo.setScene(scene);
			// 不做设备校验
			List<TSceneAction> actions = info.getActions();
			if (actions != null && !actions.isEmpty()) {
				List<TSceneAction> list = new ArrayList<TSceneAction>();
				for (TSceneAction action : actions) {
					if (action != null) {
						action.setSceneNumber(sceneNumber);
						list.add(action);
					}
				}
				sceneService.batchAdd(list);
				sceneInfo.setActions(list);
			}
			List<TSceneCondition> conditions = info.getConditions();
			if (conditions != null && !conditions.isEmpty()) {
				List<TSceneCondition> list = new ArrayList<TSceneCondition>();
				for (TSceneCondition condition : conditions) {
					if (condition != null) {
						condition.setSceneNumber(sceneNumber);
						list.add(condition);
					}
				}
				sceneService.batchAdd(list);
				sceneInfo.setConditions(list);
			}
			res.setData(sceneInfo);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	// list scene
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<SceneInfo>> getSceneByUserAndPage(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize) {
		ResponseObject<List<SceneInfo>> res = new ResponseObject<List<SceneInfo>>();
		try {
			if (userId == 0) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (pageIndex == null)
				pageIndex = 0;
			if (pageSize == null || pageSize <= 0)
				pageSize = 10;
			List<TScene> list = sceneService.querySceneByUserId(userId, pageIndex, pageSize);
			if (list == null || list.size() <= 0) {
				res.setCode(ResponseEnum.SearchIsEmpty.getCode());
				res.setMsg(ResponseEnum.SearchIsEmpty.getMsg());
			} else {
				int count = sceneService.queryCountSceneByUserId(userId);
				List<SceneInfo> infos=new ArrayList<SceneInfo>();
				for(TScene scene:list){
					SceneInfo info=new SceneInfo();
					info.setScene(scene);
					List<TSceneAction> actions = sceneService.queryActionsBySceneNumber(scene.getSceneNumber());
					info.setActions(actions);
					List<TSceneCondition> conditions = sceneService.queryConditionsBySceneNumber(scene.getSceneNumber());
					info.setConditions(conditions);
					infos.add(info);
				}
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
				res.setData(infos);
				res.setPageSize(pageSize);
				res.setPageIndex(pageIndex);
				res.setPageCount((count / pageSize + (count % pageSize == 0 ? 0 : 1)));
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
