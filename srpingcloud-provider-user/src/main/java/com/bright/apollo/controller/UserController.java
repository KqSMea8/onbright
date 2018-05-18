package com.bright.apollo.controller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.cache.UserCacheService;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.constant.Constant;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.MsgService;
import com.bright.apollo.service.UserService;
import com.bright.apollo.service.WxService;
import com.bright.apollo.tool.HttpUtil;
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
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private UserService userService;
	@Autowired
	private UserCacheService userCacheService;
	@Autowired
	private MsgService msgService;
	@Autowired
	private WxService wxService;

	@SuppressWarnings("rawtypes")
	@GetMapping("/register/{mobile}")
	public ResponseObject register(@PathVariable String mobile) {
		ResponseObject res = new ResponseObject();
		try {
			if (!Verify.checkCellphone(mobile)) {
				res.setCode(ResponseEnum.ErrorMobile.getCode());
				res.setMsg(ResponseEnum.ErrorMobile.getMsg());
				return res;
			}
			TUser tUser = userService.queryUserByName(mobile);
			if (tUser != null) {
				res.setCode(ResponseEnum.ExistMobile.getCode());
				res.setMsg(ResponseEnum.ExistMobile.getMsg());
				return res;
			}
			int code = RandomUtil.makeCode();
			msgService.sendAuthCode(code, mobile);
			userCacheService.saveCode(mobile, code);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
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
				res.setCode(ResponseEnum.WxLoginError.getCode());
				res.setMsg(ResponseEnum.WxLoginError.getMsg());
			} else {
				String token = wxToken.getString("access_token");
				String openId = wxToken.getString("openid");
				JSONObject wxUserInfo = wxService.getWxUserInfo(token, openId);
				if (wxUserInfo == null || !wxUserInfo.has("headimgurl") || !wxUserInfo.has("nickname")
						|| !wxUserInfo.has("sex")) {
					res.setCode(ResponseEnum.WxLoginError.getCode());
					res.setMsg(ResponseEnum.WxLoginError.getMsg());
				} else {
					userCacheService.saveOpenId(openId);
					final JSONObject requestByBasic = HttpUtil.requestByBasic(
							Constant.OAUTH + "?grant_type=password&username" + openId + "&password=" + openId,
							Constant.APPID, Constant.SECRET);
					res.setCode(ResponseEnum.Success.getCode());
					res.setMsg(ResponseEnum.Success.getMsg());
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
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/forget/{mobile}")
	public ResponseObject forget(@PathVariable String mobile) {
		ResponseObject res = new ResponseObject();
		try {
			if (!Verify.checkCellphone(mobile)) {
				res.setCode(ResponseEnum.ErrorMobile.getCode());
				res.setMsg(ResponseEnum.ErrorMobile.getMsg());
				return res;
			}
			TUser tUser = userService.queryUserByName(mobile);
			if (tUser == null) {
				res.setCode(ResponseEnum.NoExistMobile.getCode());
				res.setMsg(ResponseEnum.NoExistMobile.getMsg());
				return res;
			}
			int code = RandomUtil.makeCode();
			msgService.sendCode(code, mobile);
			userCacheService.saveCode(mobile, code);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/getUser/{userName}", method = RequestMethod.GET)
	public ResponseObject<TUser> getUser(@PathVariable(required = true) String userName) {
		ResponseObject<TUser> res = new ResponseObject<TUser>();
		try {
			TUser tuser = userService.queryUserByName(userName);
			if (tuser == null) {
				res.setCode(ResponseEnum.UnKonwUser.getCode());
				res.setMsg(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(tuser);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
