package com.bright.apollo.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				res.setStatus(ResponseEnum.SelectSuccess.getStatus());
				res.setMessage(ResponseEnum.SelectSuccess.getMsg());
				res.setData(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	// get list by page

	@RequestMapping(value = "/updateScene", method = RequestMethod.PUT)
	public ResponseObject<TScene> updateScene(@RequestBody(required=true) TScene scene) {
		ResponseObject<TScene> res = new ResponseObject<TScene>();
		try {
			if(scene==null||scene.getSceneNumber()==null||scene.getSceneNumber()==0){
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}else{
				if(sceneService.getSceneBySceneNumber(scene.getSceneNumber())==null){
					res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
					res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				}else{
					sceneService.updateScene(scene);
					res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
					res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
					res.setData(sceneService.getSceneBySceneNumber(scene.getSceneNumber()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
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
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				sceneService.deleteSceneBySceneNumber(sceneNumber);
				res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
				res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// modify the code
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteSceneCondition/{sceneNumber}/{condtionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneCondition(@PathVariable Integer sceneNumber, @PathVariable Integer condtionId) {
		ResponseObject res = new ResponseObject();
		try {
			if (condtionId != null && condtionId != 0) {
				TSceneCondition tSceneCondition = sceneService.queryTById(new TSceneCondition(condtionId));
				if (tSceneCondition == null) {
					res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
					res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					sceneService.deleteSceneConditionById(condtionId);
					res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
					res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
				}
			} else {
				// delete all
				if (sceneNumber == null || sceneNumber == 0) {
					res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
					res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					TScene tScene = sceneService.queryTById(new TScene(sceneNumber));
					if (tScene == null) {
						res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
						res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
					} else {
						sceneService.deleteSceneConditionBySceneNumber(sceneNumber);
						res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
						res.setMessage(ResponseEnum.DeleteSuccess.getMsg());

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
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
					res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
					res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					sceneService.deleteSceneActionById(actionId);
					res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
					res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
				}
			} else {
				// delete all
				TScene tScene = sceneService.queryTById(new TScene(sceneNumber));
				if (tScene == null) {
					res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
					res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				} else {
					sceneService.deleteSceneActionBySceneNumber(sceneNumber);
					res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
					res.setMessage(ResponseEnum.DeleteSuccess.getMsg());

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/addSceneInfo", method = RequestMethod.POST)
	public ResponseObject<SceneInfo> addSceneInfo(@RequestBody(required = true) SceneInfo info) {
		ResponseObject<SceneInfo> res = new ResponseObject<SceneInfo>();
		try {
			TScene scene = info.getScene();
			SceneInfo sceneInfo = new SceneInfo();
			if (scene.getSceneNumber() != null && scene.getSceneNumber().intValue() != 0) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
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
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	// list scene
	@RequestMapping(value = "/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<SceneInfo>> getSceneByUserAndPage(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize) {
		ResponseObject<List<SceneInfo>> res = new ResponseObject<List<SceneInfo>>();
		try {
			if (userId == 0) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (pageIndex == null)
				pageIndex = 0;
			if (pageSize == null || pageSize <= 0)
				pageSize = 10;
			List<TScene> list = sceneService.querySceneByUserId(userId, pageIndex, pageSize);
			if (list == null || list.size() <= 0) {
				res.setStatus(ResponseEnum.SearchIsEmpty.getStatus());
				res.setMessage(ResponseEnum.SearchIsEmpty.getMsg());
			} else {
				int count = sceneService.queryCountSceneByUserId(userId);
				List<SceneInfo> infos = new ArrayList<SceneInfo>();
				for (TScene scene : list) {
					SceneInfo info = new SceneInfo();
					info.setScene(scene);
					List<TSceneAction> actions = sceneService.queryActionsBySceneNumber(scene.getSceneNumber());
					info.setActions(actions);
					List<TSceneCondition> conditions = sceneService
							.queryConditionsBySceneNumber(scene.getSceneNumber());
					info.setConditions(conditions);
					infos.add(info);
				}
				res.setStatus(ResponseEnum.SelectSuccess.getStatus());
				res.setMessage(ResponseEnum.SelectSuccess.getMsg());
				res.setData(infos);
				res.setPageSize(pageSize);
				res.setPageIndex(pageIndex);
				res.setPageCount((count / pageSize + (count % pageSize == 0 ? 0 : 1)));
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/addLocalScene", method = RequestMethod.POST)
	public ResponseObject<SceneInfo> addLocalScene(@RequestBody(required = true) SceneInfo info) {
		ResponseObject<SceneInfo> res = new ResponseObject<SceneInfo>();
		try {
			if (!StringUtils.isEmpty(info.getScene().getOboxSerialId())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getScenesByOboxSerialId/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<List<TScene>> getScenesByOboxSerialId(
			@PathVariable(value = "oboxSerialId") String oboxSerialId) {
		ResponseObject<List<TScene>> res = new ResponseObject<List<TScene>>();
		try {
			List<TScene> list = sceneService.getSceneByOboxSerialId(oboxSerialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getSceneConditionsBySceneNumber/{sceneNumber}", method = RequestMethod.GET)
	public ResponseObject<List<TSceneCondition>> getSceneConditionsBySceneNumber(
			@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject<List<TSceneCondition>> res = new ResponseObject<List<TSceneCondition>>();
		try {
			List<TSceneCondition> list = sceneService.getConditionsBySceneNumber(sceneNumber);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteSceneConditionBySceneNumber/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneConditionBySceneNumber(@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			sceneService.deleteSceneConditionBySceneNumber(sceneNumber);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteScenesBySceneNumber/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject deleteScenesBySceneNumber(@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			sceneService.deleteSceneActionBySceneNumber(sceneNumber);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteSceneByOboxSerialId/{oboxSerialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneByOboxSerialId(@PathVariable(value = "oboxSerialId") String oboxSerialId) {
		ResponseObject res = new ResponseObject();
		try {
			sceneService.deleteSceneByOboxSerialId(oboxSerialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/addScene", method = RequestMethod.POST)
	public ResponseObject<TScene> addScene(@RequestBody(required = true) TScene scene) {
		ResponseObject<TScene> res = new ResponseObject<TScene>();
		try {
			if (scene.getSceneNumber() != null || scene.getSceneNumber() == 0) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			} else {
				int sceneNumber = sceneService.addScene(scene);
				if (sceneNumber == 0) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
				} else {
					scene.setSceneNumber(sceneNumber);
					res.setData(scene);
					res.setStatus(ResponseEnum.AddSuccess.getStatus());
					res.setMessage(ResponseEnum.AddSuccess.getMsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===addScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addSceneAction", method = RequestMethod.POST)
	public ResponseObject addSceneAction(@RequestBody(required = true) TSceneAction tSceneAction) {
		ResponseObject res = new ResponseObject();
		try {
			if (tSceneAction != null && (tSceneAction.getSceneNumber() == null 
					|| tSceneAction.getSceneNumber() == 0)) {
				sceneService.addSceneAction(tSceneAction);
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
			} else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===addSceneAction error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addSceneCondition", method = RequestMethod.POST)
	public ResponseObject addSceneCondition(@RequestBody(required = true) TSceneCondition tSceneCondition) {
		ResponseObject res = new ResponseObject();
		try {
			if (tSceneCondition != null && (tSceneCondition.getSceneNumber() == null 
					|| tSceneCondition.getSceneNumber() == 0)) {
				sceneService.addSceneCondition(tSceneCondition);
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
			} else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===addSceneAction error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/getScenesByOboxSerialIdAndSceneNumber/{oboxSerialId}/{sceneNumber}", method = RequestMethod.GET)
	public ResponseObject<TScene> getScenesByOboxSerialIdAndSceneNumber(
			@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject<TScene> res = new ResponseObject<TScene>();
		try {
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(sceneService.getTSceneByOboxSerialIdAndSceneNumber(oboxSerialId, sceneNumber));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScenesByOboxSerialIdAndSceneNumber error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/getSceneBySceneNumber/{sceneNumber}", method = RequestMethod.GET)
	public ResponseObject<TScene> getScenesByOboxSerialIdAndSceneNumber(
			@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject<TScene> res = new ResponseObject<TScene>();
		try {
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(sceneService.getSceneBySceneNumber(sceneNumber));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScenesByOboxSerialIdAndSceneNumber error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/getSceneConditionsBySceneNumberAndConditionGroup/{sceneNumber}/{conditionGroup}", method = RequestMethod.GET)
	ResponseObject<List<TSceneCondition>> getSceneConditionsBySceneNumberAndConditionGroup(@PathVariable(value = "sceneNumber")Integer sceneNumber,
			@PathVariable(value = "conditionGroup")Integer conditionGroup
			){
		ResponseObject<List<TSceneCondition>> res = new ResponseObject<List<TSceneCondition>>();
		try {
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(sceneService.getSceneConditionBySceneNumberAndGroup(sceneNumber,conditionGroup));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getSceneConditionsBySceneNumberAndConditionGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/getSceneActionsBySceneNumber/{sceneNumber}", method = RequestMethod.GET)
	ResponseObject<List<TSceneAction>> getSceneActionsBySceneNumber(@PathVariable(value = "sceneNumber")Integer sceneNumber
			){
		ResponseObject<List<TSceneAction>> res = new ResponseObject<List<TSceneAction>>();
		try {
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(sceneService.getSceneActionsBySceneNumber(sceneNumber));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getSceneConditionsBySceneNumberAndConditionGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
