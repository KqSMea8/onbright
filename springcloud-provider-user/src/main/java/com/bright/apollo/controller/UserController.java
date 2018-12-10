package com.bright.apollo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.cache.UserCacheService;
import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.common.entity.TCreateTableSql;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserOperation;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.constant.Constant;
import com.bright.apollo.constant.WxConstant;
import com.bright.apollo.http.HttpRequester;
import com.bright.apollo.http.HttpRespons;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.CreateTableLogService;
import com.bright.apollo.service.CreateTableSqlService;
import com.bright.apollo.service.SmsService;
import com.bright.apollo.service.UserAliDevService;
import com.bright.apollo.service.UserDeviceService;
import com.bright.apollo.service.UserOboxService;
import com.bright.apollo.service.UserOperationService;
import com.bright.apollo.service.UserSceneService;
import com.bright.apollo.service.UserService;
import com.bright.apollo.service.WxService;
import com.bright.apollo.service.impl.AsyncServiceImpl;
import com.bright.apollo.tool.Base64Util;
import com.bright.apollo.tool.HttpUtil;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.tool.NumberHelper;
import com.bright.apollo.tool.RandomUtil;
import com.bright.apollo.tool.Verify;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月9日
 * @Version:1.1.0
 */
@RequestMapping("user")
@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private UserCacheService userCacheService;
	@Autowired
	private SmsService smsService;
	//@Autowired
	//private MsgService msgService;
	@Autowired
	private WxService wxService;
	@Autowired
	private UserOboxService userOboxService;
	@Autowired
	private UserDeviceService userDeviceService;
	@Autowired
	private UserOperationService userOperationService;
	@Autowired
	private CreateTableLogService createTableLogService;
	@Autowired
	private CreateTableSqlService createTableSqlService;
	@Autowired
	private UserSceneService userSceneService;
	@Autowired
	private UserAliDevService userAliDevService;
	@Autowired
	private AsyncServiceImpl asyncServiceImpl;
	@SuppressWarnings("rawtypes")
	@GetMapping("/sendCodeToMobile/{mobile}")
	public ResponseObject sendCodeToMobile(@PathVariable String mobile) {
		ResponseObject res = new ResponseObject();
		try {
			if (!Verify.checkCellphone(mobile)) {
				res.setStatus(ResponseEnum.ErrorMobile.getStatus());
				res.setMessage(ResponseEnum.ErrorMobile.getMsg());
				return res;
			}
			TUser tUser = userService.queryUserByName(mobile);
			if (tUser != null) {
				res.setStatus(ResponseEnum.ExistMobile.getStatus());
				res.setMessage(ResponseEnum.ExistMobile.getMsg());
				return res;
			}
			int code = RandomUtil.makeCode();
			smsService.sendAuthCode(code, mobile);
			//msgService.sendAuthCode(code, mobile);
			userCacheService.saveCode(mobile, code);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@GetMapping("/sendCodeToApp/{Appkey}")
	public ResponseObject<Map<String, Object>> sendCodeToApp(@PathVariable(value="Appkey") String Appkey) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {			 
			int code = RandomUtil.makeCode();
 			userCacheService.saveCode(Appkey, code);
 			Map<String, Object> map=new HashMap<String, Object>();
 			map.put("code", code);
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

	@SuppressWarnings("rawtypes")
	@GetMapping("/register/{mobile}/{code}/{pwd}")
	public ResponseObject register(@PathVariable String mobile, @PathVariable Integer code, @PathVariable String pwd) {
		ResponseObject res = new ResponseObject();
		try {
			if (!Verify.checkCellphone(mobile)) {
				res.setStatus(ResponseEnum.ErrorMobile.getStatus());
				res.setMessage(ResponseEnum.ErrorMobile.getMsg());
				return res;
			}
			TUser tUser = userService.queryUserByName(mobile);
			if (tUser != null) {
				res.setStatus(ResponseEnum.ExistMobile.getStatus());
				res.setMessage(ResponseEnum.ExistMobile.getMsg());
				return res;
			}
			String saveCode = userCacheService.getCode(mobile);
			if (!NumberHelper.isNumeric(saveCode) || Integer.parseInt(saveCode) != code) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			userService.addUser(mobile, pwd);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/wxLogin", method = RequestMethod.POST)
	public ResponseObject wxLogin(@RequestParam String code) {
		ResponseObject res = new ResponseObject();
		try {
			JSONObject wxToken = wxService.getWxToken(code);
			if (wxToken == null || !wxToken.has("access_token") || !wxToken.has("openid")) {
				res.setStatus(ResponseEnum.WxLoginError.getStatus());
				res.setMessage(ResponseEnum.WxLoginError.getMsg());
			} else {
				String token = wxToken.getString("access_token");
				String openId = wxToken.getString("openid");
				JSONObject wxUserInfo = wxService.getWxUserInfo(token, openId);
				if (wxUserInfo == null || !wxUserInfo.has("headimgurl") || !wxUserInfo.has("nickname")
						|| !wxUserInfo.has("sex")) {
					res.setStatus(ResponseEnum.WxLoginError.getStatus());
					res.setMessage(ResponseEnum.WxLoginError.getMsg());
				} else {
					userCacheService.saveOpenId(openId);
					final JSONObject requestByBasic = HttpUtil.requestByBasic(
							Constant.OAUTH + "?grant_type=password&username" + openId + "&password=" + openId,
							Constant.APPID, Constant.SECRET);
					res.setStatus(ResponseEnum.AddSuccess.getStatus());
					res.setMessage(ResponseEnum.AddSuccess.getMsg());
					res.setData(requestByBasic);
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (requestByBasic.has("openid") && requestByBasic.has("headimgurl")
									&& requestByBasic.has("nickname")) {
								TUser tuser = userService.queryUserByOpenId(requestByBasic.getString("openid"));
								if (tuser == null)
									userService.saveUserByWeiXinInfo(requestByBasic.getString("openid"),
											requestByBasic.getString("headimgurl"),
											requestByBasic.getString("nickname"));
							}
						}
					}).start();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/forget/{mobile}")
	public ResponseObject forget(@PathVariable String mobile) {
		ResponseObject res = new ResponseObject();
		try {
			if (!Verify.checkCellphone(mobile)) {
				res.setStatus(ResponseEnum.ErrorMobile.getStatus());
				res.setMessage(ResponseEnum.ErrorMobile.getMsg());
				return res;
			}
			TUser tUser = userService.queryUserByName(mobile);
			if (tUser == null) {
				res.setStatus(ResponseEnum.NoExistMobile.getStatus());
				res.setMessage(ResponseEnum.NoExistMobile.getMsg());
				return res;
			}
			int code = RandomUtil.makeCode();
			smsService.sendCode(mobile, code+"");
			//msgService.sendCode(code, mobile);
			userCacheService.saveCode(mobile, code);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getUser/{userName}", method = RequestMethod.GET)
	public ResponseObject<TUser> getUser(@PathVariable(required = true) String userName) {
		ResponseObject<TUser> res = new ResponseObject<TUser>();
		try {
			TUser tuser = null;
			if(NumberHelper.isNumeric(userName))
				tuser=userService.queryUserByName(userName);
			else
				tuser=userService.queryUserByOpenId(userName);
			if (tuser == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(tuser);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getUserById/{id}", method = RequestMethod.GET)
	public ResponseObject<TUser> getUserById(@PathVariable(required = true) Integer id) {
		ResponseObject<TUser> res = new ResponseObject<TUser>();
		try {
			TUser tuser = userService.getUserByUserId(id);
			if (tuser == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(tuser);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addUserObox", method = RequestMethod.POST)
	public ResponseObject addUserObox(@RequestBody(required = true) TUserObox tUserObox) {
		ResponseObject res = new ResponseObject();
		try {
			TUser tuser = userService.getUserByUserId(tUserObox.getUserId());
			if (tuser == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			userService.addUserObox(tUserObox);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
			// res.setData(tuser);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addUserDevice", method = RequestMethod.POST)
	public ResponseObject addUserDevice(@RequestBody(required = true) TUserDevice tUserDevice) {
		ResponseObject res = new ResponseObject();
		try {
			userService.addUserDevice(tUserDevice);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
			// res.setData(tuser);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addUserScene", method = RequestMethod.POST)
	public ResponseObject<TUserScene> addUserScene(@RequestBody(required = true) TUserScene tUserScene) {
		ResponseObject<TUserScene> res = new ResponseObject<TUserScene>();
		try {
			userService.addUserScene(tUserScene);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
			res.setData(userService.getUserSceneByUserIdAndSceneNumber(tUserScene.getUserId(),
					tUserScene.getSceneNumber()));
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getUserDevcieByUser/{userId}", method = RequestMethod.GET)
	public ResponseObject<List<TUserDevice>> getUserDevcieByUser(
			@PathVariable(required = true, value = "userId") Integer userId) {
		ResponseObject<List<TUserDevice>> res = new ResponseObject<List<TUserDevice>>();
		try {
			List<TUserDevice> list = userService.getListOfUserDeviceByUserId(userId);
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

	@RequestMapping(value = "/getUserObox/{userId}/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<TUserObox> getUserObox(@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		ResponseObject<TUserObox> res = new ResponseObject<TUserObox>();
		try {
			TUserObox tuserObox = userOboxService.getUserOboxByUserIdAndOboxSerialId(userId, oboxSerialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(tuserObox);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getUserDevcieByUserIdAndSerialId/{userId}/{deviceSerialId}", method = RequestMethod.GET)
	public ResponseObject<TUserDevice> getUserDevcieByUserIdAndSerialId(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(required = true, value = "deviceSerialId") String deviceSerialId) {
		ResponseObject<TUserDevice> res = new ResponseObject<TUserDevice>();
		try {
			TUserDevice tuserDevice = userDeviceService.getUserDeviceByUserIdAndSerialId(userId, deviceSerialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(tuserDevice);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getUserOperation/{fromDate}/{toDate}/{serialId}/{startIndex}/{countIndex}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> getUserOperation(
			@PathVariable(required = true, value = "fromDate") Long fromDate,
			@PathVariable(required = true, value = "toDate") Long toDate,
			@PathVariable(required = true, value = "serialId") String serialId,
			@PathVariable(required = true, value = "startIndex") Integer startIndex,
			@PathVariable(required = true, value = "countIndex") Integer countIndex) {
		ResponseObject<List<TUserOperation>> res = new ResponseObject<List<TUserOperation>>();
		try {
			/*
			 * List<TUserOperation> operations =
			 * UserBusiness.queryUserOperation( from, to, serialId,
			 * Integer.parseInt(startIndex),
			 */
			res.setData(userOperationService.getUserOperation(fromDate, toDate, serialId, startIndex, countIndex));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/queryUserOperationByMonth/{tableName}/{serialId}/{day}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperationByMonth(
			@PathVariable(required = true, value = "tableName") String tableName,
			@PathVariable(required = true, value = "serialId") String serialId,
			@PathVariable(required = true, value = "day") String day) {
		ResponseObject<List<TUserOperation>> res = new ResponseObject<List<TUserOperation>>();
		try {
			/*
			 * List<TUserOperation> operations =
			 * UserBusiness.queryUserOperationByMonth(
			 * SubTableConstant.T_USER_OPERATION_SUFFIX +
			 * DateHelper.formatDate(new Date().getTime(),
			 * DateHelper.FORMATMONTH), serialId, days.get(i).getDay());
			 */
			res.setData(userOperationService.queryUserOperationByMonth(tableName, serialId, day));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	@RequestMapping(value = "/queryUserOperationByMonthDayList/{tableName}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperationByMonthDayList(
			@PathVariable(required = true, value = "tableName") String tableName) {
		ResponseObject<List<TUserOperation>> res = new ResponseObject<List<TUserOperation>>();
		try {
			res.setData(userOperationService.queryUserOperationByMonthDayList(tableName));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/listCreateTableLogByNameWithLike/{tUserOperationSuffix}", method = RequestMethod.GET)
	public ResponseObject<List<TCreateTableLog>> listCreateTableLogByNameWithLike(
			@PathVariable(required = true, value = "tUserOperationSuffix") String tUserOperationSuffix) {
		ResponseObject<List<TCreateTableLog>> res = new ResponseObject<List<TCreateTableLog>>();
		try {
			res.setData(createTableLogService.listCreateTableLogByNameWithLike(tUserOperationSuffix));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/queryUserOperation/{name}/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperation(
			@PathVariable(required = true, value = "name") String name,
			@PathVariable(required = true, value = "serialId") String serialId) {
		ResponseObject<List<TUserOperation>> res = new ResponseObject<List<TUserOperation>>();
		try {
			res.setData(userOperationService.queryUserOperation(name, serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/dropTable/{tableName}", method = RequestMethod.DELETE)
	public ResponseObject dropTable(@PathVariable(required = true, value = "tableName") String tableName) {
		ResponseObject res = new ResponseObject();
		try {
			createTableLogService.dropTable(tableName);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param createTableSql
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/createTable/{createTableSql}", method = RequestMethod.POST)
	public ResponseObject createTable(@PathVariable(required = true, value = "createTableSql") String createTableSql) {
		ResponseObject res = new ResponseObject();
		try {
			createTableLogService.createTable(createTableSql);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param prefix
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryTCreateTableSqlByprefix/{prefix}", method = RequestMethod.GET)
	public ResponseObject<TCreateTableSql> queryTCreateTableSqlByprefix(
			@PathVariable(required = true, value = "prefix") String prefix) {
		ResponseObject<TCreateTableSql> res = new ResponseObject<TCreateTableSql>();
		try {
			res.setData(createTableSqlService.queryTCreateTableSqlByprefix(prefix));
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param tCreateTableLog
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addCreateTableLog", method = RequestMethod.POST)
	public ResponseObject addCreateTableLog(@RequestBody TCreateTableLog tCreateTableLog) {
		ResponseObject res = new ResponseObject();
		try {
			createTableLogService.addCreateTableLog(tCreateTableLog);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteUserScene/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserSceneBySceneNumber(
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			userSceneService.deleteUserSceneBySceneNum(sceneNumber);
			// createTableLogService.addCreateTableLog(tCreateTableLog);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param code
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/wxLogin/{code}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> wxLogin(@PathVariable(required = true, value = "code") Integer code) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("appid", WxConstant.WXAPPID);
			param.put("secret", WxConstant.WXSECRET);
			param.put("js_code", code);
			param.put("grant_type", WxConstant.WXGRANTTYPE);
			HttpRequester requestWX = new HttpRequester();
			HttpRespons hr = requestWX.sendPost(WxConstant.WXURL, param);
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param serialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/getUserDeviceExceptRoot/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<TUserDevice>> getUserDeviceExceptRoot(@PathVariable(required = true, value = "serialId")String serialId){
		ResponseObject<List<TUserDevice>> res=new ResponseObject<List<TUserDevice>>();
		try {
			res.setData(userDeviceService.getUserDevicesBySerialId(serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param from
	 * @param to
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryUserOperationByDate/{from}/{to}/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperationByDate(
			@PathVariable(required = true, value = "from") long from,
			@PathVariable(required = true, value = "to") long to,
			@PathVariable(required = true, value = "serialId") String serialId){
		ResponseObject<List<TUserOperation>> res=new ResponseObject<List<TUserOperation>>();
		try {
			res.setData(userOperationService.queryUserOperationByDate(from,to,serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/register/{mobile}/{code}/{pwd}/{appkey}", method = RequestMethod.POST)
	public ResponseObject register(@PathVariable(required = true, value = "mobile") String mobile,
			@PathVariable(required = true, value = "code") String code,
			@PathVariable(required = true, value = "pwd") String pwd,
			@PathVariable(required = true, value = "appkey") String appkey){
		ResponseObject res=new ResponseObject();
		try {
		/*	if (!Verify.checkCellphone(mobile)) {
				res.setStatus(ResponseEnum.ErrorMobile.getStatus());
				res.setMessage(ResponseEnum.ErrorMobile.getMsg());
				return res;
			}*/
			TUser tUser = userService.queryUserByName(mobile);
			if (tUser != null) {
				res.setStatus(ResponseEnum.ExistMobile.getStatus());
				res.setMessage(ResponseEnum.ExistMobile.getMsg());
				return res;
			} 
			String saveCode = userCacheService.getCode(appkey);
			if (StringUtils.isEmpty(saveCode)||!NumberHelper.isNumeric(saveCode) || !code.equals(saveCode)) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			String base64Encrypt = Base64Util.base64Encrypt(pwd.toString().getBytes());
			userService.addUser(mobile, MD5.getMD5Str(base64Encrypt+pwd));
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteUserDevice/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserDeviceBySerialId(
			@PathVariable(required = true, value = "serialId") String serialId){
		ResponseObject res=new ResponseObject();
		try {
			userDeviceService.deleteUserDeviceBySerialId(serialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	
	}
	/**
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteUserObox/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserOboxByOboxSerialId(
			@PathVariable(required = true, value = "serialId") String serialId){
		ResponseObject res=new ResponseObject();
		try {
			userOboxService.delectUserOboxByOboxSerialId(serialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param user  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateUserPwd", method = RequestMethod.PUT)
	public ResponseObject updateUserPwd(@RequestBody TUser user){
		ResponseObject res=new ResponseObject();
		try {
			if(user==null||user.getId()==null||StringUtils.isEmpty(user.getPassword())){
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}else{
				String base64Encrypt = Base64Util.base64Encrypt(user.getPassword().toString().getBytes());
				user.setPassword(MD5.getMD5Str(base64Encrypt+user.getPassword()));
				userService.updateUser(user);
			}
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteUserAliDev/{deviceSerialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserAliDev(@PathVariable(required = true, value = "deviceSerialId")  String deviceSerialId){
		ResponseObject res=new ResponseObject();
		try {
			userAliDevService.deleteUserAliDev(deviceSerialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param tUserAliDev  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addUserAliDev", method = RequestMethod.POST)
	public ResponseObject addUserAliDev(@RequestBody TUserAliDevice tUserAliDev){
		ResponseObject res=new ResponseObject();
		try {
			userAliDevService.addUserAliDev(tUserAliDev);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteUserDeviceByOboxSerialId/{oboxSerialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserDeviceByOboxSerialId(@PathVariable(required = true, value = "oboxSerialId")String oboxSerialId){
		ResponseObject res=new ResponseObject();
		try {
			userDeviceService.deleteUserDeviceByOboxSerialId(oboxSerialId);
			//userAliDevService.addUserAliDev(tUserAliDev);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**
	 * @param deviceSerialId
	 * @param oboxSerialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addUserDeviceBySerialIdAndOboxSerialId/{deviceSerialId}/{oboxSerialId}", method = RequestMethod.POST)
	public ResponseObject addUserDeviceBySerialIdAndOboxSerialId(
			@PathVariable(required = true, value = "deviceSerialId") String deviceSerialId,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId){
		ResponseObject res=new ResponseObject();
		try {
			asyncServiceImpl.addUserDeviceBySerialIdAndOboxSerialId(deviceSerialId,oboxSerialId);
			//userDeviceService.deleteUserDeviceByOboxSerialId(oboxSerialId);
 			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	
	}
}
