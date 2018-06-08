package com.bright.apollo.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.dto.OboxResp.Type;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.enums.SceneTypeEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.feign.FeignUserClient;
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
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/release/{oboxSerialId}", method = RequestMethod.DELETE)
	public ResponseObject releaseDevice(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		ResponseObject res = new ResponseObject();
		try {
			// query obox if exist
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getCode() != ResponseEnum.Success.getCode() || resObox.getData() == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				// send cmd or use a facade to send the order to ali
				ResponseObject<OboxResp> releaseObox = feignAliClient.releaseObox(oboxSerialId);
				if (releaseObox != null && releaseObox.getCode() == ResponseEnum.Success.getCode()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setCode(ResponseEnum.SendOboxFail.getCode());
							res.setMsg(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setCode(ResponseEnum.SendOboxTimeOut.getCode());
							res.setMsg(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setCode(ResponseEnum.SendOboxUnKnowFail.getCode());
							res.setMsg(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}
				} else {
					res.setCode(ResponseEnum.SendOboxError.getCode());
					res.setMsg(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// control device
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "control  device", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseObject controlDevice(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestParam(required = true, value = "status") String status
			) {
		ResponseObject res = new ResponseObject();
		try {
			// the device and obox is exist
			ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient.getDevice(serialId);
			if(deviceRes==null||deviceRes.getCode()!=ResponseEnum.Success.getCode()||
					deviceRes.getData()==null
					){
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}
			TOboxDeviceConfig tOboxDeviceConfig = deviceRes.getData();
			ResponseObject<TObox> oboxRes = feignOboxClient.getObox(tOboxDeviceConfig.getOboxSerialId());
			if(oboxRes==null||oboxRes.getCode()!=ResponseEnum.Success.getCode()||
					oboxRes.getData()==null
					){
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}
			// may will add respone return status and serialId
			ResponseObject<OboxResp> resSet =feignAliClient.setDeviceStatus(tOboxDeviceConfig.getOboxSerialId(),status);
			if(resSet==null||resSet.getCode()!=ResponseEnum.Success.getCode()){
 				return resSet;
			}
			OboxResp oboxResp = resSet.getData();
			if (oboxResp.getType() != Type.success) {
				if (oboxResp.getType() == Type.obox_process_failure
						|| oboxResp.getType() == Type.socket_write_error) {
					res.setCode(ResponseEnum.SendOboxFail.getCode());
					res.setMsg(ResponseEnum.SendOboxFail.getMsg());
				} else if (oboxResp.getType() == Type.reply_timeout) {
					res.setCode(ResponseEnum.SendOboxTimeOut.getCode());
					res.setMsg(ResponseEnum.SendOboxTimeOut.getMsg());
				} else {
					res.setCode(ResponseEnum.SendOboxUnKnowFail.getCode());
					res.setMsg(ResponseEnum.SendOboxUnKnowFail.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// control scene
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "control  scene,the scene must is a server scene ", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{sceneNumber}", method = RequestMethod.PUT)
	public ResponseObject controlScene(@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<SceneInfo> sceneRes = feignSceneClient.getScene(sceneNumber);
			if (sceneRes == null || sceneRes.getCode() != ResponseEnum.Success.getCode() || sceneRes.getData() == null
					|| sceneRes.getData().getScene() == null) {
				res.setCode(ResponseEnum.ServerError.getCode());
				res.setMsg(ResponseEnum.ServerError.getMsg());
				return res;
			} else {
				TScene scene = sceneRes.getData().getScene();
				if (!scene.getSceneType().equals(SceneTypeEnum.server.getValue())) {
					res.setCode(ResponseEnum.RequestParamError.getCode());
					res.setMsg(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				if (scene.getSceneRun() == 0) {
					// old code
					scene.setSceneRun(1);
					SceneInfo sceneInfo = sceneRes.getData();
					sceneInfo.setScene(scene);
					ResponseObject updateScene = feignSceneClient.updateScene(sceneNumber, sceneInfo);
					if (updateScene == null || updateScene.getCode() != ResponseEnum.Success.getCode()) {
						res.setCode(ResponseEnum.MicroServiceUnConnection.getCode());
						res.setMsg(ResponseEnum.MicroServiceUnConnection.getMsg());
						return res;
					}
					ResponseObject resp = feignAliClient.controlServerScene(sceneNumber);
					if (resp != null && resp.getCode() == ResponseEnum.Success.getCode()) {
						return resp;
					}
					scene.setSceneRun(0);
					sceneInfo.setScene(scene);
					feignSceneClient.updateScene(sceneNumber, sceneInfo);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// ====================================search
	// device==========================================
	// stop scan old code state is 00 ，取消搜索设备
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 00", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scan/{oboxSerialId}", method = RequestMethod.DELETE)
	public ResponseObject stopScan(@PathVariable(required = true) String oboxSerialId) {
		// modify the request param
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getCode() != ResponseEnum.Success.getCode() || resObox.getData() == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				ResponseObject<OboxResp> scanObox = feignAliClient.stopScan(oboxSerialId);
				if (scanObox != null && scanObox.getCode() == ResponseEnum.Success.getCode()
						&& scanObox.getData() != null) {
					OboxResp oboxResp = scanObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setCode(ResponseEnum.SendOboxFail.getCode());
							res.setMsg(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setCode(ResponseEnum.SendOboxTimeOut.getCode());
							res.setMsg(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setCode(ResponseEnum.SendOboxUnKnowFail.getCode());
							res.setMsg(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}
				} else {
					res.setCode(ResponseEnum.SendOboxError.getCode());
					res.setMsg(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// search/scan 设备重新上电
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 01", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scan/restart/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject searchDevicesByOldStyle(
			@PathVariable(value = "oboxSerialId", required = true) String oboxSerialId,
			@RequestParam(required=false,value="deviceType") String deviceType,
			@RequestParam(required=false,value="deviceChildType") String deviceChildType,
			@RequestParam(required=false,value="deviceChildType") String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getCode() != ResponseEnum.Success.getCode() || resObox.getData() == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				ResponseObject<OboxResp> releaseObox = feignAliClient.scanByRestart(oboxSerialId, deviceType,
						deviceChildType, serialId);
				if (releaseObox != null && releaseObox.getCode() == ResponseEnum.Success.getCode()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setCode(ResponseEnum.SendOboxFail.getCode());
							res.setMsg(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setCode(ResponseEnum.SendOboxTimeOut.getCode());
							res.setMsg(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setCode(ResponseEnum.SendOboxUnKnowFail.getCode());
							res.setMsg(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}
				} else {
					res.setCode(ResponseEnum.SendOboxError.getCode());
					res.setMsg(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// search/scan 设备不重新上电搜索
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 02", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
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
			if (resObox == null || resObox.getCode() != ResponseEnum.Success.getCode() || resObox.getData() == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				if (principal.getUsername() != null && !principal.getUsername().equals("")) {
					res.setCode(ResponseEnum.RequestParamError.getCode());
					res.setMsg(ResponseEnum.RequestParamError.getMsg());
				}
				int countOfDevice = 0;
				ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
				if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
					TUser tUser = resUser.getData();
					ResponseObject<List<TOboxDeviceConfig>> resDevice = feignDeviceClient
							.getOboxDeviceConfigByUserId(tUser.getId());
					if (resDevice == null || resDevice.getCode() != ResponseEnum.Success.getCode()
							|| resDevice.getData() != null) {
						res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
						res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
						return res;
					}
					List<TOboxDeviceConfig> list = resDevice.getData();
					countOfDevice = list.size() + 1;
				} else {
					res.setCode(ResponseEnum.UnKonwUser.getCode());
					res.setMsg(ResponseEnum.UnKonwUser.getMsg());
					return res;
				}
				// search device by user
				ResponseObject<OboxResp> releaseObox = feignAliClient.scanByUnStop(oboxSerialId, deviceType,
						deviceChildType, serialId, countOfDevice);
				if (releaseObox != null && releaseObox.getCode() == ResponseEnum.Success.getCode()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setCode(ResponseEnum.SendOboxFail.getCode());
							res.setMsg(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setCode(ResponseEnum.SendOboxTimeOut.getCode());
							res.setMsg(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setCode(ResponseEnum.SendOboxUnKnowFail.getCode());
							res.setMsg(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}
				} else {
					res.setCode(ResponseEnum.SendOboxError.getCode());
					res.setMsg(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// search/scan 03 自动搜索
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "search  device the state is 03", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scan/initiative/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject searchDevicesByInitiative(
			@PathVariable(value = "oboxSerialId", required = true) String oboxSerialId,
			@RequestParam(value = "deviceType",required=false) String deviceType,
			@RequestParam(value = "deviceChildType",required=false) String deviceChildType,
			@RequestParam(value = "serialId",required=false) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> resObox = feignOboxClient.getObox(oboxSerialId);
			if (resObox == null || resObox.getCode() != ResponseEnum.Success.getCode() || resObox.getData() == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				if (principal.getUsername() != null && !principal.getUsername().equals("")) {
					res.setCode(ResponseEnum.RequestParamError.getCode());
					res.setMsg(ResponseEnum.RequestParamError.getMsg());
				}
				int countOfDevice = 0;
				ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
				if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
					TUser tUser = resUser.getData();
					ResponseObject<List<TOboxDeviceConfig>> resDevice = feignDeviceClient
							.getOboxDeviceConfigByUserId(tUser.getId());
					if (resDevice == null || resDevice.getCode() != ResponseEnum.Success.getCode()
							|| resDevice.getData() != null) {
						res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
						res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
						return res;
					}
					List<TOboxDeviceConfig> list = resDevice.getData();
					countOfDevice = list.size() + 1;
				} else {
					res.setCode(ResponseEnum.UnKonwUser.getCode());
					res.setMsg(ResponseEnum.UnKonwUser.getMsg());
					return res;
				}
				// search device by user
				ResponseObject<OboxResp> releaseObox = feignAliClient.scanByInitiative(oboxSerialId, deviceType,
						deviceChildType, serialId, countOfDevice);
				if (releaseObox != null && releaseObox.getCode() == ResponseEnum.Success.getCode()
						&& releaseObox.getData() != null) {
					OboxResp oboxResp = releaseObox.getData();
					if (oboxResp.getType() != Type.success) {
						if (oboxResp.getType() == Type.obox_process_failure
								|| oboxResp.getType() == Type.socket_write_error) {
							res.setCode(ResponseEnum.SendOboxFail.getCode());
							res.setMsg(ResponseEnum.SendOboxFail.getMsg());
						} else if (oboxResp.getType() == Type.reply_timeout) {
							res.setCode(ResponseEnum.SendOboxTimeOut.getCode());
							res.setMsg(ResponseEnum.SendOboxTimeOut.getMsg());
						} else {
							res.setCode(ResponseEnum.SendOboxUnKnowFail.getCode());
							res.setMsg(ResponseEnum.SendOboxUnKnowFail.getMsg());
						}
					}
				} else {
					res.setCode(ResponseEnum.SendOboxError.getCode());
					res.setMsg(ResponseEnum.SendOboxError.getMsg());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// ===================================================================
	// delete obox
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "delete  obox", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/obox/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delObox(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> oboxRes = feignOboxClient.deleteObox(serialId);
			if (oboxRes.getCode() == ResponseEnum.Success.getCode()) {
				// cound't ensure this operations all success
				feignUserClient.deleteUserOboxByOboxSerialId(serialId);
				feignDeviceClient.deleleDeviceByOboxSerialId(serialId);
			}
			return oboxRes;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// delete device
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "delete  device", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delDevice(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject resDevice = feignDeviceClient.delDevice(serialId);
			if (resDevice.getCode() == ResponseEnum.Success.getCode()) {
				feignUserClient.deleteUserDeviceBySerialId(serialId);
			}
			return resDevice;

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// delete scene
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "delete  scene", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject delScene(@PathVariable(required = true) Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject resScene = feignSceneClient.deleteScene(sceneNumber);
			if (resScene.getCode() == ResponseEnum.Success.getCode()) {
				feignUserClient.deleteUserSceneBySceneNumber(sceneNumber);
			}
			return resScene;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// query obox by page
	@ApiOperation(value = "get obox by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/obox/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TObox>> getOboxByUserAndPage(@PathVariable Integer pageIndex,
			@PathVariable Integer pageSize) {
		ResponseObject<List<TObox>> res = new ResponseObject<List<TObox>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getUsername() != null && !principal.getUsername().equals("")) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignOboxClient.getOboxByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setCode(ResponseEnum.UnKonwUser.getCode());
				res.setMsg(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// query device by page
	@ApiOperation(value = "get device by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/device/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(@PathVariable Integer pageIndex,
			@PathVariable Integer pageSize) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getUsername() != null && !principal.getUsername().equals("")) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignDeviceClient.getDeviceByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setCode(ResponseEnum.UnKonwUser.getCode());
				res.setMsg(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "get scene by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<SceneInfo>> getSceneByUserAndPage(@PathVariable Integer pageIndex,
			@PathVariable Integer pageSize) {
		ResponseObject<List<SceneInfo>> res = new ResponseObject<List<SceneInfo>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal.getUsername() != null && !principal.getUsername().equals("")) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignSceneClient.getSceneByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setCode(ResponseEnum.UnKonwUser.getCode());
				res.setMsg(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
}
