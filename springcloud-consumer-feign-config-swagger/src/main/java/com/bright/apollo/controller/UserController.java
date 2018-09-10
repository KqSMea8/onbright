package com.bright.apollo.controller;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.http.HttpWithBasicAuth;
import com.bright.apollo.http.MobClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.tool.Base64Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月10日
 * @Version:1.1.0
 */
@Api("user Controller")
@RequestMapping("user")
@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private FeignUserClient feignUserClient;
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "wx login", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/wxLogin/{code}/{mobile}/{pwd}", method = RequestMethod.POST)
	public ResponseObject<Map<String, Object>> wxLogin(@PathVariable Integer code) {
		ResponseObject<Map<String, Object>> res = null;
		try {
			res = feignUserClient.wxLogin(code);
			
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "sms login", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/smsCodeLogin/{shareSdkAppkey}/{zone}/{mobile}/{code}", method = RequestMethod.POST)
	public ResponseObject<Map<String, Object>> smsCodeLogin(@PathVariable(name="shareSdkAppkey") String shareSdkAppkey,@PathVariable(name="zone") String zone,
			@RequestParam(name="mobile") String mobile,@RequestParam(name="code") String code
			) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String,Object>>();
		try {
			ResponseObject<TUser> userRes = feignUserClient.getUser(mobile);
			if(userRes==null||userRes.getStatus()!=ResponseEnum.SelectSuccess.getStatus()||
					userRes.getData()==null
					){
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			MobClient mobClient = new MobClient();
			mobClient.addParam("appkey", shareSdkAppkey).addParam("phone", mobile)
			.addParam("zone", zone).addParam("code", code);
			mobClient.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			mobClient.addRequestProperty("Accept", "application/json");
			String result = mobClient.post();
			JSONObject object = new JSONObject(result);
			logger.info("====result:"+result);
			if (object.getInt("status")==200) {
				TUser user = userRes.getData();
				ResponseObject<List<OauthClientDetails>> clientsRes = feignUserClient.getClients("password");
				if(clientsRes==null||clientsRes.getData()==null||
						clientsRes.getStatus()!=ResponseEnum.SelectSuccess.getStatus()){
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				List<OauthClientDetails> clients = clientsRes.getData();
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("grant_type", "password");
				map.put("username", user.getUserName());
				//may should decode
				map.put("password", user.getPassword());
				CloseableHttpResponse httpResponse = HttpWithBasicAuth.http(map, clients.get(0));
				if(httpResponse!=null&&httpResponse.getStatusLine().getStatusCode()==200){
					res.setStatus(ResponseEnum.SelectSuccess.getStatus());
					res.setMessage(ResponseEnum.SelectSuccess.getMsg());
					Map<String, Object> hashmap=new HashMap<String, Object>();
					
					return res; 
				}else if(httpResponse!=null){
					logger.error("=== http result:"+EntityUtils.toString(httpResponse.getEntity()));
				}
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}else{
				res.setStatus(ResponseEnum.NoExistCode.getStatus());
				res.setMessage(ResponseEnum.NoExistCode.getMsg());
				return res;
			}
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "register user", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseObject register(@RequestParam(name="appkey") String appkey,@RequestParam(name="mobile") String mobile,
			@RequestParam(name="code") String code,@RequestParam(name="pwd") String pwd
			) {
		ResponseObject res = new ResponseObject();
		try {
			/*if(!map.containsKey("code")||!map.containsKey("mobile")||!map.containsKey("pwd")
					||!map.containsKey("appkey")
					){
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}else{*/
				res = feignUserClient.register(mobile, code, pwd,appkey);
			//}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@Deprecated
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "send", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@GetMapping("/sendCodeToMobile/{mobile}")
	public ResponseObject register(@PathVariable String mobile) {
		ResponseObject res = null;
		try {
			res = feignUserClient.sendCodeToMobile(mobile);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
			return res;
		}
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "send", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@GetMapping("/sendCodeToApp/{appkey}")
	public ResponseObject<Map<String, Object>> sendCodeToApp(@PathVariable String appkey) {
		ResponseObject res = null;
		try {
			res = feignUserClient.sendCodeToApp(appkey);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
			return res;
		}
	}
	@Deprecated
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "forget password", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@GetMapping("/forget/{mobile}")
	public ResponseObject forget(@PathVariable String mobile) {
		ResponseObject res = null;
		try {
			res = feignUserClient.forget(mobile);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
			return res;
		}
	}
	/**
	   * 构造Basic Auth认证头信息
	   * 
	   * @return
	   */
	  private static String getHeader(String appkey,String secrect) {
	    String auth = appkey + ":" + secrect;
	    //encodeBase64();Base64Util.base64Encrypt(fingerRemoteUser.getPwd().getBytes())
	    String encodedAuth = Base64Util.base64Encrypt(auth.getBytes(Charset.forName("US-ASCII")));
	    String authHeader = "Basic " + new String(encodedAuth);
	    System.out.println(authHeader);
	    return authHeader;
	  }
public static void main(String[] args) throws Exception {
	MobClient mobClient = new MobClient();
	mobClient.addParam("grant_type", "password").addParam("username", "13828486833")
	.addParam("password", "12345678").addParam("appkey", "fsfsfdsf").addParam("AppControlbox", "dadad");
	mobClient.setAddress("https://aliiot.on-bright.com/oauth/token");
	mobClient.addRequestProperty("Authorization",getHeader("webApp", "webApp"));
	mobClient.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	mobClient.addRequestProperty("Accept", "application/json");
	String result = mobClient.post();
	System.out.println(result);
}
}
