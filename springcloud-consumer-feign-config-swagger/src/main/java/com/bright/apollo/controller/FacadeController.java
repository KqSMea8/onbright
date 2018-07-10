package com.bright.apollo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.dto.OboxResp.Type;
import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.enums.ConditionTypeEnum;
import com.bright.apollo.enums.DeviceTypeEnum;
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
import com.bright.apollo.response.DevcieCount;
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
@Controller
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
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resObox.getData() == null) {
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
					} else {
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
			if (oboxRes == null || oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| oboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}
			// may will add respone return status and serialId
			ResponseObject<OboxResp> resSet = feignAliClient.setDeviceStatus(tOboxDeviceConfig.getOboxSerialId(),
					status,tOboxDeviceConfig.getDeviceRfAddr());
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
			} else {
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
			if (sceneRes == null || sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| sceneRes.getData() == null || sceneRes.getData().getScene() == null) {
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
					// rewrite
					/*
					 * ResponseObject updateScene =
					 * feignSceneClient.updateScene(sceneNumber, sceneInfo); if
					 * (updateScene == null || updateScene.getStatus() !=
					 * ResponseEnum.SelectSuccess.getStatus()) {
					 * res.setStatus(ResponseEnum.MicroServiceUnConnection.
					 * getStatus());
					 * res.setMessage(ResponseEnum.MicroServiceUnConnection.
					 * getMsg()); return res; } ResponseObject resp =
					 * feignAliClient.controlServerScene(sceneNumber); if (resp
					 * != null && resp.getStatus() ==
					 * ResponseEnum.SelectSuccess.getStatus()) { return resp; }
					 * scene.setSceneRun((byte) 0); sceneInfo.setScene(scene);
					 * 
					 * feignSceneClient.updateScene(sceneNumber, sceneInfo);
					 */
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
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resObox.getData() == null) {
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
					} else {
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
			@RequestParam(required = false, value = "serialId") String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resObox.getData() == null) {
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
					} else {
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
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resObox.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				if (StringUtils.isEmpty(principal.getUsername())) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				int countOfDevice = 0;
				ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
				if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
					TUser tUser = resUser.getData();
					ResponseObject<List<TOboxDeviceConfig>> resDevice = feignDeviceClient
							.getOboxDeviceConfigByUserId(tUser.getId());
					if (resDevice == null || resDevice.getStatus() != ResponseEnum.SelectSuccess.getStatus()
							|| resDevice.getData() != null) {
						countOfDevice++;
					} else {
						List<TOboxDeviceConfig> list = resDevice.getData();
						countOfDevice = list.size() + 1;
					}
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
					} else {
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
			if (resObox == null || resObox.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resObox.getData() == null) {
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
					} else {
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
			} else {
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
			} else {
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
			} else {
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

	@RequestMapping(value = "/obox", method = RequestMethod.GET)
	public ResponseObject<List<TObox>> getOboxByUser() {
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
				return feignOboxClient.getOboxByUser(tUser.getId());

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

	@ApiOperation(value = "get device by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/device/{deviceType}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDevice(@PathVariable(required = false) String deviceType) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (StringUtils.isEmpty(deviceType))
					res = feignDeviceClient.getDeviceByUser(tUser.getId());
				else
					res = feignDeviceClient.getDevciesByUserIdAndType(tUser.getId(), deviceType);

			} else {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
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
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "add local scene ", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/addLocalScene", method = RequestMethod.POST)
	public ResponseObject addLocalScene(@RequestBody(required = true) SceneDTO sceneDTO) {
		ResponseObject res = new ResponseObject();
		try {
			String sceneName = sceneDTO.getSceneName();
			String sceneType = sceneDTO.getSceneType();
			Byte msgAlter = sceneDTO.getMsgAlter();
			String sceneGroup = sceneDTO.getSceneGroup();
			Integer sceneNumber = null;
			TScene tScene = new TScene();
			if (msgAlter != null) {
				tScene.setMessageAlter(msgAlter);
			}
			if (!StringUtils.isEmpty(sceneGroup)) {
				tScene.setSceneGroup(sceneGroup);
			}
			if (!StringUtils.isEmpty(sceneName) && !StringUtils.isEmpty(sceneType)
					&& !sceneType.equals(SceneTypeEnum.server.getValue())) {
				String oboxSerialId = sceneDTO.getOboxSerialId();
				if (StringUtils.isEmpty(oboxSerialId)) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				ResponseObject<TObox> oboxRes = feignOboxClient.getObox(oboxSerialId);
				if (oboxRes == null || oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
						|| oboxRes.getData() == null) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				ResponseObject<OboxResp> addLocalSceneRes = feignAliClient.addLocalScene(sceneName, oboxSerialId,
						sceneGroup);
				if (addLocalSceneRes == null || addLocalSceneRes.getStatus() != ResponseEnum.AddSuccess.getStatus()
						|| addLocalSceneRes.getData() == null || addLocalSceneRes.getData().getType() != Type.success) {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
					return res;
				}
				String data = addLocalSceneRes.getData().getData();
				if (data.substring(0, 2).equals("00")) {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
					return res;
				}
				sceneNumber = Integer.parseInt(data.substring(6, 8), 16);
				tScene.setOboxSceneNumber(sceneNumber);
				tScene.setSceneName(sceneName);
				tScene.setSceneType(sceneType);
				List<List<SceneConditionDTO>> sceneConditionDTOs = sceneDTO.getConditions();
				if (sceneConditionDTOs != null && sceneConditionDTOs.size() > 0) {
					// 判断是否同一个obox
					if (isNotCommonObox(sceneConditionDTOs, oboxSerialId)) {
						res.setStatus(ResponseEnum.RequestParamError.getStatus());
						res.setMessage(ResponseEnum.RequestParamError.getMsg());
						return res;
					}
					feignAliClient.addLocalSceneCondition(sceneNumber, oboxRes.getData().getOboxSerialId(),
							sceneConditionDTOs);
				}
				List<SceneActionDTO> tActionDTOs = sceneDTO.getActions();
				List<SceneActionDTO> nodeActionDTOs = new ArrayList<SceneActionDTO>();
				List<SceneActionDTO> cameraActionDTOs = new ArrayList<SceneActionDTO>();
				List<SceneActionDTO> nvrActionDTOs = new ArrayList<SceneActionDTO>();
				if (tActionDTOs != null) {
					for (SceneActionDTO sceneActionDTO : tActionDTOs) {
						if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.camera.getValue())) {
							cameraActionDTOs.add(sceneActionDTO);
						} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.nvr.getValue())) {
							nvrActionDTOs.add(sceneActionDTO);
						} else {
							nodeActionDTOs.add(sceneActionDTO);
						}
					}
					feignAliClient.addLocalSceneAction(nodeActionDTOs, sceneNumber,
							oboxRes.getData().getOboxSerialId());
					// new Thread(new sceneAction(nodeActionDTOs, sceneNumber,
				}
				TScene dbScene = null;
				if (dbScene == null) {
					ResponseObject<TScene> dbSceneRes = feignSceneClient
							.getScenesByOboxSerialIdAndSceneNumber(oboxSerialId, sceneNumber);
					if (dbSceneRes != null)
						dbScene = dbSceneRes.getData();
				}
				// camamera action
				for (SceneActionDTO sceneActionDTO : cameraActionDTOs) {
					TYSCamera tysCamera = null;
					ResponseObject<TYSCamera> ysCameraRes = feignDeviceClient
							.getYSCameraBySerialId(sceneActionDTO.getDeviceSerialId());
					if (ysCameraRes != null)
						tysCamera = ysCameraRes.getData();
					// TYSCamera tysCamera = CameraBusiness
					// .queryYSCameraBySerialId(sceneActionDTO
					// .getDeviceSerialId());
					if (tysCamera != null) {
						TSceneAction tSceneAction = new TSceneAction();
						tSceneAction.setAction(sceneActionDTO.getAction());
						tSceneAction.setSceneNumber(dbScene.getSceneNumber());
						tSceneAction.setActionid(tysCamera.getDeviceserial());
						tSceneAction.setNodeType(NodeTypeEnum.camera.getValue());
						feignSceneClient.addSceneAction(tSceneAction);
						// SceneBusiness.addSceneAction(tSceneAction);
					}
				}

				for (SceneActionDTO sceneActionDTO : nvrActionDTOs) {
					ResponseObject<TNvr> nvrRes = feignDeviceClient.getNvrByIP(sceneActionDTO.getDeviceSerialId());
					TNvr tNvr = null;
					if (nvrRes != null)
						tNvr = nvrRes.getData();
					// TNvr tNvr = NvrBusiness.queryNvrByIP(sceneActionDTO
					// .getDeviceSerialId());
					if (tNvr != null) {
						TSceneAction tSceneAction = new TSceneAction();
						tSceneAction.setAction(sceneActionDTO.getAction());
						tSceneAction.setSceneNumber(dbScene.getSceneNumber());
						tSceneAction.setActionid(tNvr.getId().intValue() + "");
						tSceneAction.setNodeType(NodeTypeEnum.nvr.getValue());
						feignSceneClient.addSceneAction(tSceneAction);
						// SceneBusiness.addSceneAction(tSceneAction);
					}
				}

				if (sceneType.equals("02") || sceneType.equals("03")) {
					dbScene.setSceneType(sceneType);
					feignSceneClient.updateScene(dbScene);
					// SceneBusiness.updateScene(dbScene);
				}

				// if (UserWeightEnum.ADMIN.getValue().equals(weight)) {
				// TUserScene tUserScene = new TUserScene();
				// tUserScene.setSceneNumber(dbScene.getSceneNumber());
				// tUserScene.setUserId(Integer.parseInt(uid));
				// SceneBusiness.addUserScene(tUserScene);
				// }

				/*
				 * jsonObject .addProperty("scene_number",
				 * dbScene.getSceneNumber());
				 * jsonObject.addProperty("obox_scene_number",
				 * dbScene.getOboxSceneNumber());
				 * jsonObject.addProperty("obox_serial_id", oboxSerialId);
				 */
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
			} else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "add server scene ", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/addServerScene", method = RequestMethod.POST)
	public ResponseObject addServerScene(@RequestBody(required = true) SceneDTO sceneDTO) {
		ResponseObject res = new ResponseObject();
		try {
			logger.info("====add server scene====");
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			TUser tUser = resUser.getData();
			String sceneName = sceneDTO.getSceneName();
			String sceneType = sceneDTO.getSceneType();
			Byte msgAlter = sceneDTO.getMsgAlter();
			String sceneGroup = sceneDTO.getSceneGroup();
			if (StringUtils.isEmpty(sceneName)) {
				logger.error("====sceneName can't be null====");
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (StringUtils.isEmpty(sceneType)) {
				sceneType = "00";
			}
			TScene tScene = new TScene();
			if (msgAlter != null) {
				tScene.setMessageAlter(msgAlter);
			}
			if (!StringUtils.isEmpty(sceneGroup)) {
				tScene.setSceneGroup(sceneGroup);
			}
			tScene.setSceneName(sceneName);
			tScene.setSceneType(sceneType);
			tScene.setSceneType(sceneType);
			tScene.setSceneStatus(sceneDTO.getSceneStatus());
			ResponseObject<TScene> sceneRes = feignSceneClient.addScene(tScene);
			if (sceneRes == null || sceneRes.getData() == null
					|| sceneRes.getStatus() != ResponseEnum.AddSuccess.getStatus()) {
				res.setStatus(ResponseEnum.AddObjError.getStatus());
				res.setMessage(ResponseEnum.AddObjError.getMsg());
				return res;
			}
			int ret = sceneRes.getData().getSceneNumber();
			// add user scene
			TUserScene tUserScene = new TUserScene();
			tUserScene.setSceneNumber(ret);
			tUserScene.setUserId(tUser.getId());
			feignUserClient.addUserScene(tUserScene);
			List<SceneActionDTO> tActionDTOs = sceneDTO.getActions();
			if (tActionDTOs != null) {
				logger.info("====create server add scene action====");
				for (SceneActionDTO sceneActionDTO : tActionDTOs) {
					TSceneAction tSceneAction = new TSceneAction();
					tSceneAction.setAction(sceneActionDTO.getAction());
					tSceneAction.setSceneNumber(ret);
					logger.info("====nodeType:" + sceneActionDTO.getNodeType());
					if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.group.getValue())) {
						// remove group
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.single.getValue())) {
						ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
								.getDevice(sceneActionDTO.getDeviceSerialId());
						if (deviceRes != null && deviceRes.getData() != null
								&& deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
							tSceneAction.setActionid(deviceRes.getData().getDeviceSerialId());
							tSceneAction.setNodeType(NodeTypeEnum.single.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
						}
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.camera.getValue())) {
						ResponseObject<TYSCamera> ysCameraRes = feignDeviceClient
								.getYSCameraBySerialId(sceneActionDTO.getDeviceSerialId());
						if (ysCameraRes != null && ysCameraRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								&& ysCameraRes.getData() != null) {
							tSceneAction.setActionid(ysCameraRes.getData().getDeviceserial());
							tSceneAction.setNodeType(NodeTypeEnum.camera.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
						}
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.status.getValue())) {
						ResponseObject<TScene> scRes = feignSceneClient
								.getSceneBySceneNumber(sceneActionDTO.getSceneNumber());
						if (scRes != null && scRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
								&& scRes.getData() != null) {
							tSceneAction.setActionid(scRes.getData().getSceneNumber().intValue()+"");
							tSceneAction.setNodeType(NodeTypeEnum.status.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
						}
					}else if (sceneActionDTO.getNodeType().equals(
							NodeTypeEnum.nvr.getValue())) {
						ResponseObject<TNvr> nvrRes = feignDeviceClient.getNvrByIP(sceneActionDTO
								.getDeviceSerialId());
						if (nvrRes!=null&&nvrRes.getData()!=null&&nvrRes.getStatus()!= ResponseEnum.SelectSuccess.getStatus()) {
							tSceneAction.setActionid(nvrRes.getData().getId().intValue()+"");
							tSceneAction.setNodeType(NodeTypeEnum.nvr
									.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
						}
					} else if (sceneActionDTO.getNodeType().equals(
							NodeTypeEnum.security.getValue())) {
						
					}
				}
			}
			List<List<SceneConditionDTO>> sceneConditionDTOs = sceneDTO
					.getConditions();
			if (sceneConditionDTOs != null) {
				logger.info("====create server add scene condition====");
				for (int i = 0; i < sceneConditionDTOs.size(); i++) {
					List<SceneConditionDTO> list = sceneConditionDTOs
							.get(i);
					for (SceneConditionDTO sceneConditionDTO : list) {
						if (!sceneConditionDTO.getConditionType().equals(
								ConditionTypeEnum.time.getValue())
								&& !sceneConditionDTO.getConditionType()
										.equals(ConditionTypeEnum.quartz
												.getValue())) {
							if (sceneConditionDTO.getDeviceSerialId() != null) {
								
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "add obox ", httpMethod = "POST", produces = "application/json;charset=UTF-8")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/addObox", method = RequestMethod.POST)
	public ResponseObject addObox(@RequestBody(required = true) OboxDTO oboxDTO) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
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
				ResponseObject<TUserObox> userOboxRes=feignUserClient.getUserObox(resUser.getData().getId(),oboxRes.getData().getOboxSerialId());
				ResponseObject resobj =null;
				if(userOboxRes!=null&&userOboxRes.getData()==null)
					resobj=feignUserClient.addUserObox(tUserObox);
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
						if (resCondition == null
								|| resCondition.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
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
						oboxDeviceConfig.setOboxId(tobox.getId());
						oboxDeviceConfig.setOboxSerialId(tobox.getOboxSerialId());
						ResponseObject<TOboxDeviceConfig> resDevice = feignDeviceClient
								.addDevice(oboxDeviceConfig.getDeviceSerialId(), oboxDeviceConfig);
						if (resDevice != null && resDevice.getStatus() == ResponseEnum.AddSuccess.getStatus()
								&& resDevice.getData() != null) {
							ResponseObject<TUserDevice> userDeviceRes =feignUserClient.getUserDevcieByUserIdAndSerialId(resUser.getData().getId(),oboxDeviceConfig.getDeviceSerialId());
							if(userDeviceRes==null||userDeviceRes.getData()==null){
								TUserDevice tUserDevice = new TUserDevice();
								tUserDevice.setDeviceSerialId(oboxDeviceConfig.getDeviceSerialId());
								tUserDevice.setUserId(resUser.getData().getId());
								feignUserClient.addUserDevice(tUserDevice);
							}
							
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
						// SceneBusiness.addUserScene(tUserScene);

						List<SceneActionDTO> tActionDTOs = sceneDTO.getActions();
						if (tActionDTOs != null) {
							for (SceneActionDTO sceneActionDTO : tActionDTOs) {
								TSceneAction tSceneAction = new TSceneAction();
								tSceneAction.setAction(sceneActionDTO.getAction());
								tSceneAction.setSceneNumber(sceneRes.getData().getSceneNumber());

								String node_type = sceneActionDTO.getNodeType();
								if (node_type.equals(NodeTypeEnum.group.getValue())) {
									/*
									 * // group //the ali server has no group
									 * TServerOboxGroup tServerOboxGroup =
									 * DeviceBusiness.queryOBOXGroupByAddr(
									 * dbObox.getOboxSerialId(),
									 * sceneActionDTO.getOboxGroupAddr()); if
									 * (tServerOboxGroup != null) { TServerGroup
									 * tServerGroup = DeviceBusiness
									 * .querySererGroupById(tServerOboxGroup.
									 * getServerId());
									 * 
									 * if (tServerGroup != null) {
									 * tSceneAction.setActionID(tServerGroup.
									 * getId());
									 * tSceneAction.setNodeType(NodeTypeEnum.
									 * group.getValue()); } }
									 */} else if (node_type.equals(NodeTypeEnum.single.getValue())) {
									ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
											.getDevice(sceneActionDTO.getDeviceSerialId());
									// TOboxDeviceConfig tOboxDeviceConfig =
									// DeviceBusiness
									// .queryDeviceConfigBySerialID(sceneActionDTO.getDeviceSerialId());
									if (deviceRes != null
											|| deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
											|| deviceRes.getData() != null) {
										tSceneAction.setActionid(deviceRes.getData().getDeviceSerialId());
										// tSceneAction.setActionID(deviceRes.getData().getId());
									}
									// if (tOboxDeviceConfig != null) {
									// tSceneAction.setActionID(tOboxDeviceConfig.getId());
									// }
								}
								feignSceneClient.addSceneAction(tSceneAction);
								// SceneBusiness.addSceneAction(tSceneAction);
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
										TOboxDeviceConfig tOboxDeviceConfig = feignDeviceClient
												.getDevice(sceneConditionDTO.getDeviceSerialId()).getData();
										// TOboxDeviceConfig tOboxDeviceConfig =
										// DeviceBusiness
										// .queryDeviceConfigBySerialID(sceneConditionDTO.getDeviceSerialId());
										if (tOboxDeviceConfig != null) {
											TSceneCondition tSceneCondition = new TSceneCondition();
											tSceneCondition.setSerialid(serialID);
											// tSceneCondition.setSerialId(serialID);
											tSceneCondition.setCond(condition);
											// tSceneCondition.setCondition(condition);
											tSceneCondition.setSceneNumber(sceneRes.getData().getSceneNumber());
											tSceneCondition.setConditionGroup(i);
											feignSceneClient.addSceneCondition(tSceneCondition);
											// SceneBusiness.addSceneCondition(tSceneCondition);
										}

									} else {
										// time condition
										String cond = sceneConditionDTO.getCondition();
										TSceneCondition tSceneCondition = new TSceneCondition();
										tSceneCondition.setCond(cond);
										// tSceneCondition.setCondition(cond);
										tSceneCondition.setConditionGroup(i);
										// tSceneCondition.setSceneNumber(ret);
										tSceneCondition.setSceneNumber(sceneRes.getData().getSceneNumber());
										feignSceneClient.addSceneCondition(tSceneCondition);
										// SceneBusiness.addSceneCondition(tSceneCondition);
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

	@ApiOperation(value = "regist Ali device,the type is required,the zone isn't required", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/registAliDev/{type}", method = RequestMethod.GET)
	public ResponseObject<AliDevInfo> registAliDev(@PathVariable(required = true, value = "type") String type,
			@RequestParam(value = "zone", required = false) String zone) {
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

	@ApiOperation(value = "query device count", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/queryDevcieCount", method = RequestMethod.GET)
	public ResponseObject<List<DevcieCount>> queryDevcieCount() {
		ResponseObject<List<DevcieCount>> res = new ResponseObject<List<DevcieCount>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			List<DevcieCount> tCounts = new ArrayList<DevcieCount>();
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getStatus() == ResponseEnum.SelectSuccess.getStatus() && resUser.getData() != null) {
				logger.info("====userId:"+resUser.getData().getId()+"===userName:"+principal.getUsername());
				ResponseObject<List<TOboxDeviceConfig>> resDevices = feignDeviceClient
						.getDeviceTypeByUser(resUser.getData().getId());
				if (resDevices != null && resDevices.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						|| resDevices.getData() != null) {
					List<TOboxDeviceConfig> list = resDevices.getData();
					for (TOboxDeviceConfig tOboxDeviceConfig : list) {
						ResponseObject<List<TOboxDeviceConfig>> deviceConfigs = feignDeviceClient
								.getDevciesByUserIdAndType(resUser.getData().getId(),
										tOboxDeviceConfig.getDeviceType());
						if (deviceConfigs != null && deviceConfigs.getData() != null) {
							DevcieCount count = new DevcieCount();
							count.setType(tOboxDeviceConfig.getDeviceType());
							count.setCount(deviceConfigs.getData().size());
							tCounts.add(count);
						}
					}
					ResponseObject<List<TObox>> oboxRes = feignOboxClient.getOboxByUser(resUser.getData().getId());
					if (oboxRes != null && oboxRes.getData() != null) {
						DevcieCount count = new DevcieCount();
						count.setType(DeviceTypeEnum.obox.getValue());
						count.setCount(oboxRes.getData().size());
						tCounts.add(count);
					}
					/*
					 * TCount cameraCount =
					 * CameraBusiness.queryYSCameraCountByLicense(user.
					 * getLicense()); if (cameraCount != null) { JsonObject
					 * object = new JsonObject(); object.addProperty("type",
					 * DeviceTypeEnum.camera.getValue());
					 * object.addProperty("count", cameraCount.getCount());
					 * devices.add(object); }
					 */
					// ResponseObject<List<TYSCamera>> cameraRes =
					// feignDeviceClient.getYSCameraByUserId(resUser.getData().getId());
					res.setStatus(ResponseEnum.SelectSuccess.getStatus());
					res.setMessage(ResponseEnum.SelectSuccess.getMsg());

					res.setData(tCounts);
				}
			} else {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param value
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query device count", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/getDeviceByObox/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByObox(
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			return feignDeviceClient.getDevicesByOboxSerialId(oboxSerialId);

		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param value
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "getSearchNewDevice", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/getSearchNewDevice/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<List<Map<String, String>>> getSearchNewDevice(
			@PathVariable(value = "oboxSerialId") String oboxSerialId) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
		try {
			// return feignDeviceClient.getDevicesByOboxSerialId(oboxSerialId);
			// TObox dbObox =
			// OboxBusiness.queryOboxsByOboxSerialId(obox_serial_id);
			ResponseObject<TObox> oboxRes = feignOboxClient.getObox(oboxSerialId);
			if (oboxRes == null || oboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				ResponseObject<List<Map<String, String>>> resList = feignAliClient
						.getSearchNewDevice(oboxRes.getData());
				if (resList != null && resList.getStatus() == ResponseEnum.UpdateSuccess.getStatus()) {
					res.setStatus(ResponseEnum.SelectSuccess.getStatus());
					res.setMessage(ResponseEnum.SelectSuccess.getMsg());
					res.setData(resList.getData());
				} else {
					res.setStatus(ResponseEnum.SelectSuccess.getStatus());
					res.setMessage(ResponseEnum.SelectSuccess.getMsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param sceneConditionDTOs
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	private boolean isNotCommonObox(List<List<SceneConditionDTO>> sceneConditionDTOs, String oboxSerialId) {
		for (int i = 0; i < sceneConditionDTOs.size(); i++) {
			List<SceneConditionDTO> list = sceneConditionDTOs.get(i);
			if (list != null && list.size() > 0) {
				for (SceneConditionDTO conditionDTO : list) {
					if (conditionDTO.getConditionType().equals(ConditionTypeEnum.device.getValue())
							&& !conditionDTO.getOboxSerialId().equals(oboxSerialId)) {
						if (!conditionDTO.getOboxSerialId().equals(oboxSerialId))
							return true;
					}

				}
			}
		}
		return false;
	}

	private String extractPathFromPattern(final HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	}

}
