package com.bright.apollo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.dto.OboxResp.Type;
import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserOperation;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.constant.SubTableConstant;
import com.bright.apollo.enums.ConditionTypeEnum;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.enums.IntelligentMaxEnum;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.enums.RemoteUserEnum;
import com.bright.apollo.enums.SceneTypeEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignQuartzClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.request.IntelligentFingerRemoteUserDTO;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
import com.bright.apollo.request.IntelligentFingerWarnItemDTO;
import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.request.OboxDTO;
import com.bright.apollo.request.SceneActionDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.request.SceneDTO;
import com.bright.apollo.request.TIntelligentFingerPushDTO;
import com.bright.apollo.response.AliDevInfo;
import com.bright.apollo.response.DevcieCount;
import com.bright.apollo.response.IntelligentOpenRecordItemDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;
import com.bright.apollo.service.MsgService;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.DateHelper;
import com.bright.apollo.tool.MobileUtil;
import com.bright.apollo.tool.NumberHelper;
import com.google.gson.JsonObject;
import com.zz.common.util.MD5;

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
	public static String salt = "eqcs231@gfdgaqweqxaa4648}{";
	private final long max_waitting_time = 15000l;

	private static final Logger logger = LoggerFactory.getLogger(FacadeController.class);
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
	@Autowired
	private FeignQuartzClient feignQuartzClient;
	@Autowired
	private CmdCache cmdCache;
	@Autowired
	private MsgService msgService;

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
				if (releaseObox != null && releaseObox.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
					res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
					res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
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
					status, tOboxDeviceConfig.getDeviceRfAddr());
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
				if (releaseObox != null && releaseObox.getStatus() == ResponseEnum.AddSuccess.getStatus()) {
					// OboxResp oboxResp = releaseObox.getData();
					/*
					 * if (oboxResp.getType() != Type.success) { if
					 * (oboxResp.getType() == Type.obox_process_failure ||
					 * oboxResp.getType() == Type.socket_write_error) {
					 * res.setStatus(ResponseEnum.SendOboxFail.getStatus());
					 * res.setMessage(ResponseEnum.SendOboxFail.getMsg()); }
					 * else if (oboxResp.getType() == Type.reply_timeout) {
					 * res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
					 * res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg()); }
					 * else {
					 * res.setStatus(ResponseEnum.SendOboxUnKnowFail.getStatus()
					 * );
					 * res.setMessage(ResponseEnum.SendOboxUnKnowFail.getMsg());
					 * } } else {
					 */
					res.setStatus(ResponseEnum.AddSuccess.getStatus());
					res.setMessage(ResponseEnum.AddSuccess.getMsg());
					//res.setData(null);
					// }
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
							|| resDevice.getData() == null) {
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
				if (releaseObox != null && releaseObox.getStatus() == ResponseEnum.AddSuccess.getStatus()
						) {
					res.setStatus(ResponseEnum.AddSuccess.getStatus());
					res.setMessage(ResponseEnum.AddSuccess.getMsg());
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
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
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
	public ResponseObject<Map<String, Object>> addLocalScene(@RequestBody(required = true) SceneDTO sceneDTO) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String sceneName = sceneDTO.getSceneName();
			String sceneType = sceneDTO.getSceneType();
			Byte msgAlter = sceneDTO.getMsgAlter();
			String sceneGroup = sceneDTO.getSceneGroup();
			Integer oboxSceneNumber = null;
			Byte alterNeed = sceneDTO.getAlterNeed();
			TScene tScene = new TScene();
			map.put("scene_type", sceneType);
			if (msgAlter == null || msgAlter == (byte) 0) {
				tScene.setMessageAlter((byte) 0);
			}
			if (alterNeed == null || alterNeed == (byte) 0) {
				tScene.setAlterNeed((byte) 0);
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
				boolean setWrite = cmdCache.setWrite(oboxSerialId);
				if (setWrite) {
					res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
					res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
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
				String data = addLocalSceneRes.getData().getDate();
				if (data.substring(0, 2).equals("00")) {
					res.setStatus(ResponseEnum.SendOboxError.getStatus());
					res.setMessage(ResponseEnum.SendOboxError.getMsg());
					return res;
				}
				String reply = null;
				long startTime = System.currentTimeMillis();
				oboxSceneNumber = Integer.parseInt(data.substring(6, 8), 16);
				while (System.currentTimeMillis() - startTime < max_waitting_time) {
					try {
						reply = cmdCache.getLocalSceneInfo(sceneName, oboxSerialId, sceneGroup, oboxSceneNumber);
						if (StringUtils.isEmpty(reply)) {
							TimeUnit.MILLISECONDS.sleep(150);
						} else {
							cmdCache.delLocalSceneInfo(sceneName, oboxSerialId, sceneGroup, oboxSceneNumber);
							break;
						}
					} catch (Exception e) {
					}
				}
				if (StringUtils.isEmpty(reply)) {
					res.setStatus(ResponseEnum.SendOboxTimeOut.getStatus());
					res.setMessage(ResponseEnum.SendOboxTimeOut.getMsg());
					return res;
				}
				TScene dbScene = null;
				if (dbScene == null) {
					ResponseObject<TScene> dbSceneRes = feignSceneClient
							.getScenesByOboxSerialIdAndOboxSceneNumber(oboxSerialId, oboxSceneNumber);
					if (dbSceneRes != null)
						dbScene = dbSceneRes.getData();
				}
				tScene.setOboxSceneNumber(oboxSceneNumber);
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
					feignAliClient.addLocalSceneCondition(oboxSceneNumber, oboxRes.getData().getOboxSerialId(),
							sceneConditionDTOs);
					TimeUnit.MILLISECONDS.sleep(1500);
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
					feignAliClient.addLocalSceneAction(nodeActionDTOs, oboxSceneNumber,
							oboxRes.getData().getOboxSerialId());
					// new Thread(new sceneAction(nodeActionDTOs, sceneNumber,
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
				map.put("scene_number", dbScene.getSceneNumber());
				map.put("obox_scene_number", dbScene.getOboxSceneNumber());
				map.put("obox_serial_id", oboxSerialId);
				res.setData(map);
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
	public ResponseObject<Map<String, Object>> addServerScene(@RequestBody(required = true) SceneDTO sceneDTO) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scene_type", SceneTypeEnum.server.getValue());
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
			Byte alterNeed = sceneDTO.getAlterNeed();
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
			if (msgAlter == null || msgAlter == (byte) 0) {
				tScene.setMessageAlter((byte) 0);
			}
			if (alterNeed == null || alterNeed == (byte) 0) {
				tScene.setAlterNeed((byte) 0);
			}
			if (!StringUtils.isEmpty(sceneGroup)) {
				tScene.setSceneGroup(sceneGroup);
			}
			tScene.setSceneName(sceneName);
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
			map.put("scene_number", ret);
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
							tSceneAction.setActionid(scRes.getData().getSceneNumber().intValue() + "");
							tSceneAction.setNodeType(NodeTypeEnum.status.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
						}
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.nvr.getValue())) {
						ResponseObject<TNvr> nvrRes = feignDeviceClient.getNvrByIP(sceneActionDTO.getDeviceSerialId());
						if (nvrRes != null && nvrRes.getData() != null
								&& nvrRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
							tSceneAction.setActionid(nvrRes.getData().getId().intValue() + "");
							tSceneAction.setNodeType(NodeTypeEnum.nvr.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
						}
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.security.getValue())) {

					}
				}
			}
			List<List<SceneConditionDTO>> sceneConditionDTOs = sceneDTO.getConditions();
			if (sceneConditionDTOs != null) {
				logger.info("====create server add scene condition====");
				for (int i = 0; i < sceneConditionDTOs.size(); i++) {
					List<SceneConditionDTO> list = sceneConditionDTOs.get(i);
					for (SceneConditionDTO sceneConditionDTO : list) {
						if (!sceneConditionDTO.getConditionType().equals(ConditionTypeEnum.time.getValue())
								&& !sceneConditionDTO.getConditionType().equals(ConditionTypeEnum.quartz.getValue())) {
							if (sceneConditionDTO.getDeviceSerialId() != null) {
								// node condition
								String condition = sceneConditionDTO.getCondition();
								String serialID = sceneConditionDTO.getDeviceSerialId();

								if (sceneConditionDTO.getConditionType().equals(ConditionTypeEnum.device.getValue())) {
									ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
											.getDeviceByUserAndSerialId(tUser.getId(), serialID);
									if (deviceRes != null
											&& deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
											&& deviceRes.getData() != null) {
										TSceneCondition tSceneCondition = new TSceneCondition();
										tSceneCondition.setSerialid(serialID);
										tSceneCondition.setCond(condition);
										tSceneCondition.setSceneNumber(ret);
										tSceneCondition.setConditionGroup(i);
										feignSceneClient.addSceneCondition(tSceneCondition);
									}

								} else if (sceneConditionDTO.getConditionType()
										.equals(ConditionTypeEnum.remoter.getValue())) {

								} else if (sceneConditionDTO.getConditionType()
										.equals(ConditionTypeEnum.fingerprint.getValue())) {

								}

							}
						} else if (sceneConditionDTO.getConditionType().equals(ConditionTypeEnum.time.getValue())) {
							String cond = sceneConditionDTO.getCondition();
							TSceneCondition tSceneCondition = new TSceneCondition();
							tSceneCondition.setCond(cond);
							tSceneCondition.setConditionGroup(i);
							tSceneCondition.setSceneNumber(ret);
							feignSceneClient.addSceneCondition(tSceneCondition);
							if (!StringUtils.isEmpty(tSceneCondition.getCond())) {
								feignQuartzClient.addJob(ret, sceneName, tSceneCondition.getConditionGroup(),
										tSceneCondition.getCond());
							}
						} else {
							String cond = sceneConditionDTO.getCondition();
							TSceneCondition tSceneCondition = new TSceneCondition();
							tSceneCondition.setCond(cond);
							tSceneCondition.setConditionGroup(i);
							tSceneCondition.setSceneNumber(ret);
							feignSceneClient.addSceneCondition(tSceneCondition);
							if (!StringUtils.isEmpty(tSceneCondition.getCond())) {
							}
						}
					}
				}
			}
			res.setData(map);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "modify server scene ", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/modifyServerScene", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> modifyServerScene(@RequestBody(required = true) SceneDTO sceneDTO) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TScene> sceneRes = feignSceneClient.getSceneBySceneNumber(sceneDTO.getSceneNumber());
			if (sceneRes == null || sceneRes.getData() == null
					|| sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			boolean needUpdate = false;
			String sceneName = sceneDTO.getSceneName();
			TScene tScene = sceneRes.getData();
			if (!StringUtils.isEmpty(sceneName)) {
				if (sceneDTO.getSceneName().equals(tScene.getSceneName())) {
					needUpdate = true;
					tScene.setSceneName(sceneDTO.getSceneName());
				}
			}
			if (sceneDTO.getMsgAlter() != null) {
				if (sceneDTO.getMsgAlter() != tScene.getMsgAlter()) {
					needUpdate = true;
					tScene.setMessageAlter(sceneDTO.getMsgAlter());
				}
			}
			if (!StringUtils.isEmpty(sceneDTO.getSceneGroup())) {
				needUpdate = true;
				tScene.setSceneGroup(sceneDTO.getSceneGroup());
			}
			if (needUpdate) {
				feignSceneClient.updateScene(tScene);
				// SceneBusiness.updateScene(tScene);
			}
			// action
			List<SceneActionDTO> sceneActionDTOs = sceneDTO.getActions();
			if (sceneActionDTOs != null) {
				feignSceneClient.deleteSceneActionsBySceneNumber(tScene.getSceneNumber());
				// SceneBusiness.deleteSceneActionsBySceneNumber(tScene
				// .getSceneNumber());
				for (SceneActionDTO sceneActionDTO : sceneActionDTOs) {
					TSceneAction tSceneAction = new TSceneAction();
					tSceneAction.setAction(sceneActionDTO.getAction());
					tSceneAction.setSceneNumber(tScene.getSceneNumber());

					if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.group.getValue())) {
						/*
						 * // group TServerGroup tServerGroup =
						 * queryGroupByWeight( tUser,
						 * Integer.parseInt(sceneActionDTO .getGroupId()));
						 * 
						 * if (tServerGroup != null) {
						 * tSceneAction.setActionID(tServerGroup.getId());
						 * tSceneAction.setNodeType(NodeTypeEnum.group
						 * .getValue());
						 * SceneBusiness.addSceneAction(tSceneAction); }
						 * 
						 */} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.single.getValue())) {
						ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient.getDeviceByUserAndSerialId(
								resUser.getData().getId(), sceneActionDTO.getDeviceSerialId());
						// TOboxDeviceConfig tOboxDeviceConfig =
						// queryDeviceByWeight(
						// tUser, sceneActionDTO.getDeviceSerialId());
						if (deviceRes != null && deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								&& deviceRes.getData() != null) {
							// if (tOboxDeviceConfig != null) {
							tSceneAction.setActionid(deviceRes.getData().getDeviceSerialId());
							tSceneAction.setNodeType(NodeTypeEnum.single.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
							// SceneBusiness.addSceneAction(tSceneAction);
						}

					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.camera.getValue())) {
						ResponseObject<TYSCamera> ysCameraRes = feignDeviceClient
								.getYSCameraBySerialId(sceneActionDTO.getDeviceSerialId());
						// TYSCamera tysCamera = CameraBusiness
						// .queryYSCameraBySerialId(sceneActionDTO
						// .getDeviceSerialId());
						if (ysCameraRes != null && ysCameraRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								&& ysCameraRes.getData() != null) {
							// if (tysCamera != null) {
							tSceneAction.setActionid(ysCameraRes.getData().getDeviceserial());
							// tSceneAction.setActionID(tysCamera.getId());
							tSceneAction.setNodeType(NodeTypeEnum.camera.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
							// SceneBusiness.addSceneAction(tSceneAction);
						}
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.nvr.getValue())) {
						ResponseObject<TNvr> nvrRes = feignDeviceClient.getNvrByIP(sceneActionDTO.getDeviceSerialId());
						// TNvr tNvr = NvrBusiness.queryNvrByIP(sceneActionDTO
						// .getDeviceSerialId());
						// if (tNvr != null) {
						if (nvrRes != null && nvrRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								&& nvrRes.getData() != null) {
							// tSceneAction.setActionID(tNvr.getId());
							tSceneAction.setActionid(nvrRes.getData().getIp());
							tSceneAction.setNodeType(NodeTypeEnum.nvr.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
							// SceneBusiness.addSceneAction(tSceneAction);
						}
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.security.getValue())) {
						/*
						 * // TNvr tNvr = //
						 * NvrBusiness.queryNvrByIP(sceneActionDTO.
						 * getDeviceSerialId()); TScene scene = SceneBusiness
						 * .querySceneBySceneNumber(sceneActionDTO
						 * .getSceneNumber()); if (scene != null) { tSceneAction
						 * .setActionID(scene.getSceneNumber());
						 * tSceneAction.setNodeType(NodeTypeEnum.security
						 * .getValue());
						 * SceneBusiness.addSceneAction(tSceneAction); }
						 */} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.status.getValue())) {
						ResponseObject<TScene> tsceneRes = feignSceneClient
								.getSceneBySceneNumber(sceneActionDTO.getSceneNumber());
						// TScene scene = SceneBusiness
						// .querySceneBySceneNumber(sceneActionDTO
						// .getSceneNumber());
						if (tsceneRes != null && tsceneRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								&& tsceneRes.getData() != null) {
							// if (scene != null) {
							tSceneAction.setActionid(tsceneRes.getData().getSceneNumber().intValue() + "");
							tSceneAction.setNodeType(NodeTypeEnum.status.getValue());
							feignSceneClient.addSceneAction(tSceneAction);
							// SceneBusiness.addSceneAction(tSceneAction);
						}
					}
				}
			}

			tScene.setSceneStatus(sceneDTO.getSceneStatus());
			// condition
			List<List<SceneConditionDTO>> tConditionDTOs = sceneDTO.getConditions();
			if (tConditionDTOs != null) {
				ResponseObject<List<TSceneCondition>> sceneConditionsRes = feignSceneClient
						.getSceneConditionsBySceneNumber(tScene.getSceneNumber());
				// List<TSceneCondition> tSceneConditions = SceneBusiness
				// .querySceneConditionsBySceneNumber(tScene
				// .getSceneNumber());
				if (sceneConditionsRes != null
						&& sceneConditionsRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& sceneConditionsRes.getData() != null) {
					List<TSceneCondition> tSceneConditions = sceneConditionsRes.getData();
					for (TSceneCondition tSceneCondition : tSceneConditions) {
						// if (tSceneCondition.getSerialId() == null) {
						if (tSceneCondition.getSerialid() == null) {
							// time quartz condition,to delete
							/*
							 * quartzService.deleteJob(String.format("%d",
							 * tScene.getSceneNumber()) + "_" +
							 * String.format("%d", tSceneCondition
							 * .getConditionGroup()));
							 */
							feignQuartzClient.deleteJob(String.format("%d", tScene.getSceneNumber()) + "_"
									+ String.format("%d", tSceneCondition.getConditionGroup()));
						}
					}
					feignSceneClient.deleteSceneConditionBySceneNumber(tScene.getSceneNumber());
					// SceneBusiness.deleteSceneConditionBySceneNumber(tScene
					// .getSceneNumber());
				}

				for (int i = 0; i < tConditionDTOs.size(); i++) {
					List<SceneConditionDTO> list = tConditionDTOs.get(i);
					for (SceneConditionDTO sceneConditionDTO : list) {
						if (sceneConditionDTO.getConditionType().equals(ConditionTypeEnum.time.getValue())) {
							String cond = sceneConditionDTO.getCondition();
							TSceneCondition tSceneCondition = new TSceneCondition();
							// tSceneCondition.setCondition(cond);
							tSceneCondition.setCond(cond);
							tSceneCondition.setConditionGroup(i);
							tSceneCondition.setSceneNumber(tScene.getSceneNumber());
							feignSceneClient.addSceneCondition(tSceneCondition);
							// SceneBusiness
							// .addSceneCondition(tSceneCondition);
							// if (!StringUtils.isEmpty(tSceneCondition
							// .getCondition())) {
							if (!StringUtils.isEmpty(tSceneCondition.getCond())) {
								/*
								 * quartzService .startSchedule(
								 * tScene.getSceneNumber(), sceneName,
								 * tSceneCondition .getCond(), tSceneCondition
								 * .getConditionGroup());
								 */
								feignQuartzClient.addJob(tScene.getSceneNumber(), sceneName,
										tSceneCondition.getConditionGroup(), tSceneCondition.getCond());
							}
						} else if (sceneConditionDTO.getConditionType().equals(ConditionTypeEnum.quartz.getValue())) {

						} else {
							if (sceneConditionDTO.getDeviceSerialId() != null) {
								// node condition
								String condition = sceneConditionDTO.getCondition();
								String serialID = sceneConditionDTO.getDeviceSerialId();

								if (sceneConditionDTO.getConditionType().equals(ConditionTypeEnum.device.getValue())) {
									// TOboxDeviceConfig tOboxDeviceConfig =
									// queryDeviceByWeight(tUser, serialID);
									ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
											.getDeviceByUserAndSerialId(resUser.getData().getId(), serialID);
									if (deviceRes != null
											&& deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
											&& deviceRes.getData() != null) {
										TSceneCondition tSceneCondition = new TSceneCondition();
										tSceneCondition.setSerialid(serialID);
										// tSceneCondition.setSerialId(serialID);
										tSceneCondition.setCond(condition);
										// tSceneCondition.setCondition(condition);
										tSceneCondition.setSceneNumber(tScene.getSceneNumber());
										tSceneCondition.setConditionGroup(i);
										feignSceneClient.addSceneCondition(tSceneCondition);
										// SceneBusiness.addSceneCondition(tSceneCondition);
									}
								} else if (sceneConditionDTO.getConditionType()
										.equals(ConditionTypeEnum.remoter.getValue())) {

								} else if (sceneConditionDTO.getConditionType()
										.equals(ConditionTypeEnum.fingerprint.getValue())) {

								}
							}
						}
					}
				}
			}
			map.put("scene_number", tScene.getSceneNumber());
			map.put("scene_name", tScene.getSceneName());
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			res.setData(map);
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "modify local scene ", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/modifyLocalScene", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> modifyLocalScene(@RequestBody(required = true) SceneDTO sceneDTO) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			// fail safe
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			String oboxSerialId = sceneDTO.getOboxSerialId();
			if (StringUtils.isEmpty(oboxSerialId)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TScene> sceneRes = feignSceneClient.getSceneBySceneNumber(sceneDTO.getSceneNumber());
			if (sceneRes == null || sceneRes.getData() == null
					|| sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TScene tScene = sceneRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					oboxSerialId);
			// TObox tObox = queryOboxByWeight(tUser, oboxSerialId);
			if (oboxRes == null || oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| oboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (sceneDTO.getMsgAlter() != null) {
				if (sceneDTO.getMsgAlter() != tScene.getMsgAlter()) {
					tScene.setMessageAlter(sceneDTO.getMsgAlter());
					// SceneBusiness.updateScene(tScene);
					feignSceneClient.updateScene(tScene);
				}
			}
			String sceneName = sceneDTO.getSceneName();
			if (!StringUtils.isEmpty(sceneName)) {
				if (!tScene.getSceneName().equals(sceneName)) {
					feignAliClient.setLocalScene(tScene.getSceneStatus(), tScene.getOboxSceneNumber(), sceneName,
							oboxSerialId, tScene.getSceneGroup());
				}
			}
			// condition
			List<List<SceneConditionDTO>> sceneConditionDTOs = sceneDTO.getConditions();
			if (sceneConditionDTOs != null && sceneConditionDTOs.size() > 0) {
				if (isNotCommonObox(sceneConditionDTOs, oboxSerialId)) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				feignAliClient.modifyLocalSceneCondition(tScene.getOboxSceneNumber(), oboxSerialId,
						resUser.getData().getId(), sceneConditionDTOs);
			}
			// action
			List<SceneActionDTO> tActionDTOs = sceneDTO.getActions();
			if (tActionDTOs != null) {
				ResponseObject<List<TSceneAction>> sceneActionsRes = feignSceneClient
						.getSceneActionsBySceneNumber(tScene.getSceneNumber());
				// List<TSceneAction> dbActions =
				// SceneBusiness.querySceneActionsBySceneNumber(tScene.getSceneNumber());
				List<SceneActionDTO> needDTOs = new ArrayList<SceneActionDTO>();
				if (sceneActionsRes != null && sceneActionsRes.getData() != null
						&& sceneActionsRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
					List<TSceneAction> dbActions = sceneActionsRes.getData();
					for (SceneActionDTO sceneActionDTO : tActionDTOs) {
						boolean isFound = false;
						for (TSceneAction tSceneAction : dbActions) {
							if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.single.getValue())
									&& tSceneAction.getNodeType().equals(NodeTypeEnum.single.getValue())) {
								ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
										.getDevice(tSceneAction.getActionid());
								// TOboxDeviceConfig tOboxDeviceConfig =
								// DeviceBusiness
								// .queryDeviceConfigByID(tSceneAction.getActionid());
								if (deviceRes != null && deviceRes.getData() != null
										&& deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
									TOboxDeviceConfig tOboxDeviceConfig = deviceRes.getData();
									if (tOboxDeviceConfig.getDeviceSerialId()
											.equals(sceneActionDTO.getDeviceSerialId())) {
										if (!tSceneAction.getAction().equals(sceneActionDTO.getAction())) {
											sceneActionDTO.setActionType(2);
											needDTOs.add(sceneActionDTO);
										}

										isFound = true;

										break;
									}
								}
							} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.group.getValue())
									&& tSceneAction.getNodeType().equals(NodeTypeEnum.group.getValue())) {
								/*
								 * TServerGroup tServerGroup = null; if
								 * (!StringUtils.isEmpty(sceneActionDTO.
								 * getGroupId()) &&
								 * Integer.parseInt(sceneActionDTO.getGroupId())
								 * != 0) { tServerGroup =
								 * queryGroupByWeight(tUser,
								 * Integer.parseInt(sceneActionDTO.getGroupId())
								 * ); } else { if
								 * (!StringUtils.isEmpty(sceneActionDTO.
								 * getOboxSerialId()) &&
								 * !StringUtils.isEmpty(sceneActionDTO.
								 * getOboxGroupAddr())) { TServerOboxGroup
								 * tServerOboxGroup =
								 * DeviceBusiness.queryOBOXGroupByAddr(
								 * sceneActionDTO.getOboxSerialId(),
								 * sceneActionDTO.getOboxGroupAddr()); if
								 * (tServerOboxGroup != null) { tServerGroup =
								 * DeviceBusiness
								 * .querySererGroupById(tServerOboxGroup.
								 * getServerId()); } } } if (tServerGroup !=
								 * null) { if (tServerGroup.getId().intValue()
								 * == tSceneAction.getActionid().intValue()) {
								 * if (!tSceneAction.getAction().equals(
								 * sceneActionDTO.getAction())) {
								 * sceneActionDTO.setActionType(2);
								 * needDTOs.add(sceneActionDTO); }
								 * 
								 * isFound = true; break; } }
								 */} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.camera.getValue())
										&& tSceneAction.getNodeType().equals(NodeTypeEnum.camera.getValue())) {
								ResponseObject<TYSCamera> ysCameraRes = feignDeviceClient
										.getYSCameraBySerialId(tSceneAction.getActionid());
								// TYSCamera tysCamera =
								// CameraBusiness.queryYSCameraById(tSceneAction.getActionid());
								if (ysCameraRes != null && ysCameraRes.getData() != null
										&& ysCameraRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
									TYSCamera tysCamera = ysCameraRes.getData();
									if (tysCamera.getDeviceserial().equals(sceneActionDTO.getDeviceSerialId())) {
										if (!tSceneAction.getAction().equals(sceneActionDTO.getAction())) {
											tSceneAction.setAction(sceneActionDTO.getAction());
											// feignSceneClient.updateSceneAction(tSceneAction);
											feignSceneClient.updateSceneAction(tSceneAction);
										}
										break;
									}
								}
							} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.nvr.getValue())
									&& tSceneAction.getNodeType().equals(NodeTypeEnum.nvr.getValue())) {
								ResponseObject<TNvr> nvrRes = feignDeviceClient.getNvrByIP(tSceneAction.getActionid());
								if (nvrRes != null && nvrRes.getData() != null
										&& nvrRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
									TNvr tNvr = nvrRes.getData();
									if (tNvr.getIp().equals(sceneActionDTO.getDeviceSerialId())) {
										if (!tSceneAction.getAction().equals(sceneActionDTO.getAction())) {
											tSceneAction.setAction(sceneActionDTO.getAction());
											feignSceneClient.updateSceneAction(tSceneAction);
										}
										break;
									}
								}
							}
						}
						if (!isFound) {
							if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.single.getValue())
									|| sceneActionDTO.getNodeType().equals(NodeTypeEnum.group.getValue())) {
								sceneActionDTO.setActionType(1);
								needDTOs.add(sceneActionDTO);
							}
						}
					}
				}
				List<TSceneAction> dbActions = sceneActionsRes.getData();
				for (TSceneAction tSceneAction : dbActions) {
					boolean isFound = false;
					for (SceneActionDTO sceneActionDTO : tActionDTOs) {
						if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.single.getValue())
								&& tSceneAction.getNodeType().equals(NodeTypeEnum.single.getValue())) {
							ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
									.getDevice(tSceneAction.getActionid());
							// TOboxDeviceConfig tOboxDeviceConfig =
							// DeviceBusiness
							// .queryDeviceConfigByID(tSceneAction.getActionid());
							if (deviceRes != null && deviceRes.getData() != null
									&& deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus() && deviceRes
											.getData().getDeviceSerialId().equals(sceneActionDTO.getDeviceSerialId())) {
								// if
								// (tOboxDeviceConfig.getDeviceSerialId().equals(sceneActionDTO.getDeviceSerialId()))
								// {
								isFound = true;
								break;
							}
						} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.group.getValue())
								&& tSceneAction.getNodeType().equals(NodeTypeEnum.group.getValue())) {
						} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.camera.getValue())
								&& tSceneAction.getNodeType().equals(NodeTypeEnum.camera.getValue())) {
							// TYSCamera tysCamera =
							// CameraBusiness.queryYSCameraById(tSceneAction.getActionid());
							ResponseObject<TYSCamera> ysCameraRes = feignDeviceClient
									.getYSCameraBySerialId(tSceneAction.getActionid());
							if (ysCameraRes != null && ysCameraRes.getData() != null
									&& ysCameraRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
								TYSCamera tysCamera = ysCameraRes.getData();
								if (tysCamera.getDeviceserial().equals(sceneActionDTO.getDeviceSerialId())) {
									isFound = true;
									break;
								}
							}
						} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.nvr.getValue())
								&& tSceneAction.getNodeType().equals(NodeTypeEnum.nvr.getValue())) {
							// TNvr tNvr =
							// NvrBusiness.queryNVR(tSceneAction.getActionid());
							ResponseObject<TNvr> nvrRes = feignDeviceClient.getNvrByIP(tSceneAction.getActionid());
							if (nvrRes != null && nvrRes.getData() != null
									&& nvrRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
								TNvr tNvr = nvrRes.getData();
								if (tNvr.getIp().equals(sceneActionDTO.getDeviceSerialId())) {
									isFound = true;
									break;
								}
							}
						}
					}
					if (!isFound) {
						if (tSceneAction.getNodeType().equals(NodeTypeEnum.single.getValue())) {
							ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient
									.getDevice(tSceneAction.getActionid());
							// TOboxDeviceConfig tOboxDeviceConfig =
							// DeviceBusiness
							// .queryDeviceConfigByID(tSceneAction.getActionid());
							if (deviceRes != null && deviceRes.getData() != null
									&& deviceRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
								TOboxDeviceConfig tOboxDeviceConfig = deviceRes.getData();
								SceneActionDTO sceneActionDTO = new SceneActionDTO();
								sceneActionDTO.setAction(tSceneAction.getAction());
								sceneActionDTO.setActionName(tOboxDeviceConfig.getDeviceId());
								sceneActionDTO.setDeviceRfAddr(tOboxDeviceConfig.getDeviceRfAddr());
								sceneActionDTO.setDeviceSerialId(tOboxDeviceConfig.getDeviceSerialId());
								sceneActionDTO.setOboxSerialId(tOboxDeviceConfig.getOboxSerialId());
								sceneActionDTO.setDeviceChildType(tOboxDeviceConfig.getDeviceChildType());
								sceneActionDTO.setDeviceType(tOboxDeviceConfig.getDeviceType());
								sceneActionDTO.setNodeType(NodeTypeEnum.single.getValue());
								sceneActionDTO.setActionType(0);
								needDTOs.add(sceneActionDTO);
							}
						} else if (tSceneAction.getNodeType().equals(NodeTypeEnum.group.getValue())) {
							SceneActionDTO sceneActionDTO = new SceneActionDTO();
							sceneActionDTO.setAction(tSceneAction.getAction());
							sceneActionDTO.setGroupId(tSceneAction.getActionid());
							sceneActionDTO.setNodeType(NodeTypeEnum.group.getValue());
							sceneActionDTO.setActionType(0);
							needDTOs.add(sceneActionDTO);
						} else if (tSceneAction.getNodeType().equals(NodeTypeEnum.camera.getValue())) {
							feignSceneClient.deleteSceneActionBySceneNumberAndActionId(tScene.getSceneNumber(),
									tSceneAction.getId().intValue() + "");
							// SceneBusiness.deleteSceneActionBySceneNumberAndActionId(tScene.getSceneNumber(),
							// tSceneAction.getId());
						} else if (tSceneAction.getNodeType().equals(NodeTypeEnum.nvr.getValue())) {
							// SceneBusiness.deleteSceneActionBySceneNumberAndActionId(tScene.getSceneNumber(),
							// tSceneAction.getId());
							feignSceneClient.deleteSceneActionBySceneNumberAndActionId(tScene.getSceneNumber(),
									tSceneAction.getId().intValue() + "");
						}
					}
				}
				feignAliClient.addLocalSceneAction(needDTOs, tScene.getOboxSceneNumber(), oboxSerialId);
				// new Thread(new sceneAction(needDTOs,
				// tScene.getOboxSceneNumber(), tObox)).start();
			}
			// jsonObject.addProperty("obox_scene_number",
			// tScene.getOboxSceneNumber());
			// jsonObject.addProperty("obox_serial_id",
			// tScene.getOboxSerialId());
			map.put("obox_serial_id", tScene.getOboxSerialId());
			map.put("obox_scene_number", tScene.getOboxSceneNumber());
			map.put("scene_number", tScene.getSceneNumber());
			map.put("scene_name", tScene.getSceneName());
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			res.setData(map);
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
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
				ResponseObject<TUserObox> userOboxRes = feignUserClient.getUserObox(resUser.getData().getId(),
						oboxRes.getData().getOboxSerialId());
				ResponseObject resobj = null;
				if (userOboxRes != null && userOboxRes.getData() == null)
					resobj = feignUserClient.addUserObox(tUserObox);
				TObox tobox = oboxRes.getData();
				if (oboxDTO.getOboxName() != null) {
					tobox.setOboxName(oboxDTO.getOboxName());
				}
				if (oboxDTO.getOboxVersion() != null) {
					tobox.setOboxVersion(oboxDTO.getOboxVersion());
				}
				// tobox.setOboxControl((byte) 0);
				feignOboxClient.updateObox(tobox.getOboxSerialId(), tobox);
				/*
				 * ResponseObject<List<TOboxDeviceConfig>> resList =
				 * feignDeviceClient
				 * .getDevicesByOboxSerialId(tobox.getOboxSerialId()); if
				 * (resList == null || resList.getStatus() !=
				 * ResponseEnum.SelectSuccess.getStatus()) {
				 * res.setStatus(ResponseEnum.Error.getStatus());
				 * res.setMessage(ResponseEnum.Error.getMsg()); return res; }
				 * List<TOboxDeviceConfig> deviceList = resList.getData(); if
				 * (deviceList != null && deviceList.size() > 0) { for
				 * (TOboxDeviceConfig tOboxDeviceConfig : deviceList) {
				 * 
				 * if (!tOboxDeviceConfig.getGroupAddr().equals("00")) {
				 * TServerOboxGroup tServerOboxGroup =
				 * DeviceBusiness.queryOBOXGroupByAddr(
				 * tOboxDeviceConfig.getOboxSerialId(),
				 * tOboxDeviceConfig.getGroupAddr()); if (tServerOboxGroup !=
				 * null) { TServerGroup tServerGroup = DeviceBusiness
				 * .querySererGroupById(tServerOboxGroup.getServerId()); if
				 * (tServerGroup != null) { if
				 * (tServerGroup.getGroupStyle().equals(GroupTypeEnum.
				 * local.getValue())) {
				 * DeviceBusiness.deleteServerGroup(tServerGroup); } }
				 * DeviceBusiness.deleteOBOXGroupByAddr(
				 * tOboxDeviceConfig.getOboxSerialId(),
				 * tOboxDeviceConfig.getGroupAddr()); } }
				 * 
				 * //
				 * DeviceBusiness.deleteDeviceGroup(tOboxDeviceConfig.getId());
				 * feignDeviceClient.delDevice(tOboxDeviceConfig.
				 * getDeviceSerialId()); //
				 * DeviceBusiness.deleteUserDeviceByDeviceId(tOboxDeviceConfig.
				 * getId()); //
				 * .DeviceBusiness.delDeviceChannel(tOboxDeviceConfig.getId());
				 * } }
				 */
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
							ResponseObject<TUserDevice> userDeviceRes = feignUserClient
									.getUserDevcieByUserIdAndSerialId(resUser.getData().getId(),
											oboxDeviceConfig.getDeviceSerialId());
							if (userDeviceRes == null || userDeviceRes.getData() == null) {
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
						if (sceneRes == null || sceneRes.getStatus() != ResponseEnum.AddSuccess.getStatus()
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
									 /*} */
								}else if (node_type.equals(NodeTypeEnum.single.getValue())) {
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
			logger.error("===error msg:"+e.getMessage());
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
			logger.error("===error msg:"+e.getMessage());
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
				logger.info("====userId:" + resUser.getData().getId() + "===userName:" + principal.getUsername());
				ResponseObject<List<TOboxDeviceConfig>> resDevices = feignDeviceClient
						.getDeviceTypeByUser(resUser.getData().getId());
				if (resDevices != null && resDevices.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						 ) {
					if(resDevices.getData()==null||resDevices.getData().size()<=0){
						res.setStatus(ResponseEnum.SelectSuccess.getStatus());
						res.setMessage(ResponseEnum.SelectSuccess.getMsg());
						res.setData(tCounts);
						return res;
					}
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

	@ApiOperation(value = "delete scene", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "deleteSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/deleteScene/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject<Map<String, Object>> deleteScene(@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			ResponseObject<TScene> sceneRes = feignSceneClient.getSceneBySceneNumber(sceneNumber);
			Map<String, Object> map = new HashMap<String, Object>();
			if (sceneRes == null || sceneRes.getData() == null
					|| sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			} else {
				TScene scene = sceneRes.getData();
				if (scene.getSceneType().equals(SceneTypeEnum.server.getValue())) {
					ResponseObject<List<TSceneCondition>> sceneConditionsRes = feignSceneClient
							.getSceneConditionsBySceneNumber(sceneNumber);
					if (sceneConditionsRes != null && sceneConditionsRes.getData() != null
							&& sceneConditionsRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
						List<TSceneCondition> tSceneConditions = sceneConditionsRes.getData();
						for (TSceneCondition tSceneCondition : tSceneConditions) {
							if (tSceneCondition.getSerialid() == null) {
								feignQuartzClient.deleteJob(scene.getSceneName() + "_"
										+ String.format("%d", tSceneCondition.getConditionGroup()));
								break;
							}
						}
						feignSceneClient.deleteSceneConditionBySceneNumber(sceneNumber);
					}
				} else if (scene.getSceneType().equals(SceneTypeEnum.local.getValue())) {
					ResponseObject<TObox> oboxRes = feignOboxClient.getObox(scene.getOboxSerialId());
					if (oboxRes != null && oboxRes.getData() != null
							&& oboxRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
						feignAliClient.deleteLocalScene(scene.getOboxSceneNumber(), scene.getSceneName(),
								oboxRes.getData().getOboxSerialId());
					}
				}
				// feignUserClient.deleteUserSceneBySceneNumber(sceneNumber);
				// SceneBusiness.deleteUserScene(tScene.getSceneNumber());
				// feignSceneClient.deleteSceneActionsBySceneNumber(sceneNumber);
				// SceneBusiness.deleteSceneActionsBySceneNumber(tScene
				// .getSceneNumber());
				// feignSceneClient.deleteScene(sceneNumber);
				// SceneBusiness.deleteSceneBySceneNumber(tScene.getSceneNumber());
				// SceneBusiness.deleteSceneLocationBySceneNumber(tScene
				// .getSceneNumber());
				res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
				res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
				// JsonObject jsonObject = respRight();
				map.put("scene_number", scene.getSceneNumber());
				// jsonObject.addProperty("scene_number",
				// tScene.getSceneNumber());
				map.put("obox_scene_number", scene.getOboxSceneNumber());
				// jsonObject.addProperty("obox_scene_number",
				// tScene.getOboxSceneNumber());
				map.put("obox_serial_id", scene.getOboxSerialId());
				// jsonObject.addProperty("obox_serial_id",
				// tScene.getOboxSerialId());
				// jsonObject.addProperty("scene_status", sceneStatus);
				map.put("scene_status", "03");
				// return jsonObject;
				res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
				res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
				res.setData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "excute scene", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/excuteScene/{sceneNumber}", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> excuteScene(@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ResponseObject<TScene> sceneRes = feignSceneClient.getSceneBySceneNumber(sceneNumber);
			if (sceneRes == null || sceneRes.getData() == null
					|| sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			} else {
				TScene scene = sceneRes.getData();
				if (scene.getSceneType().equals("00")) {
					if (scene.getSceneRun() == 0) {
						scene.setSceneRun((byte) 1);
						scene.setAlterNeed((byte) 1);
						feignSceneClient.updateScene(scene);
						// SceneBusiness.updateScene(tScene);
						// sceneActionThreadPool.addSceneAction(tScene.getSceneNumber());
						feignAliClient.addSceneAction(sceneNumber);
					}
				} else {
					ResponseObject<TObox> oboxRes = feignOboxClient.getObox(scene.getOboxSerialId());
					if (oboxRes != null && oboxRes.getData() != null
							&& oboxRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
						// TObox obox = oboxRes.getData();
						feignAliClient.excuteLocalScene(scene.getOboxSceneNumber(), scene.getSceneName(),
								oboxRes.getData().getOboxSerialId());
					}
				}
				map.put("scene_number", sceneNumber);
				// jsonObject.addProperty("scene_number",
				// tScene.getSceneNumber());
				map.put("scene_type", scene.getSceneType());
				// jsonObject.addProperty("scene_type", tScene.getSceneType());
				String oboxSerialId = scene.getOboxSerialId();
				if (StringUtils.isEmpty(oboxSerialId))
					oboxSerialId = null;
				// jsonObject
				// .addProperty("obox_scene_number",
				// tScene.getOboxSceneNumber());
				map.put("obox_scene_number", scene.getOboxSceneNumber());
				map.put("obox_serial_id", oboxSerialId);
				// jsonObject.addProperty("obox_serial_id",oboxSerialId);
				map.put("scene_status", "02");
				// jsonObject.addProperty("scene_status", sceneStatus);
				// return jsonObject;
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				res.setData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "enable/disable scene", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "UpdateSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/enableScene/{sceneNumber}/{sceneStatus}", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> enableScene(@PathVariable(value = "sceneNumber") Integer sceneNumber,
			@PathVariable(value = "sceneStatus") String sceneStatus) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ResponseObject<TScene> sceneRes = feignSceneClient.getSceneBySceneNumber(sceneNumber);
			if (sceneRes == null || sceneRes.getData() == null
					|| sceneRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			} else {
				TScene scene = sceneRes.getData();
				if (scene.getSceneType().equals("00")) {
					scene.setSceneStatus((byte) Integer.parseInt(sceneStatus, 16));
					ResponseObject<List<TSceneCondition>> sceneConditions = feignSceneClient
							.getSceneConditionsBySceneNumber(sceneNumber);
					if (sceneConditions != null && sceneConditions.getData() != null
							&& sceneConditions.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
						// QuartzService quartzService = new QuartzService();
						List<TSceneCondition> tSceneConditions = sceneConditions.getData();
						for (TSceneCondition tSceneCondition : tSceneConditions) {
							if (tSceneCondition.getSerialid() == null) {
								if (sceneStatus.equals("00")) {
									// disable
									feignQuartzClient.pauseJob(scene.getSceneName() + "_"
											+ String.format("%d", tSceneCondition.getConditionGroup()));
								} else if (sceneStatus.equals("01")) {
									feignQuartzClient.resumeJob(scene.getSceneName() + "_"
											+ String.format("%d", tSceneCondition.getConditionGroup()));
								}
							}
						}
					}
					feignSceneClient.updateScene(scene);
				} else {
					ResponseObject<TObox> oboxRes = feignOboxClient.getObox(scene.getOboxSerialId());
					if (oboxRes != null && oboxRes.getData() != null
							&& oboxRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
						feignAliClient.excuteLocalScene(scene.getOboxSceneNumber(), scene.getSceneName(),
								oboxRes.getData().getOboxSerialId(), sceneStatus);
					}
				}
				map.put("scene_number", sceneNumber);
				// jsonObject.addProperty("scene_number",
				// tScene.getSceneNumber());
				map.put("scene_type", scene.getSceneType());
				// jsonObject.addProperty("scene_type", tScene.getSceneType());
				String oboxSerialId = scene.getOboxSerialId();
				if (StringUtils.isEmpty(oboxSerialId))
					oboxSerialId = null;
				// jsonObject
				// .addProperty("obox_scene_number",
				// tScene.getOboxSceneNumber());
				map.put("obox_scene_number", scene.getOboxSceneNumber());
				map.put("obox_serial_id", oboxSerialId);
				// jsonObject.addProperty("obox_serial_id",oboxSerialId);
				map.put("scene_status", "02");
				// jsonObject.addProperty("scene_status", sceneStatus);
				// return jsonObject;
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				res.setData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "queryUserOperationHistory", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/queryUserOperationHistory/{serialId}/{type}/{fromDate}/{toDate}/{startIndex}/{countIndex}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> queryUserOperationHistory(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "type") String type,
			@PathVariable(value = "fromDate") Long fromDate, @PathVariable(value = "toDate") Long toDate,
			@PathVariable(value = "startIndex") Integer startIndex,
			@PathVariable(value = "countIndex") Integer countIndex) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("type", type);
			if (type.equals("00")) {
				ResponseObject<List<TUserOperation>> userOperationRes = feignUserClient.getUserOperation(fromDate,
						toDate, serialId, startIndex, countIndex);
				/*
				 * List<TUserOperation> operations =
				 * UserBusiness.queryUserOperation( from, to, serialId,
				 * Integer.parseInt(startIndex),
				 */
				List<TUserOperation> operations = null;
				if (userOperationRes != null && userOperationRes.getData() != null
						&& userOperationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
					operations = userOperationRes.getData();
					for (TUserOperation tUserOperation : operations) {
						tUserOperation.setTime(tUserOperation.getLastOpTime().getTime() / 1000);
					}
				}
				map.put("history", operations);
			} else if (type.equals("01")) {
				List<JsonObject> list = new ArrayList<JsonObject>();
				if ((toDate % fromDate) / 86400 > 0) {
					for (int i = 1; i <= (toDate % fromDate) / 86400; i++) {
						ResponseObject<List<TUserOperation>> userOperationRes = feignUserClient
								.queryUserOperationByDate(fromDate + (i - 1) * 86400, fromDate + i * 86400, serialId);
						/*
						 * List<TUserOperation> operations =
						 * UserBusiness.queryUserOperationByDate(from + (i - 1)
						 * * 86400, from + i * 86400, serialId);
						 */
						if (userOperationRes != null && userOperationRes.getData() != null
								&& userOperationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								&& userOperationRes.getData().size() != 0) {
							JsonObject object = new JsonObject();
							object.addProperty("from_date", String.valueOf(fromDate + (i - 1) * 86400));
							object.addProperty("to_date", String.valueOf(fromDate + i * 86400));
							object.addProperty("count", userOperationRes.getData().size());
							list.add(object);
						}
					}
					ResponseObject<List<TUserOperation>> userOperationRes = feignUserClient.queryUserOperationByDate(
							fromDate + ((toDate % fromDate) / 86400) * 86400, toDate, serialId);
					/*
					 * List<TUserOperation> operations = UserBusiness
					 * .queryUserOperationByDate(from + ((to % from) / 86400) *
					 * 86400, to, serialId);
					 */
					if (userOperationRes != null && userOperationRes.getData() != null
							&& userOperationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
							&& userOperationRes.getData().size() != 0) {
						// if (operations.size() != 0) {
						JsonObject object = new JsonObject();
						object.addProperty("from_date",
								String.valueOf(fromDate + ((toDate % fromDate) / 86400) * 86400));
						object.addProperty("to_date", String.valueOf(toDate));
						object.addProperty("count", userOperationRes.getData().size());
						list.add(object);
					}
				} else {
					ResponseObject<List<TUserOperation>> userOperationRes = feignUserClient
							.queryUserOperationByDate(fromDate, toDate, serialId);
					// List<TUserOperation> operations =
					// UserBusiness.queryUserOperationByDate(from, to,
					// serialId);
					if (userOperationRes != null && userOperationRes.getData() != null
							&& userOperationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
							&& userOperationRes.getData().size() != 0) {
						// if (operations.size() != 0) {
						JsonObject object = new JsonObject();
						object.addProperty("from_date", String.valueOf(fromDate));
						object.addProperty("to_date", String.valueOf(toDate));
						object.addProperty("count", userOperationRes.getData().size());
						list.add(object);
					}
				}
				Collections.reverse(list);
				map.put("history", list);
				// jsonObject.add("history", g2.toJsonTree(list));
			} else if (type.equals("02")) {
				List<JsonObject> list = new ArrayList<JsonObject>();
				for (int i = 0; i < 7; i++) {
					ResponseObject<List<TUserOperation>> userOperationRes = feignUserClient.queryUserOperationByDate(
							fromDate + i * 24 * 60 * 60, fromDate + (i + 1) * 24 * 60 * 60, serialId);
					/*
					 * List<TUserOperation> operations =
					 * UserBusiness.queryUserOperationByDate(from + i * 24 * 60
					 * * 60, from + (i + 1) * 24 * 60 * 60, serialId);
					 */
					float power = 0.0f;
					List<TUserOperation> operations = null;
					if (userOperationRes != null && userOperationRes.getData() != null
							&& userOperationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
							&& userOperationRes.getData().size() != 0) {
						operations = userOperationRes.getData();
						for (int j = 0; j < operations.size(); j++) {
							TUserOperation operation1 = operations.get(j);
							String bright1 = operation1.getDeviceState().substring(0, 2);
							if (!bright1.equals("00")) {
								if (j + 1 >= operations.size()) {
									// the last one
									String cString = dateToString(operation1.getLastOpTime());
									String[] timeString = cString.split(" ");
									String[] tc = timeString[1].split(":");

									int remainH = 24 - Integer.parseInt(tc[0]);
									int remainM = 60 - Integer.parseInt(tc[1]);
									float time = remainH + remainM / 60.0f;
									if (Integer.parseInt(bright1, 16) < 154) {
										power += 6 * 0.1 * time;
									} else {
										power += 6 * ((Integer.parseInt(bright1, 16) - 154) / 100.0f) * time;
									}
								} else {
									TUserOperation operation2 = operations.get(j + 1);

									long date;
									if (operation2.getLastOpTime().getTime() > operation1.getLastOpTime().getTime()) {
										date = operation2.getLastOpTime().getTime()
												- operation1.getLastOpTime().getTime();
									} else {
										date = operation1.getLastOpTime().getTime()
												- operation2.getLastOpTime().getTime();
									}

									long day = date / (1000 * 60 * 60 * 24);
									long hour = (date / (1000 * 60 * 60) - day * 24);
									long min = ((date / (60 * 1000)) - day * 24 * 60 - hour * 60);

									float time = hour + min / 60.0f;
									if (Integer.parseInt(bright1, 16) < 154) {
										power += 6 * 0.1 * time;
									} else {
										power += 6 * ((Integer.parseInt(bright1, 16) - 154) / 100.0f) * time;
									}
								}
							}
						}
					}
					JsonObject object = new JsonObject();
					object.addProperty("day", fromDate + i * 24 * 60 * 60);
					object.addProperty("power", String.valueOf(power));
					object.addProperty("count", operations == null ? 0 : operations.size());
					list.add(object);
				}
				map.put("history", list);
				// jsonObject.add("history", g2.toJsonTree(list));
			} else if (type.equals("03")) {
				//
				/*
				 * if (StringUtils.isEmpty(fromDate)) { return
				 * respError(ErrorEnum.request_param_invalid.getValue()); }
				 */
				// TCount
				List<JsonObject> list = new ArrayList<JsonObject>();
				ResponseObject<List<TUserOperation>> userOperationRes = feignUserClient
						.queryUserOperationByMonthDayList(SubTableConstant.T_USER_OPERATION_SUFFIX
								+ DateHelper.formatDate(new Date().getTime(), DateHelper.FORMATMONTH));
				/*
				 * List<TUserOperation> days = UserBusiness
				 * .queryUserOperationByMonthDayList(SubTableConstant.
				 * T_USER_OPERATION_SUFFIX + DateHelper.formatDate(new
				 * Date().getTime(), DateHelper.FORMATMONTH));
				 */
				/*
				 * if (days == null || days.size() <= 0) { // JsonObject object
				 * = new JsonObject(); jsonObject.add("history",
				 * g2.toJsonTree(null)); return jsonObject; }
				 */
				if (userOperationRes != null && userOperationRes.getData() != null
						&& userOperationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& userOperationRes.getData().size() != 0) {
					List<TUserOperation> days = userOperationRes.getData();
					for (int i = 0; i < days.size(); i++) {
						int power = 0;
						logger.info("===days.get(i).getDay()===:" + days.get(i).getDay());
						ResponseObject<List<TUserOperation>> operationRes = feignUserClient.queryUserOperationByMonth(
								SubTableConstant.T_USER_OPERATION_SUFFIX
										+ DateHelper.formatDate(new Date().getTime(), DateHelper.FORMATMONTH),
								serialId, days.get(i).getDay());
						if (operationRes != null && operationRes.getData() != null
								&& operationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
								&& operationRes.getData().size() != 0) {
							List<TUserOperation> operations = operationRes.getData();
							// if (operations != null && operations.size() > 0)
							// {
							for (int j = 0; j < operations.size(); j++) {
								TUserOperation operation1 = operations.get(j);
								if (!operation1.getDeviceState().substring(0, 2).equals("00")) {
									power += Integer.parseInt(operation1.getDeviceState().substring(10, 14), 16);
								}

							}
							JsonObject object = new JsonObject();
							object.addProperty("day", operations.get(0).getLastOpTime().getTime() / 1000);
							object.addProperty("power", ByteHelper.int2HexString(power));
							object.addProperty("count", operations.size());
							list.add(object);
						}
					}
					map.put("history", list);
					// jsonObject.add("history", g2.toJsonTree(list));
				}
			} else if (type.equals("04")) {
				List<JsonObject> list = new ArrayList<JsonObject>();
				ResponseObject<List<TCreateTableLog>> createTableLogRes = feignUserClient
						.listCreateTableLogByNameWithLike(SubTableConstant.T_USER_OPERATION_SUFFIX);
				/*
				 * List<TCreateTableLog> tCreateTableLogs =
				 * CreateTableLogBussiness
				 * .listCreateTableLogByNameWithLike(SubTableConstant.
				 * T_USER_OPERATION_SUFFIX);
				 */
				int months = 12;
				if (createTableLogRes != null && createTableLogRes.getData() != null
						&& createTableLogRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
						&& createTableLogRes.getData().size() < 12) {
					// if (tCreateTableLogs.size() < 12) {
					months = createTableLogRes.getData().size();
				}
				logger.info("===months===:" + months);
				List<TCreateTableLog> tCreateTableLogs = createTableLogRes.getData();
				for (int i = 0; i < months; i++) {
					int power = 0;
					logger.info("===table name===:" + tCreateTableLogs.get(i).getName());
					ResponseObject<List<TUserOperation>> operationRes = feignUserClient
							.queryUserOperation(tCreateTableLogs.get(i).getName(), serialId);
					/*
					 * List<TUserOperation> operations =
					 * UserBusiness.queryUserOperation(tCreateTableLogs.get(i).
					 * getName(), serialId);
					 */
					if (operationRes != null && operationRes.getData() != null
							&& operationRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()
							&& operationRes.getData().size() > 0) {
						// if (operations != null && operations.size() > 0) {
						List<TUserOperation> operations = operationRes.getData();
						logger.info("===get power===");
						for (int j = 0; j < operations.size(); j++) {
							TUserOperation operation1 = operations.get(j);
							if (!operation1.getDeviceState().substring(0, 2).equals("00")) {
								power += Integer.parseInt(operation1.getDeviceState().substring(10, 14), 16);
							}

						}
						JsonObject object = new JsonObject();
						object.addProperty("day", operations.get(0).getLastOpTime().getTime() / 1000);
						object.addProperty("power", ByteHelper.int2HexString(power));
						object.addProperty("count", operations.size());
						list.add(object);
					}
				}
				// jsonObject.add("history", g2.toJsonTree(list));
				map.put("history", list);
			} else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
				// return respError(ErrorEnum.request_param_invalid.getValue());
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(map);
			// return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "modifyDeviceName", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/modifyDeviceName/{serialId}/{name}", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> modifyDeviceName(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "name") String name) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient.getDevice(serialId);
			if (deviceRes == null || deviceRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig tOboxDeviceConfig = deviceRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getObox(tOboxDeviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			feignAliClient.modifyDeviceName(tOboxDeviceConfig.getOboxSerialId(), name,
					tOboxDeviceConfig.getDeviceRfAddr());
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			map.put("name", name);
			map.put("serialId", serialId);
			map.put("operate_type", "01");
			res.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "deleteDevice", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "SelectSuccess", response = ResponseObject.class)
	@RequestMapping(value = "/deleteDevice/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject<Map<String, Object>> deleteDevice(@PathVariable(value = "serialId") String serialId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient.getDevice(serialId);
			if (deviceRes == null || deviceRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig tOboxDeviceConfig = deviceRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getObox(tOboxDeviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			feignAliClient.deleteDevice(tOboxDeviceConfig.getOboxSerialId(), tOboxDeviceConfig.getDeviceRfAddr(),
					tOboxDeviceConfig.getDeviceId());
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			map.put("serialId", serialId);
			map.put("operate_type", "00");
			res.setData(map);
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query_intelligent_fingerHome", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentFingerHome/{serialId}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> getIntelligentFingerHome(String serialId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<Integer> countRes = feignDeviceClient.countFingerAuth(serialId);
			if (countRes == null || countRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isAuth", countRes.getData() == null ? 0 : countRes.getData().intValue());// 0不存在，大于0存在
			map.put("onLine", (new Date().getTime() - deviceConfig.getLastOpTime().getTime()) / ((60 * 60 * 1000)) > 24
					? false : true);
			String state = "00";
			// 上锁 、虚掩、开锁
			if (deviceConfig.getDeviceState().length() >= 6)
				state = deviceConfig.getDeviceState().substring(2, 4);
			if (state.equals("c6")) {
				String type = deviceConfig.getDeviceState().substring(4, 6);
				if (Integer.parseInt(type, 16) == 5)
					map.put("type", 5);
				else if (Integer.parseInt(type, 16) == 7)
					map.put("type", 7);
				else if (Integer.parseInt(type, 16) == 8)
					map.put("type", 8);
			} else {
				map.put("type", 5);
			}
			// 电量
			String betty = deviceConfig.getDeviceState().substring(0, 2);
			//电量
			//if(byteToBit.substring(7, 8).equals("1"))
			map.put("betty",Integer.parseInt(betty,16)>=128?(Integer.parseInt(betty,16)-128):Integer.parseInt(betty,16));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(map);
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query_intelligent_openRecord", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentFingerOpenRecord/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<IntelligentOpenRecordItemDTO>> getIntelligentFingerOpenRecord(
			@PathVariable(value = "serialId") String serialId) {
		ResponseObject<List<IntelligentOpenRecordItemDTO>> res = new ResponseObject<List<IntelligentOpenRecordItemDTO>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// add timeStamp
			ResponseObject<List<IntelligentOpenRecordDTO>> openRes = feignDeviceClient.queryIntelligentOpenRecordByDate(
					serialId, DateHelper.formatDate(new Date().getTime(), DateHelper.FORMATALL),
					DateHelper.formatDate(DateHelper.getMonthEnd(), DateHelper.FORMATALL));
			long startTime = DateHelper.endOfTodDay();
			List<IntelligentOpenRecordItemDTO> items = init(startTime);
			if (openRes != null && openRes.getData() != null && openRes.getData().size() > 0) {
				for (IntelligentOpenRecordDTO dto : openRes.getData()) {
					handlerDTO(dto, items, startTime);
				}
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(delEmptyItems(items));
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**  
	 * @param items
	 * @return  
	 * @Description:  
	 */
	private List<IntelligentOpenRecordItemDTO> delEmptyItems(List<IntelligentOpenRecordItemDTO> items) {
		List<IntelligentOpenRecordItemDTO> list=new ArrayList<IntelligentOpenRecordItemDTO>();
		for(IntelligentOpenRecordItemDTO item:items){
			if(item.getList()!=null&&item.getList().size()>0){
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query_intelligent_openRecord", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentFingerWarnRecord/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<IntelligentFingerWarnItemDTO>> getIntelligentFingerWarnRecord(
			@PathVariable(value = "serialId") String serialId) {
		ResponseObject<List<IntelligentFingerWarnItemDTO>> res = new ResponseObject<List<IntelligentFingerWarnItemDTO>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<Integer> countRes = feignDeviceClient.getCountIntelligentWarnBySerialId(serialId);
			if (countRes == null || countRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			List<IntelligentFingerWarnItemDTO> items = null;
			int count = countRes.getData() == null ? 0 : countRes.getData().intValue();
			if (count <= 0) {
				items = new ArrayList<IntelligentFingerWarnItemDTO>();
			} else {
				// 返回List(门锁用户（类型/场景名字，发生时间)
				List<IntelligentFingerWarnDTO> list = null;
				ResponseObject<List<IntelligentFingerWarnDTO>> warnRes = feignDeviceClient.getIntelligentWarnByDate(
						serialId, DateHelper.formatDate(new Date().getTime(), DateHelper.FORMATALL),
						DateHelper.formatDate(DateHelper.getMonthEnd(), DateHelper.FORMATALL));
				if (warnRes == null || warnRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				list = warnRes.getData();
				long startTime = DateHelper.endOfTodDay();
				items = initWarnRecord(startTime);
				if (list != null && list.size() > 0) {
					for (IntelligentFingerWarnDTO dto : list) {
						handlerWarnDTO(dto, items, startTime);
					}
				}
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(delEmptyWarnItems(items));
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**  
	 * @param items
	 * @return  
	 * @Description:  
	 */
	private List<IntelligentFingerWarnItemDTO> delEmptyWarnItems(List<IntelligentFingerWarnItemDTO> items) {
		List<IntelligentFingerWarnItemDTO> list=new ArrayList<IntelligentFingerWarnItemDTO>();
		for(IntelligentFingerWarnItemDTO item:items){
			if(item!=null&&item.getList()!=null&&item.getList().size()>0){
				list.add(item);
			}
		}
		return list;
	}

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query_intelligent_useringRecord", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentUseringRecord/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<TIntelligentFingerUser>> getIntelligentUseringRecord(
			@PathVariable(value = "serialId") String serialId) {
		ResponseObject<List<TIntelligentFingerUser>> res = new ResponseObject<List<TIntelligentFingerUser>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<Integer> countRes = feignDeviceClient.getCountIntelligentUserBySerialId(serialId);
			if (countRes == null || countRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			int count = countRes.getData() == null ? 0 : countRes.getData().intValue();
			List<TIntelligentFingerUser> list = null;
			if (count <= 0) {
				list = new ArrayList<TIntelligentFingerUser>();
			} else {
				// 返回List(用户详情（用户名，用户序列号，用户等级，胁迫发送密码）)
				ResponseObject<List<TIntelligentFingerUser>> fingerUserRes = feignDeviceClient
						.getIntelligentUserBySerialId(serialId);
				if (fingerUserRes == null || fingerUserRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				list = fingerUserRes.getData();
			}
			res.setData(list);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param pin
	 * @param nickName
	 * @param mobile
	 * @param validateCode
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "edit_intelligent_user", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/updateIntelligentUser/{serialId}/{pin}", method = RequestMethod.PUT)
	public ResponseObject updateIntelligentUser(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "pin") String pin,
			@RequestParam(required = false, value = "nickName") String nickName,
			@RequestParam(required = false, value = "mobile") String mobile,
			@RequestParam(required = false, value = "validateCode") String validateCode) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TIntelligentFingerUser> fingerUserRes = feignDeviceClient
					.getIntelligentFingerUserBySerialIdAndPin(serialId, pin);
			if (fingerUserRes == null || fingerUserRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| fingerUserRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TIntelligentFingerUser fingerUser = fingerUserRes.getData();
			if (!StringUtils.isEmpty(mobile) && !StringUtils.isEmpty(validateCode) && fingerUser.getExistForce() == 1) {
				// 判断短信验证码
				if (!MobileUtil.checkMobile(mobile)) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				String mobileValidateCode = cmdCache.getMobileValidateCode(mobile, pin, serialId);
				// String mobileValidateCode =
				// AccessTokenTool.getMobileValidateCode(mobile, pin, serialId);
				if (!StringUtils.isEmpty(mobileValidateCode) && validateCode.trim().equals(mobileValidateCode)) {
					// 修改数据
					fingerUser.setMobile(mobile);
					if (nickName != null) {
						fingerUser.setNickName(nickName);
					}
					feignDeviceClient.updatentelligentFingerUser(fingerUser);
				} else {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
			} else if (StringUtils.isEmpty(mobile) && fingerUser.getExistForce() == 1) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			} else {
				if (nickName == null) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				fingerUser.setNickName(nickName);
				feignDeviceClient.updatentelligentFingerUser(fingerUser);
			}
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param pin
	 * @param mobile
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "send_intelligent_validateCode", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/sendIntelligentValidateCode/{serialId}/{pin}/{mobile}", method = RequestMethod.POST)
	public ResponseObject sendIntelligentValidateCode(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "pin") String pin, @PathVariable(value = "mobile") String mobile) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (!MobileUtil.checkMobile(mobile)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TIntelligentFingerUser> fingerUserRes = feignDeviceClient
					.getIntelligentFingerUserBySerialIdAndPin(serialId, pin);
			if (fingerUserRes == null || fingerUserRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| fingerUserRes.getData() == null || fingerUserRes.getData().getExistForce() != 1) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 返回 code(验证码)
			String validateCode = (Math.random() * 9 + 1) * 100000 + "";
			cmdCache.addMobileValidateCode(mobile, pin, serialId, validateCode);
			msgService.sendCode(mobile, validateCode);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param pwd
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "add_intelligent_authPwd", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/addIntelligentAuthPwd/{serialId}/{pwd}", method = RequestMethod.POST)
	public ResponseObject addIntelligentAuthPwd(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "pwd") String pwd) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 返回是否成功
			ResponseObject<TIntelligentFingerAuth> authRes = feignDeviceClient.getIntelligentAuthBySerialId(serialId);
			if (authRes != null && authRes.getData() != null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TIntelligentFingerAuth auth = new TIntelligentFingerAuth();
			auth.setPwd(MD5.MD5generator(pwd + salt));
			auth.setSerialid(serialId);
			feignDeviceClient.addIntelligentFingerAuth(auth);
			// int id=UserBusiness.addIntelligentFingerAuth(auth);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param pwd
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query_intelligent_authPwd", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentAuthPwd/{serialId}/{pwd}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> getIntelligentAuthPwd(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "pwd") String pwd) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 返回是否成功
			ResponseObject<TIntelligentFingerAuth> authRes = feignDeviceClient.getIntelligentAuthBySerialId(serialId);
			if (authRes == null || authRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| authRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			String authToken = cmdCache.addIntelligentToken(serialId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("authToken", authToken);
			res.setData(map);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param authToken
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query_intelligent_authPwd", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentAuthPwd/{serialId}/{authToken}", method = RequestMethod.GET)
	public ResponseObject<List<IntelligentFingerRemoteUserDTO>> getIntelligentRemoteUnLocking(String serialId,
			String authToken) {
		ResponseObject<List<IntelligentFingerRemoteUserDTO>> res = new ResponseObject<List<IntelligentFingerRemoteUserDTO>>();
		try {
			String intelligentSerialId = cmdCache.getIntelligentSerialId(authToken);
			if (StringUtils.isEmpty(intelligentSerialId) || !intelligentSerialId.equals(serialId)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<Integer> countRes = feignDeviceClient.countFingerAuth(serialId);
			// TCount
			// count=UserBusiness.queryCountFingerRemoteUsersBySerialId(serialId);
			if (countRes == null || countRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 返回 临时授权用户（名称，开始时间，结束时间，是否进行/结束）
			List<TIntelligentFingerRemoteUser> list = null;
			ResponseObject<List<TIntelligentFingerRemoteUser>> remoteRes = feignDeviceClient
					.getIntelligentFingerRemoteUsersBySerialId(serialId);
			if (remoteRes == null || remoteRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| remoteRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(transformToDTO(remoteRes.getData()));
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param authToken
	 * @param nickName
	 * @param startTime
	 * @param endTime
	 * @param times
	 * @param mobile
	 * @param isMax
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "addIntelligentRemoteUser", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentAuthPwd/{serialId}/{authToken}/{nickName}/{startTime}/{endTime}", method = RequestMethod.POST)
	public ResponseObject<Map<String, Object>> addIntelligentRemoteUser(@PathVariable("serialId") String serialId,
			@PathVariable("authToken") String authToken, @PathVariable("nickName") String nickName,
			@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime,
			@RequestParam(value = "times", required = false) String times,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "isMax", required = false) String isMax) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			String intelligentSerialId = cmdCache.getIntelligentSerialId(authToken);
			if (StringUtils.isEmpty(intelligentSerialId) || !intelligentSerialId.equals(serialId)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<List<TIntelligentFingerAbandonRemoteUser>> abandonRemoteUserRes = feignDeviceClient
					.getTIntelligentFingerAbandonRemoteUsersBySerialId(serialId);
			if (abandonRemoteUserRes == null
					|| abandonRemoteUserRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			List<TIntelligentFingerAbandonRemoteUser> abandonRemoteUsers = abandonRemoteUserRes.getData();
			Integer userSerialId;
			if (abandonRemoteUsers != null && abandonRemoteUsers.size() > 0) {
				userSerialId = abandonRemoteUsers.get(0).getUserSerialid().intValue();
				feignDeviceClient.delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(abandonRemoteUsers.get(0).getSerialid(),abandonRemoteUsers.get(0).getUserSerialid());
				//feignDeviceClient.delIntelligentFingerAbandonRemoteUserById(abandonRemoteUsers.get(0).getId());
			} else {
				ResponseObject<List<TIntelligentFingerRemoteUser>> remoteUserRes = feignDeviceClient
						.getTIntelligentFingerRemoteUsersBySerialId(serialId);
				if (remoteUserRes == null || remoteUserRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				List<TIntelligentFingerRemoteUser> remoteUsers = remoteUserRes.getData();
				if (remoteUsers != null && remoteUsers.size() > 0) {
					userSerialId = remoteUsers.get(0).getUserSerialid() + 1;
					if (userSerialId >= 100) {
						res.setStatus(ResponseEnum.RequestParamError.getStatus());
						res.setMessage(ResponseEnum.RequestParamError.getMsg());
						return res;
					} else if (userSerialId < 10) {
						userSerialId = 10;
					}
				} else {
					userSerialId = 10;
				}
			}
			String randomNum = (int) ((Math.random() * 9 + 1) * 10000) + "";
			feignAliClient.sendMessageToFinger(RemoteUserEnum.add.getValue(), oboxRes.getData(), deviceConfig,
					startTime, endTime, times, userSerialId, randomNum);
			TIntelligentFingerRemoteUser fingerRemoteUser = null;
			if (StringUtils.isEmpty(isMax) || Integer.parseInt(isMax) == IntelligentMaxEnum.MIN.getValue()) {
				fingerRemoteUser = new TIntelligentFingerRemoteUser(userSerialId, mobile == null ? "" : mobile,
						serialId, nickName, startTime, endTime, Integer.parseInt(times), randomNum);
			} else {
				fingerRemoteUser = new TIntelligentFingerRemoteUser(userSerialId, mobile == null ? "" : mobile,
						serialId, nickName, startTime, endTime, 1, randomNum, IntelligentMaxEnum.MAX.getValue());
			}
			ResponseObject<Integer> ingerRemoteUserRes = feignDeviceClient
					.addTIntelligentFingerRemoteUser(fingerRemoteUser);
			if (ingerRemoteUserRes == null || ingerRemoteUserRes.getStatus() != ResponseEnum.AddSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			int fingerRemoteUserId = ingerRemoteUserRes.getData();
			ResponseObject<TIntelligentFingerRemoteUser> remoteUserRes = feignDeviceClient
					.getIntelligentFingerRemoteUserById(fingerRemoteUserId);
			if (remoteUserRes == null || remoteUserRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 创建一个定时器
			feignQuartzClient.addRemoteOpenTaskSchedule(fingerRemoteUserId, endTime, serialId);
			// 返回参数待测试
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("authCode", "62" + userSerialId + "" + randomNum);
			map.put("remoteUser", new IntelligentFingerRemoteUserDTO(remoteUserRes.getData()));
			res.setData(map);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param id
	 * @param serialId
	 * @param authToken
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "addIntelligentRemoteUser", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/delIntelligentRemoteUser/{id}/{serialId}/{authToken}", method = RequestMethod.DELETE)
	public ResponseObject delIntelligentRemoteUser(@PathVariable("id") Integer id,
			@PathVariable("serialId") String serialId, @PathVariable("authToken") String authToken) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			String intelligentSerialId = cmdCache.getIntelligentSerialId(authToken);
			if (StringUtils.isEmpty(intelligentSerialId) || !intelligentSerialId.equals(serialId)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TIntelligentFingerRemoteUser> remoteUserRes = feignDeviceClient
					.getIntelligentFingerRemoteUserById(id);
			if (remoteUserRes == null || remoteUserRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| remoteUserRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			feignDeviceClient.delTIntelligentFingerRemoteUserById(remoteUserRes.getData().getId());
			feignAliClient.sendMessageToFinger(RemoteUserEnum.del.getValue(), oboxRes.getData(),
					deviceConfigRes.getData(), remoteUserRes.getData().getStartTime(),
					remoteUserRes.getData().getEndTime(), remoteUserRes.getData().getTimes().intValue()
							- remoteUserRes.getData().getUseTimes().intValue() + "",
					remoteUserRes.getData().getUserSerialid(), remoteUserRes.getData().getPwd());
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "addIntelligentRemoteUser", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/updateIntelligentPwd/{serialId}/{oldPwd}/{newPwd}", method = RequestMethod.PUT)
	public ResponseObject updateIntelligentPwd(@PathVariable("serialId") String serialId,
			@PathVariable("oldPwd") String oldPwd, @PathVariable("newPwd") String newPwd) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TIntelligentFingerAuth> authRes = feignDeviceClient.getIntelligentAuthBySerialId(serialId);
			if (authRes == null || authRes.getData() == null
					|| authRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| !MD5.MD5generator(oldPwd + salt).equals(authRes.getData().getPwd())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TIntelligentFingerAuth fingerAuth = authRes.getData();
			fingerAuth.setPwd(MD5.MD5generator(newPwd + salt));
			feignDeviceClient.updateTintelligentFingerAuth(fingerAuth);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "query_intelligent_push_list", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/getIntelligentPushList/{serialId}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> getIntelligentPushList(@PathVariable("serialId") String serialId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<List<TIntelligentFingerPush>> pushRes = feignDeviceClient
					.getTIntelligentFingerPushsBySerialId(serialId);
			if (pushRes == null || pushRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			List<TIntelligentFingerPush> list = pushRes.getData();
			if (list == null || list.size() == 0) {
				map.put("mobile", "");
			} else {
				map.put("mobile", list.get(0).getMobile());
			}
			map.put("list", pushToDTO(list));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param id
	 * @param authToken
	 * @param nickName
	 * @param startTime
	 * @param endTime
	 * @param times
	 * @param mobile
	 * @param isMax
	 * @return
	 * @Description:
	 */
	@ApiOperation(value = "modify_intelligent_remote_user", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/updateIntelligentRemoteUser/{serialId}/{id}/{authToken}/{nickName}/{startTime}/{endTime}", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> updateIntelligentRemoteUser(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "id") int id,
			@PathVariable(value = "authToken") String authToken, @PathVariable(value = "nickName") String nickName,
			@PathVariable(value = "startTime") long startTime, @PathVariable(value = "endTime") long endTime,
			@RequestParam(value = "times", required = false) String times,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "isMax", required = false) String isMax) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			String intelligentSerialId = cmdCache.getIntelligentSerialId(authToken);
			if (StringUtils.isEmpty(intelligentSerialId) || !intelligentSerialId.equals(serialId)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (startTime >= endTime || endTime <= new Date().getTime()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (!StringUtils.isEmpty(mobile) && !MobileUtil.checkMobile(mobile)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (!StringUtils.isEmpty(isMax) && (!NumberHelper.isNumeric(isMax) || StringUtils.isEmpty(times)
					|| !NumberHelper.isNumeric(times))) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 判断远程用户
			ResponseObject<TIntelligentFingerRemoteUser> remoteRes = feignDeviceClient
					.getIntelligentFingerRemoteUserById(id);
			if (remoteRes == null || remoteRes.getData() == null
					|| remoteRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TIntelligentFingerRemoteUser fingerRemoteUser = remoteRes.getData();
			String randomNum = (int) ((Math.random() * 9 + 1) * 10000) + "";
			// pin 要改变
			if (fingerRemoteUser.getIsend() == 0) {
				feignAliClient.sendMessageToFinger(RemoteUserEnum.update.getValue(), oboxRes.getData(), deviceConfig,
						startTime + "", endTime + "", times, fingerRemoteUser.getUserSerialid(), randomNum);
			} else {
				ResponseObject<List<TIntelligentFingerAbandonRemoteUser>> abandonRemoteUsersRes = feignDeviceClient
						.getTIntelligentFingerAbandonRemoteUsersBySerialId(deviceConfig.getDeviceSerialId());
				if (abandonRemoteUsersRes == null
						|| abandonRemoteUsersRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				List<TIntelligentFingerAbandonRemoteUser> abandonRemoteUsers = abandonRemoteUsersRes.getData();
				if (abandonRemoteUsers != null && abandonRemoteUsers.size() > 0) {
					fingerRemoteUser.setUserSerialid(abandonRemoteUsers.get(0).getUserSerialid().intValue());
					feignDeviceClient.delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(abandonRemoteUsers.get(0).getSerialid(),abandonRemoteUsers.get(0).getUserSerialid());
					//feignDeviceClient.delIntelligentFingerAbandonRemoteUserById(abandonRemoteUsers.get(0).getId());
				} else {
					ResponseObject<List<TIntelligentFingerRemoteUser>> remoteUsersRes = feignDeviceClient
							.getTIntelligentFingerRemoteUsersBySerialId(deviceConfig.getDeviceSerialId());
					if (remoteUsersRes == null
							|| remoteUsersRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
						res.setStatus(ResponseEnum.RequestParamError.getStatus());
						res.setMessage(ResponseEnum.RequestParamError.getMsg());
						return res;
					}
					List<TIntelligentFingerRemoteUser> remoteUsers = remoteUsersRes.getData();
					if (remoteUsers != null && remoteUsers.size() > 0) {
						if ((remoteUsers.get(0).getUserSerialid() + 1) >= 100) {
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						} else if ((remoteUsers.get(0).getUserSerialid() + 1) < 10) {
							fingerRemoteUser.setUserSerialid(10);
						} else {
							fingerRemoteUser.setUserSerialid(remoteUsers.get(0).getUserSerialid() + 1);
						}
					} else {
						fingerRemoteUser.setUserSerialid(10);
					}
				}
			}
			if (StringUtils.isEmpty(isMax) || Integer.parseInt(isMax) == IntelligentMaxEnum.MIN.getValue()) {
				fingerRemoteUser.setTimes(Integer.parseInt(times));
				fingerRemoteUser.setIsmax(IntelligentMaxEnum.MIN.getValue());
			} else {
				fingerRemoteUser.setTimes(1);
				fingerRemoteUser.setIsmax(IntelligentMaxEnum.MAX.getValue());
			}
			long time = new Date().getTime();
			if (time < startTime) {
				fingerRemoteUser.setIsend(-1);
			} else if (time > endTime) {
				fingerRemoteUser.setIsend(1);
			} else {
				fingerRemoteUser.setIsend(0);
			}
			fingerRemoteUser.setNickName(nickName);
			fingerRemoteUser.setPwd(randomNum);
			fingerRemoteUser.setStartTime(startTime + "");
			fingerRemoteUser.setEndTime(endTime + "");
			fingerRemoteUser.setTimes(Integer.parseInt(times));
			fingerRemoteUser.setUseTimes(0);
			fingerRemoteUser.setMobile(mobile == null ? "" : mobile);
			feignDeviceClient.updateTIntelligentFingerRemoteUser(fingerRemoteUser);
			// 创建一个定时器
			feignQuartzClient.addRemoteOpenTaskSchedule(fingerRemoteUser.getId(), endTime + "", serialId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pwd", "62" + fingerRemoteUser.getUserSerialid() + randomNum);
			map.put("remoteUser", new IntelligentFingerRemoteUserDTO(fingerRemoteUser));
			res.setData(map);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param pin
	 * @param authToken
	 * @param mobile
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "modify_intelligent_remote_user", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/updateIntelligentRemoteUser/{serialId}/{pin}/{authToken}/{mobile}", method = RequestMethod.PUT)
	public ResponseObject sendRemotePwd(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "pin") String pin, @PathVariable(value = "authToken") String authToken,
			@PathVariable(value = "mobile") String mobile) {
		ResponseObject res = new ResponseObject();
		try {
			String intelligentSerialId = cmdCache.getIntelligentSerialId(authToken);
			if (StringUtils.isEmpty(intelligentSerialId) || !intelligentSerialId.equals(serialId)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (!MobileUtil.checkMobile(mobile)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TIntelligentFingerRemoteUser> remoteRes = feignDeviceClient
					.getTIntelligentFingerRemoteUserBySerialIdAndPin(serialId, Integer.parseInt(pin));
			if (remoteRes == null || remoteRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| remoteRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TIntelligentFingerRemoteUser remoteUser = remoteRes.getData();
			msgService.sendNotice(mobile, "你的临时授权码是：" + remoteUser.getPwd());
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param appkey
	 * @param pwd
	 * @param code
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "modify_intelligent_remote_user", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/resetIntelligentPwdByCode/{serialId}/{appkey}/{pwd}/{code}", method = RequestMethod.PUT)
	public ResponseObject resetIntelligentPwdByCode(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "appkey") String appkey, @PathVariable(value = "pwd") String pwd,
			@PathVariable(value = "code") String code) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			cmdCache.createIntelligentForgetPwdKey(serialId, sessionKey(resUser.getData().getId(), appkey));
			cmdCache.createIntelligentForgetPwd(sessionKey(resUser.getData().getId(), appkey), pwd);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param mobile
	 * @param pushinfo
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "modify_intelligent_remote_user", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/updateIntelligentRemoteUser/{serialId}/{mobile}", method = RequestMethod.PUT)
	public ResponseObject updateIntelligentRemoteUser(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "mobile") String mobile,
			@RequestParam(value = "pushinfo") List<TIntelligentFingerPushDTO> pushinfo) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getData() == null
					|| resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TOboxDeviceConfig> deviceConfigRes = feignDeviceClient.getDevice(serialId);
			if (deviceConfigRes == null || deviceConfigRes.getData() == null
					|| !deviceConfigRes.getData().getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
					|| !deviceConfigRes.getData().getDeviceChildType()
							.equals(DeviceTypeEnum.capacity_finger.getValue())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig deviceConfig = deviceConfigRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(),
					deviceConfig.getOboxSerialId());
			if (oboxRes == null || oboxRes.getData() == null
					|| oboxRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}  
			feignDeviceClient.updateTIntelligentFingerPushMobileBySerialId(mobile, serialId);
			for (int i = 0; i < pushinfo.size(); i++) {
				TIntelligentFingerPushDTO dto = pushinfo.get(i);
				feignDeviceClient.updateTIntelligentFingerPushEnableBySerialIdAndValue(dto.getEnable(),
						serialId, dto.getValue());
  			}
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	public String sessionKey(int uid, String appKey) {
		if (!StringUtils.isEmpty(appKey))
			return appKey + "#" + uid;
		return "#" + uid;
	}

	/**
	 * @param list
	 * @return
	 * @Description:
	 */
	private List<TIntelligentFingerPushDTO> pushToDTO(List<TIntelligentFingerPush> list) {
		if (list == null)
			return null;
		List<TIntelligentFingerPushDTO> dtos = new ArrayList<TIntelligentFingerPushDTO>();
		for (TIntelligentFingerPush push : list) {
			dtos.add(new TIntelligentFingerPushDTO(push));
		}
		return dtos;
	}

	/**
	 * @param data
	 * @return
	 * @Description:
	 */
	private List<IntelligentFingerRemoteUserDTO> transformToDTO(List<TIntelligentFingerRemoteUser> list) {
		List<IntelligentFingerRemoteUserDTO> dtos = new ArrayList<IntelligentFingerRemoteUserDTO>();
		for (TIntelligentFingerRemoteUser user : list) {
			dtos.add(new IntelligentFingerRemoteUserDTO(user));
		}
		return dtos;
	}

	/**
	 * @param dto
	 * @param items
	 * @param startTime
	 * @Description:
	 */
	private void handlerWarnDTO(IntelligentFingerWarnDTO dto, List<IntelligentFingerWarnItemDTO> items,
			long startTime) {
		if (dto.getTimeStamp() <= startTime) {
			int temp = (int) ((startTime - dto.getTimeStamp()) / (24 * 60 * 60 * 1000));
			if (temp <= 30) {
				dto.setWarnTime(DateHelper.formatDate(dto.getTimeStamp(), DateHelper.FORMATHOUR));
				items.get(temp).getList().add(dto);
			}
		}
	}

	/**
	 * @param startTime
	 * @return
	 * @Description:
	 */
	private List<IntelligentFingerWarnItemDTO> initWarnRecord(long startTime) {
		List<IntelligentFingerWarnItemDTO> items = new ArrayList<IntelligentFingerWarnItemDTO>();
		for (long i = 0; i < 30; i++) {
			IntelligentFingerWarnItemDTO item = new IntelligentFingerWarnItemDTO();
			item.setDateline(DateHelper.formatDate(startTime - i * 24 * 60 * 60 * 1000, DateHelper.FORMAT));
			item.setList(new ArrayList<IntelligentFingerWarnDTO>());
			items.add(item);
		}
		return items;
	}

	/**
	 * @param dto
	 * @param items
	 * @param startTime
	 * @Description:
	 */
	private void handlerDTO(IntelligentOpenRecordDTO dto, List<IntelligentOpenRecordItemDTO> items, long startTime) {

		if (dto.getTimeStamp() <= startTime) {
			int temp = (int) ((startTime - dto.getTimeStamp()) / (24 * 60 * 60 * 1000));
			if (temp <= 30) {
				dto.setOpenTime(DateHelper.formatDate(dto.getTimeStamp(), DateHelper.FORMATHOUR));
				items.get(temp).getList().add(dto);
			}
		}
	}

	/**
	 * @param startTime
	 * @return
	 * @Description:
	 */
	private List<IntelligentOpenRecordItemDTO> init(long startTime) {
		List<IntelligentOpenRecordItemDTO> items = new ArrayList<IntelligentOpenRecordItemDTO>();
		for (long i = 0; i < 30; i++) {
			IntelligentOpenRecordItemDTO item = new IntelligentOpenRecordItemDTO();
			item.setDateline(DateHelper.formatDate(startTime - i * 24 * 60 * 60 * 1000, DateHelper.FORMAT));
			item.setList(new ArrayList<IntelligentOpenRecordDTO>());
			items.add(item);
		}
		return items;
	}

	private String extractPathFromPattern(final HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	}

	private static String dateToString(java.util.Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);

		return ctime;
	}

	// @ApiOperation(value = "sendLearn2IR", httpMethod = "POST", produces =
	// "application/json")
	// @ApiResponse(code = 200, message = "SelectSuccess", response =
	// ResponseObject.class)
	@RequestMapping(value = "/sendlearn", method = RequestMethod.POST)
	public ResponseObject<List<Map<String, String>>> sendLearn2IR(@RequestBody(required = true) Object object) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
		Map<String, Object> requestMap = (Map<String, Object>) object;
		try {
			ResponseObject<TObox> oboxRes = feignOboxClient.getObox((String) requestMap.get("deviceId"));
			if (oboxRes == null || oboxRes.getData() == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				ResponseObject<List<Map<String, String>>> resList = feignAliClient.sendLearn2IR(object);
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

}
