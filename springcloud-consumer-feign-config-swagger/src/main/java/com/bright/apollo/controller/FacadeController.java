package com.bright.apollo.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HttpServletBean;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.dto.OboxResp.Type;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.enums.SceneTypeEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.request.OboxDTO;
import com.bright.apollo.request.SceneActionDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.request.SceneDTO;
import com.bright.apollo.response.AliDevInfo;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:the write operation use over two Microservice ,we cound't ensure
 *                  data consistency.
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Api("facade Controller")
@RestController
@RequestMapping("facade")
public class FacadeController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private FeignOboxClient feignOboxClient;
	@Autowired
	private FeignUserClient feignUserClient;
	@Autowired
	private FeignDeviceClient feignDeviceClient;
	@Autowired
	private FeignSceneClient feignSceneClient;
	@Autowired
	private FeignAliClient feignAliClient;

	// release obox
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "release  obox", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/release/{oboxSerialId}", method = RequestMethod.DELETE)
	public ResponseObject releaseDevice(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		ResponseObject res = new ResponseObject();
		try {
			// query obox if exist
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus() || resObox.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				// send cmd or use a facade to send the order to ali
				ResponseObject<OboxResp> releaseObox = feignAliClient.releaseObox(oboxSerialId);
				if (releaseObox != null && releaseObox.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setStatus(ResponseEnum.SendOboxFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
							res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}else{
						res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
						res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
					}
				} else {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// control device the
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "control  device", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseObject controlDevice(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestParam(required = true, value = "status") String status) {
		ResponseObject res = new ResponseObject();
		try {
			// the device and obox is exist
			ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient.getDevice(serialId);
			if (deviceRes == null || deviceRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| deviceRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}
			TOboxDeviceConfig tOboxDeviceConfig = deviceRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getObox(tOboxDeviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus() || oboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}
			// may will add respone return status and serialId
			ResponseObject<OboxResp> resSet = feignAliClient.setDeviceStatus(tOboxDeviceConfig.getOboxSerialId(),
					status);
			if (resSet == null || resSet.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				return resSet;
			}
			OboxResp oboxResp = resSet.getData();
			if (oboxResp.getType() != Type.success) {
				if (oboxResp.getType() == Type.obox_process_failure || oboxResp.getType() == Type.socket_write_error) {
					res.setStatus(ResponseEnum.SendOboxFail.getStatus());
					res.setMessage(ResponseEnum.SendOboxFail.getMsg());
				} else if (oboxResp.getType() == Type.reply_timeout) {
					res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
					res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
				} else {
					res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus());
					res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
				}
			}else{
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
 
	// control scene
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "control  scene,the scene must is a server scene ", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{sceneNumber}", method = RequestMethod.PUT)
	public ResponseObject controlScene(@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<SceneInfo> sceneRes = feignSceneClient.getScene(sceneNumber);
			if (sceneRes == null || sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus() || sceneRes.getData() == null
					|| sceneRes.getData().getScene() == null) {
				res.setStatus(ResponseEnum.ServerError.getStatus());
				res.setMessage(ResponseEnum.ServerError.getMsg());
				return res;
			} else {
				TScene scene = sceneRes.getData().getScene();
				if (!scene.getSceneType().equals(SceneTypeEnum.server.getValue())) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				if (scene.getSceneRun() == 0) {
					// old code
					scene.setSceneRun((byte) 1);
					SceneInfo sceneInfo = sceneRes.getData();
					sceneInfo.setScene(scene);
					ResponseObject updateScene = feignSceneClient.updateScene(sceneNumber, sceneInfo);
					if (updateScene == null || updateScene.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
						res.setStatus(ResponseEnum.MicroServiceUnConnection.getStatus());
						res.setMessage(ResponseEnum.MicroServiceUnConnection.getMsg());
						return res;
					}
					ResponseObject resp = feignAliClient.controlServerScene(sceneNumber);
					if (resp != null && resp.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
						return resp;
					}
					scene.setSceneRun((byte) 0);
					sceneInfo.setScene(scene);
					feignSceneClient.updateScene(sceneNumber, sceneInfo);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// ====================================search
	// device==========================================
	// stop scan old code state is 00 ，取消搜索设备
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 00", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/scan/{oboxSerialId}", method = RequestMethod.DELETE)
	public ResponseObject stopScan(@PathVariable(required = true) String oboxSerialId) {
		// modify the request param
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus() || resObox.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				ResponseObject<OboxResp> scanObox = feignAliClient.stopScan(oboxSerialId);
				if (scanObox != null && scanObox.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& scanObox.getData() != null) {
					OboxResp oboxResp = scanObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setStatus(ResponseEnum.SendOboxFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
							res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}else{
						res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
						res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
					}
				} else {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// search/scan 设备重新上电
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 01", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/scan/restart/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject searchDevicesByOldStyle(
			@PathVariable(value = "oboxSerialId", required = true) String oboxSerialId,
			@RequestParam(required = false, value = "deviceType") String deviceType,
			@RequestParam(required = false, value = "deviceChildType") String deviceChildType,
			@RequestParam(required = false, value = "deviceChildType") String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus() || resObox.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				ResponseObject<OboxResp> releaseObox = feignAliClient.scanByRestart(oboxSerialId, deviceType,
						deviceChildType, serialId);
				if (releaseObox != null && releaseObox.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setStatus(ResponseEnum.SendOboxFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
							res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}else{
						res.setStatus(ResponseEnum.AddSuccess.getStatus());
						res.setMessage(ResponseEnum.AddSuccess.getMsg());
					}
				} else {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// search/scan 设备不重新上电搜索
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 02", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/scan/unStop/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject searchDevicesByNewStyle(
			@PathVariable(value = "oboxSerialId", required = true) String oboxSerialId,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "deviceChildType", required = false) String deviceChildType,
			@RequestParam(value = "serialId", required = false) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus() || resObox.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				if (principal.getUsername() != null && !principal.getUsername().equals("")) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
				}
				int countOfDevice = 0;
				ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
				if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
					TUser tUser = resUser.getData();
					ResponseObject<List<TOboxDeviceConfig>> resDevice = feignDeviceClient
							.getOboxDeviceConfigByUserId(tUser.getId());
					if (resDevice == null || resDevice.getStatus() != ResponseEnum.SelectSuccess.getStatus()
							|| resDevice.getData() != null) {
						res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
						res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
						return res;
					}
					List<TOboxDeviceConfig> list = resDevice.getData();
					countOfDevice = list.size() + 1;
				} else {
					res.setStatus(ResponseEnum.UnKonwUser.getStatus());
					res.setMessage(ResponseEnum.UnKonwUser.getMsg());
					return res;
				}
				// search device by user
				ResponseObject<OboxResp> releaseObox = feignAliClient.scanByUnStop(oboxSerialId, deviceType,
						deviceChildType, serialId, countOfDevice);
				if (releaseObox != null && releaseObox.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setStatus(ResponseEnum.SendOboxFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
							res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}else{
						res.setStatus(ResponseEnum.AddSuccess.getStatus());
						res.setMessage(ResponseEnum.AddSuccess.getMsg());
					}
				} else {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// search/scan 03 自动搜索
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 03", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/scan/initiative/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject searchDevicesByInitiative(
			@PathVariable(value = "oboxSerialId", required = true) String oboxSerialId,
			@RequestParam(value = "deviceType", required = false) String deviceType,
			@RequestParam(value = "deviceChildType", required = false) String deviceChildType,
			@RequestParam(value = "serialId", required = false) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus() || resObox.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				if (principal.getUsername() != null && !principal.getUsername().equals("")) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
				}
				int countOfDevice = 0;
				ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
				if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
					TUser tUser = resUser.getData();
					ResponseObject<List<TOboxDeviceConfig>> resDevice = feignDeviceClient
							.getOboxDeviceConfigByUserId(tUser.getId());
					if (resDevice == null || resDevice.getStatus() != ResponseEnum.SelectSuccess.getStatus()
							|| resDevice.getData() != null) {
						res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
						res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
						return res;
					}
					List<TOboxDeviceConfig> list = resDevice.getData();
					countOfDevice = list.size() + 1;
				} else {
					res.setStatus(ResponseEnum.UnKonwUser.getStatus());
					res.setMessage(ResponseEnum.UnKonwUser.getMsg());
					return res;
				}
				// search device by user
				ResponseObject<OboxResp> releaseObox = feignAliClient.scanByInitiative(oboxSerialId, deviceType,
						deviceChildType, serialId, countOfDevice);
				if (releaseObox != null && releaseObox.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setStatus(ResponseEnum.SendOboxFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
							res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus());
							res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}else{
						res.setStatus(ResponseEnum.AddSuccess.getStatus());
						res.setMessage(ResponseEnum.AddSuccess.getMsg());
					}
				} else {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// ===================================================================
	// delete obox
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "delete  obox", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/obox/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delObox(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> oboxRes = feignOboxClient.deleteObox(serialId);
			if (oboxRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
				// cound't ensure this operations all SelectSuccess
				feignUserClient.deleteUserOboxByOboxSerialId(serialId);
				feignDeviceClient.deleleDeviceByOboxSerialId(serialId);
			}else{
				res.setStatus(ResponseEnum.MicroServiceUnConnection.getStatus());
				res.setMessage(ResponseEnum.MicroServiceUnConnection.getMsg());
				return res;
			}
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// delete device
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "delete  device", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delDevice(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject resDevice = feignDeviceClient.delDevice(serialId);
			if (resDevice.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
				feignUserClient.deleteUserDeviceBySerialId(serialId);
			}else{
				res.setStatus(ResponseEnum.MicroServiceUnConnection.getStatus());
				res.setMessage(ResponseEnum.MicroServiceUnConnection.getMsg());			
				return res;
			}
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());		
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// delete scene
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "delete  scene", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject delScene(@PathVariable(required = true) Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject resScene = feignSceneClient.deleteScene(sceneNumber);
			if (resScene.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
				feignUserClient.deleteUserSceneBySceneNumber(sceneNumber);
			}else{
				res.setStatus(ResponseEnum.MicroServiceUnConnection.getStatus());
				res.setMessage(ResponseEnum.MicroServiceUnConnection.getMsg());			
				return res;
			}
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());	
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// query obox by page
	@ApiOperation(value = "get obox by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/obox/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TObox>> getOboxByUserAndPage(
			@PathVariable(required = true, value = "pageIndex") Integer pageIndex,
			@PathVariable(required = true, value = "pageSize") Integer pageSize) {
		ResponseObject<List<TObox>> res = new ResponseObject<List<TObox>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getUsername() != null && !principal.getUsername().equals("")) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignOboxClient.getOboxByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// query device by page
	@ApiOperation(value = "get device by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/device/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(@PathVariable Integer pageIndex,
			@PathVariable Integer pageSize) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getUsername() != null && !principal.getUsername().equals("")) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignDeviceClient.getDeviceByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "get scene by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<SceneInfo>> getSceneByUserAndPage(@PathVariable Integer pageIndex,
			@PathVariable Integer pageSize) {
		ResponseObject<List<SceneInfo>> res = new ResponseObject<List<SceneInfo>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getUsername() != null && !principal.getUsername().equals("")) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignSceneClient.getSceneByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// ======add local scene
	@ApiOperation(value = "add local scene ", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/addLocalScene", method = RequestMethod.POST)
	public ResponseObject<SceneInfo> addLocalScene(@RequestBody(required = true) SceneInfo info) {
		ResponseObject<SceneInfo> res = new ResponseObject<SceneInfo>();
		try {
			if (!StringUtils.isEmpty(info.getScene().getOboxSerialId())
					|| !StringUtils.isEmpty(info.getScene().getSceneName())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TObox> tOboxRes = feignOboxClient.getObox(info.getScene().getOboxSerialId());
			if (tOboxRes == null || tOboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| tOboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<OboxResp> oboxRespone = feignAliClient.addLocalScene(info.getScene().getSceneName(),
					info.getScene().getOboxSerialId(), info.getScene().getSceneGroup());
			if (oboxRespone != null && oboxRespone.getStatus() == ResponseEnum.SelectSuccess.getStatus()
					&& oboxRespone.getData() != null) {
				OboxResp oboxResp = oboxRespone.getData();
				if (oboxResp.getType() != Type.success) {
					if (oboxResp.getType() == Type.obox_process_failure
							|| oboxResp.getType() == Type.socket_write_error) {
						res.setStatus(ResponseEnum.SendOboxFail.getStatus());
						res.setMessage(ResponseEnum.SendOboxFail.getMsg());
					} else if (oboxResp.getType() == Type.reply_timeout) {
						res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
						res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
					} else {
						res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus());
						res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
					}
					return res;
				}
			}
			info.getScene().setSceneNumber(Integer.parseInt(oboxRespone.getData().toString().substring(6, 8), 16));
			List<TSceneCondition> conditions = info.getConditions();
			// check sceneCondition oboxSerialId
			if (isNotCommonObox(conditions, info.getScene().getOboxSerialId())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			oboxRespone = feignAliClient.addLocalSceneCondition(info.getScene().getSceneNumber(), info.getConditions());
			return feignSceneClient.addLocalScene(info);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<SceneInfo>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}
	 
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "add obox ", httpMethod = "POST", produces = "application/json;charset=UTF-8")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/addObox", method = RequestMethod.POST)
	public ResponseObject<TObox> addObox(@RequestBody(required = true) OboxDTO oboxDTO) {
		ResponseObject<TObox> res = new ResponseObject<TObox>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getUsername() != null && !principal.getUsername().equals("")) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
				ResponseObject<TObox> oboxRes = feignOboxClient.getObox(oboxDTO.getOboxSerialId());
				if (oboxRes == null || oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
						|| oboxRes.getData() == null) {
					res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
					res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
					return res;
				}
				if (oboxRes.getData().getOboxStatus() != (byte) 1) {
					// return
					// respError(ErrorEnum.request_fail_not_online.getValue());
					res.setStatus(ResponseEnum.REQUESTFAILNOTONLINE.getStatus());
					res.setMessage(ResponseEnum.REQUESTFAILNOTONLINE.getMsg());
					return res;
				}
				TUserObox tUserObox = new TUserObox();
				tUserObox.setUserId(resUser.getData().getId());
				tUserObox.setOboxSerialId(oboxRes.getData().getOboxSerialId());
				ResponseObject resobj = feignUserClient.addUserObox(tUserObox);
				TObox tobox = oboxRes.getData();
				if (oboxDTO.getOboxName() != null) {
					tobox.setOboxName(oboxDTO.getOboxName());
				}
				if (oboxDTO.getOboxVersion() != null) {
					tobox.setOboxVersion(oboxDTO.getOboxVersion());
				}
				tobox.setOboxControl((byte) 0);
				feignOboxClient.updateObox(tobox.getOboxSerialId(), tobox);
				ResponseObject<List<TOboxDeviceConfig>> resList = feignDeviceClient
						.getDevicesByOboxSerialId(tobox.getOboxSerialId());
				if (resList == null || resList.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
					res.setStatus(ResponseEnum.Error.getStatus());
					res.setMessage(ResponseEnum.Error.getMsg());
					return res;
				}
				List<TOboxDeviceConfig> deviceList = resList.getData();
				if (deviceList != null && deviceList.size() > 0) {
					for (TOboxDeviceConfig tOboxDeviceConfig : deviceList) {
						/*
						 * if (!tOboxDeviceConfig.getGroupAddr().equals("00")) {
						 * TServerOboxGroup tServerOboxGroup =
						 * DeviceBusiness.queryOBOXGroupByAddr(
						 * tOboxDeviceConfig.getOboxSerialId(),
						 * tOboxDeviceConfig.getGroupAddr()); if
						 * (tServerOboxGroup != null) { TServerGroup
						 * tServerGroup = DeviceBusiness
						 * .querySererGroupById(tServerOboxGroup.getServerId());
						 * if (tServerGroup != null) { if
						 * (tServerGroup.getGroupStyle().equals(GroupTypeEnum.
						 * local.getValue())) {
						 * DeviceBusiness.deleteServerGroup(tServerGroup); } }
						 * DeviceBusiness.deleteOBOXGroupByAddr(
						 * tOboxDeviceConfig.getOboxSerialId(),
						 * tOboxDeviceConfig.getGroupAddr()); } }
						 */
						// DeviceBusiness.deleteDeviceGroup(tOboxDeviceConfig.getId());
						feignDeviceClient.delDevice(tOboxDeviceConfig.getDeviceSerialId());
						// DeviceBusiness.deleteUserDeviceByDeviceId(tOboxDeviceConfig.getId());
						// .DeviceBusiness.delDeviceChannel(tOboxDeviceConfig.getId());
					}
				}
				feignDeviceClient.deleleDeviceByOboxSerialId(tobox.getOboxSerialId());
				ResponseObject<List<TScene>> resScenes = feignSceneClient
						.getScenesByOboxSerialId(tobox.getOboxSerialId());
				if (resScenes == null || resScenes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
					res.setStatus(ResponseEnum.Error.getStatus());
					res.setMessage(ResponseEnum.Error.getMsg());
					return res;
				}
				List<TScene> scenes = resScenes.getData();
				if (scenes != null) {
					for (TScene tScene : scenes) {
						ResponseObject<List<TSceneCondition>> resCondition = feignSceneClient
								.getSceneConditionsBySceneNumber(tScene.getSceneNumber());
						if (resCondition == null || resCondition.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
							res.setStatus(ResponseEnum.Error.getStatus());
							res.setMessage(ResponseEnum.Error.getMsg());
							return res;
						}
						List<TSceneCondition> tSceneConditions = resCondition.getData();
						if (tSceneConditions != null) {
							feignSceneClient.deleteSceneConditionBySceneNumber(tScene.getSceneNumber());
							// SceneBusiness.deleteSceneConditionBySceneNumber(tScene.getSceneNumber());
						}
						feignUserClient.deleteUserSceneBySceneNumber(tScene.getSceneNumber());
						// SceneBusiness.deleteUserScene(tScene.getSceneNumber());
						feignSceneClient.deleteSceneActionsBySceneNumber(tScene.getSceneNumber());
						// SceneBusiness.deleteSceneActionsBySceneNumber(tScene.getSceneNumber());
						feignSceneClient.deleteScene(tScene.getSceneNumber());
						// SceneBusiness.deleteSceneBySceneNumber(tScene.getSceneNumber());
						// SceneBusiness.deleteSceneLocationBySceneNumber(tScene.getSceneNumber());
					}
					feignSceneClient.deleteSceneByOboxSerialId(tobox.getOboxSerialId());
					// OboxBusiness.delOboxScenes(dbObox.getOboxSerialId());
				}
				List<TOboxDeviceConfig> oboxDeviceConfigs = oboxDTO.getDeviceConfigs();
				if (oboxDeviceConfigs != null) {
					for (TOboxDeviceConfig oboxDeviceConfig : oboxDeviceConfigs) {
						oboxDeviceConfig.setOboxId(tobox.getOboxId());
						oboxDeviceConfig.setOboxSerialId(tobox.getOboxSerialId());
						ResponseObject<TOboxDeviceConfig> resDevice = feignDeviceClient
								.addDevice(oboxDeviceConfig.getDeviceSerialId(), oboxDeviceConfig);
						if (resDevice != null || resDevice.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								|| resDevice.getData() != null) {
							TUserDevice tUserDevice = new TUserDevice();
							tUserDevice.setDeviceSerialId(oboxDeviceConfig.getDeviceSerialId());
							tUserDevice.setUserId(resUser.getData().getId());
							feignUserClient.addUserDevice(tUserDevice);
						}
						// int ret =
						// OboxBusiness.addOboxConfig(oboxDeviceConfig);
						/*
						 * TDeviceChannel tDeviceChannel = new TDeviceChannel();
						 * tDeviceChannel.setDeviceId(ret);
						 * tDeviceChannel.setOboxId(dbObox.getOboxId());
						 * tDeviceChannel.setSignalIntensity(15);
						 * DeviceBusiness.addDeviceChannel(tDeviceChannel);
						 * DeviceBusiness.addUserDevice(userId, ret);
						 */
					}
				}
				List<SceneDTO> sceneDTOs = oboxDTO.getScenes();
				if (sceneDTOs != null) {
					for (SceneDTO sceneDTO : sceneDTOs) {
						TScene tScene = new TScene();
						tScene.setSceneName(sceneDTO.getSceneName());
						if (!sceneDTO.getSceneType().equals(SceneTypeEnum.local.getValue())) {
							continue;
						}
						tScene.setSceneType(sceneDTO.getSceneType());
						tScene.setSceneStatus(sceneDTO.getSceneStatus());
						tScene.setOboxSceneNumber(sceneDTO.getOboxSceneNumber());
						tScene.setOboxSerialId(tobox.getOboxSerialId());

						/*
						 * if (!StringUtils.isEmpty(sceneDTO.getSceneGroup())) {
						 * tScene.setSceneGroup(sceneDTO.getSceneGroup()); }
						 */

						// waiting for new week
						// int ret = SceneBusiness.addScene(tScene);
						 
						ResponseObject<TScene> sceneRes = feignSceneClient.addScene(tScene);
						if (sceneRes == null || sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
								|| sceneRes.getData() == null) {
							res.setStatus(ResponseEnum.ServerError.getStatus());
							res.setMessage(ResponseEnum.ServerError.getMsg());
							return res;
						}
						TUserScene tUserScene = new TUserScene();
						tUserScene.setSceneNumber(sceneRes.getData().getSceneNumber());
						tUserScene.setUserId(resUser.getData().getId());
						feignUserClient.addUserScene(tUserScene);
						//SceneBusiness.addUserScene(tUserScene);

						List<SceneActionDTO> tActionDTOs = sceneDTO.getActions();
						if (tActionDTOs != null) {
							for (SceneActionDTO sceneActionDTO : tActionDTOs) {
								TSceneAction tSceneAction = new TSceneAction();
								tSceneAction.setAction(sceneActionDTO.getAction());
								tSceneAction.setSceneNumber(sceneRes.getData().getSceneNumber());

								String node_type = sceneActionDTO.getNodeType();
								if (node_type.equals(NodeTypeEnum.group.getValue())) {/*
									// group
									//the ali server has no group
									TServerOboxGroup tServerOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(
											dbObox.getOboxSerialId(), sceneActionDTO.getOboxGroupAddr());
									if (tServerOboxGroup != null) {
										TServerGroup tServerGroup = DeviceBusiness
												.querySererGroupById(tServerOboxGroup.getServerId());

										if (tServerGroup != null) {
											tSceneAction.setActionID(tServerGroup.getId());
											tSceneAction.setNodeType(NodeTypeEnum.group.getValue());
										}
									}
								*/} else if (node_type.equals(NodeTypeEnum.single.getValue())) {
									ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient.getDevice(sceneActionDTO.getDeviceSerialId());
									//TOboxDeviceConfig tOboxDeviceConfig = DeviceBusiness
									//		.queryDeviceConfigBySerialID(sceneActionDTO.getDeviceSerialId());
									if(deviceRes!=null||deviceRes.getStatus()==ResponseEnum.SelectSuccess.getStatus()||deviceRes.getData()!=null){
										tSceneAction.setActionid(deviceRes.getData().getDeviceSerialId());
										//tSceneAction.setActionID(deviceRes.getData().getId());
									}
									//if (tOboxDeviceConfig != null) {
									//	tSceneAction.setActionID(tOboxDeviceConfig.getId());
									//}
								}
								feignSceneClient.addSceneAction(tSceneAction);
 								//SceneBusiness.addSceneAction(tSceneAction);
							}
						}
						List<List<SceneConditionDTO>> sceneConditionDTOs = sceneDTO.getConditions();
						if (sceneConditionDTOs != null) {
							for (int i = 0; i < sceneConditionDTOs.size(); i++) {
								List<SceneConditionDTO> list = sceneConditionDTOs.get(i);
								for (SceneConditionDTO sceneConditionDTO : list) {
									if (sceneConditionDTO.getDeviceSerialId() != null) {
										// node condition
										String condition = sceneConditionDTO.getCondition();
										String serialID = sceneConditionDTO.getDeviceSerialId();
										TOboxDeviceConfig tOboxDeviceConfig = feignDeviceClient.getDevice(sceneConditionDTO.getDeviceSerialId()).getData();
										//TOboxDeviceConfig tOboxDeviceConfig = DeviceBusiness
										//		.queryDeviceConfigBySerialID(sceneConditionDTO.getDeviceSerialId());
										if (tOboxDeviceConfig != null) {
											TSceneCondition tSceneCondition = new TSceneCondition();
											tSceneCondition.setSerialid(serialID);
											//tSceneCondition.setSerialId(serialID);
											tSceneCondition.setCond(condition);
											//tSceneCondition.setCondition(condition);
											tSceneCondition.setSceneNumber(sceneRes.getData().getSceneNumber());
											tSceneCondition.setConditionGroup(i);
											feignSceneClient.addSceneCondition(tSceneCondition);
											//SceneBusiness.addSceneCondition(tSceneCondition);
										}

									} else {
										// time condition
										String cond = sceneConditionDTO.getCondition();
										TSceneCondition tSceneCondition = new TSceneCondition();
										tSceneCondition.setCond(cond);
										//tSceneCondition.setCondition(cond);
										tSceneCondition.setConditionGroup(i);
										//tSceneCondition.setSceneNumber(ret);
										tSceneCondition.setSceneNumber(sceneRes.getData().getSceneNumber());
										feignSceneClient.addSceneCondition(tSceneCondition);
										//SceneBusiness.addSceneCondition(tSceneCondition);
									}
								}
							}
						}
					}
				}
			} else {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "regist Ali device", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/registAliDev/{type}/{zone}", method = RequestMethod.GET)
	public ResponseObject<AliDevInfo> registAliDev(@PathVariable(required = true, value = "type") String type,
			@PathVariable(required = false, value = "zone") String zone) {
		ResponseObject<AliDevInfo> res = new ResponseObject<AliDevInfo>();
		try {
			return feignAliClient.registAliDev(type, zone);
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param conditions
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	private boolean isNotCommonObox(List<TSceneCondition> conditions, String oboxSerialId) {
		for (int i = 0; i < conditions.size(); i++) {
			TSceneCondition tSceneCondition = conditions.get(i);
			if (!StringUtils.isEmpty(tSceneCondition.getSerialid())) {
				ResponseObject<TObox> oboxResp = feignOboxClient.getObox(tSceneCondition.getSerialid());
				if (oboxResp == null || oboxResp.getStatus() != ResponseEnum.SelectSuccess.getStatus()
						|| oboxResp.getData() == null || StringUtils.isEmpty(oboxResp.getData().getOboxSerialId())
						|| !oboxResp.getData().getOboxSerialId().equals(oboxSerialId)) {
					return true;
				}
			}
		}

		return false;
	}
}
