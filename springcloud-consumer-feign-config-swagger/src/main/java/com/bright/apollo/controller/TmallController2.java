package com.bright.apollo.controller;


import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.redis.RedisBussines;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.transition.TMallDeviceAdapter;
import com.bright.apollo.transition.TMallTemplate;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 *@Title:
 *@Description:
 *@Author:JettyLiu
 *@Since:2018年3月16日
 *@Version:1.1.0
 */
@RequestMapping("tmall2")
@RestController
public class TmallController2 {
	private static final Logger logger = LoggerFactory.getLogger(TmallController2.class);

	@Autowired
	private RedisBussines redisBussines;

	@Autowired
	private FeignDeviceClient feignDeviceClient;

	@Autowired
	private FeignUserClient feignUserClient;

	@Autowired
	private TMallTemplate tMallTemplate;

	@Autowired
	private FacadeController facadeController;

	private ReentrantLock lock = new ReentrantLock();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/tmallCmd2", method = RequestMethod.POST,produces = "application/json ;charset=UTF-8")
	@ResponseBody
	public Object tmallCmd(@RequestBody Object object) throws Exception {

		logger.info("====== messageID ======"+object);
		Map<String,Object> requestMap = (Map<String, Object>) object;
		Map<String,Object> requestHeaderMap = (Map<String, Object>) requestMap.get("header");
		JSONObject map = new JSONObject();
		JSONObject headerMap = new JSONObject();
		JSONObject playloadMap = new JSONObject();
		TMallDeviceAdapter adapter = null;
		Map<String,Object> payload = (Map<String,Object>) requestMap.get("payload");
		String accessToken = (String)payload.get("accessToken");
		logger.info(" ===== accessToken ====== "+accessToken);
		OAuth2Authentication defaultOAuth2AccessToken = redisBussines.getObject("auth:"+accessToken,OAuth2Authentication.class);
		logger.info(" ===== redisToken ====== "+defaultOAuth2AccessToken);

		if(requestHeaderMap.get("namespace").equals("AliGenie.Iot.Device.Discovery")){//天猫精灵发现设备

			headerMap.put("namespace","AliGenie.Iot.Device.Discovery");
			headerMap.put("name","DiscoveryDevicesResponse");
			headerMap.put("messageId",(String)requestHeaderMap.get("messageId"));
			headerMap.put("payLoadVersion","1");
			map.put("header",headerMap);
			JSONArray jsonArray = new JSONArray();
			templateScan(jsonArray);//展示使用(日后可删除)
			playloadMap.put("devices",jsonArray);
			map.put("payload",playloadMap);
			headerMap.put("namespace","AliGenie.Iot.Device.Discovery");
			headerMap.put("name","DiscoveryDevicesResponse");
			headerMap.put("messageId",(String)requestHeaderMap.get("messageId"));
			headerMap.put("payLoadVersion","1");
			map.put("header",headerMap);
		}else if(requestHeaderMap.get("namespace").equals("AliGenie.Iot.Device.Control")){//天猫精灵控制设备
			try {
				enableSSl();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			Map<String,Object> playLoadMap = (Map<String, Object>) requestMap.get("payload");

            Map<String,Object> header = (Map<String, Object>) requestMap.get("header");
			String name = (String)requestHeaderMap.get("name");
			String deviceId = (String)playLoadMap.get("deviceId");
			String originalId = deviceId;
			String[] deviceIdArr = deviceId.split("_");
			deviceId = deviceIdArr[0];

			//====== 生成httpsClient begin ======
			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
			HttpPost httpPost =new HttpPost("https://cloud.on-bright.com/common");
			Registry<ConnectionSocketFactory>
					socketFactoryRegistry =RegistryBuilder.<ConnectionSocketFactory>create().
					register("http", PlainConnectionSocketFactory.INSTANCE).
					register("https", socketFactory).build();
			PoolingHttpClientConnectionManager connectionManager =
					new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			CloseableHttpClient httpClient = HttpClients.custom().
					setConnectionManager(connectionManager)
					.setDefaultRequestConfig(requestConfig).build();
			//====== 生成httpsClient end ======

			templateControl(name,deviceId,nvps,httpPost,httpClient);//展示台模板(仅供展厅展示使用，日后可删除)
			headerMap.put("namespace","AliGenie.Iot.Device.Control");
			headerMap.put("name",name+"Response");
			headerMap.put("messageId",(String)requestHeaderMap.get("messageId"));
			headerMap.put("payLoadVersion","1");
			map.put("header",headerMap);
			playloadMap.put("deviceId",deviceId);
			map.put("payload",playloadMap);
		}
		logger.info("map ====== "+StringEscapeUtils.unescapeJavaScript(map.toString()));
		return StringEscapeUtils.unescapeJavaScript(map.toString());
	}

	private void templateScan(JSONArray list ) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		JSONArray propertiesJsonArray = new JSONArray();
		JSONObject propertiesMap = new JSONObject();
		propertiesMap.put("name","color");
		propertiesMap.put("value","Red");
		propertiesJsonArray.put(propertiesMap);
		JSONObject extensionsMap = new JSONObject();
		extensionsMap.put("extension1","");
		extensionsMap.put("extension2","");
		JSONObject devices = new JSONObject();

		devices.put("deviceId","34ea34cf2e63");
		devices.put("deviceName","大灯");
		devices.put("deviceType","light");
		devices.put("zone","阳台");
		devices.put("brand","1");
		devices.put("model","1");
		devices.put("icon","https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/light.png");
		devices.put("properties",propertiesJsonArray);
		String[] actions = new String[2];
		actions[0] = "TurnOn";
		actions[1] = "TurnOff";
		devices.put("extensions",extensionsMap);

		JSONObject devices2 = new JSONObject();

		devices2.put("deviceId","34ea34cf2e61");
		devices2.put("deviceName","灯");
		devices2.put("deviceType","light");
		devices2.put("zone","客厅");
		devices2.put("brand","1");
		devices2.put("model","1");
		devices2.put("icon","https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/light.png");
		devices2.put("properties",propertiesJsonArray);
		String[] actions2 = new String[2];
		actions2[0] = "TurnOn";
		actions2[1] = "TurnOff";
		devices2.put("extensions",extensionsMap);

		JSONObject devices3 = new JSONObject();

		devices3.put("deviceId","34ea34cf2e69");
		devices3.put("deviceName","窗帘");
		devices3.put("deviceType","curtain");
		devices3.put("zone","");
		devices3.put("brand","1");
		devices3.put("model","1");
		devices3.put("icon","https://raw.githubusercontent.com/onbright-canton/onbrightConfig/master/tmallImg/curtain.png");
		devices3.put("properties",propertiesJsonArray);
		String[] actions3 = new String[2];
		actions3[0] = "TurnOn";
		actions3[1] = "TurnOff";
		devices3.put("extensions",extensionsMap);
		list.put(devices);
		list.put(devices2);
		list.put(devices3);
	}

	private void templateControl(String name,String deviceId,List<NameValuePair> nvps,HttpPost httpPost,CloseableHttpClient httpClient) throws IOException {
		if(name.equals("TurnOn")&&deviceId.equals("34ea34cf2e63")){
			nvps.add(new BasicNameValuePair("CMD", "set_group"));
			nvps.add(new BasicNameValuePair("access_token", "ce9a4a7c-ef10-48f3-a6e2-1f9c0ab5c758"));
			nvps.add(new BasicNameValuePair("operate_type", "06"));
			nvps.add(new BasicNameValuePair("group_id", "104"));
			nvps.add(new BasicNameValuePair("group_state", "ff000000000002"));
			nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute(httpPost);
		}
		if(name.equals("TurnOff")&&deviceId.equals("34ea34cf2e63")){
			nvps.add(new BasicNameValuePair("CMD", "set_group"));
			nvps.add(new BasicNameValuePair("access_token", "ce9a4a7c-ef10-48f3-a6e2-1f9c0ab5c758"));
			nvps.add(new BasicNameValuePair("operate_type", "06"));
			nvps.add(new BasicNameValuePair("group_id", "104"));
			nvps.add(new BasicNameValuePair("group_state", "00000000000002"));
			nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute(httpPost);
		}
		if(name.equals("TurnOn")&&deviceId.equals("34ea34cf2e61")){
			nvps.add(new BasicNameValuePair("CMD", "set_group"));
			nvps.add(new BasicNameValuePair("access_token", "ce9a4a7c-ef10-48f3-a6e2-1f9c0ab5c758"));
			nvps.add(new BasicNameValuePair("operate_type", "06"));
			nvps.add(new BasicNameValuePair("group_id", "105"));
			nvps.add(new BasicNameValuePair("group_state", "ff000000000002"));
			nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute(httpPost);
		}
		if(name.equals("TurnOff")&&deviceId.equals("34ea34cf2e61")){
			nvps.add(new BasicNameValuePair("CMD", "set_group"));
			nvps.add(new BasicNameValuePair("access_token", "ce9a4a7c-ef10-48f3-a6e2-1f9c0ab5c758"));
			nvps.add(new BasicNameValuePair("operate_type", "06"));
			nvps.add(new BasicNameValuePair("group_id", "105"));
			nvps.add(new BasicNameValuePair("group_state", "00000000000002"));
			nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute(httpPost);
		}
		if(name.equals("TurnOn")&&deviceId.equals("34ea34cf2e69")){
			nvps.add(new BasicNameValuePair("CMD", "setting_node_status"));
			nvps.add(new BasicNameValuePair("access_token", "ce9a4a7c-ef10-48f3-a6e2-1f9c0ab5c758"));
			nvps.add(new BasicNameValuePair("serialId", "185f010000"));
			nvps.add(new BasicNameValuePair("status", "02000000000000"));
			nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute(httpPost);
		}
		if(name.equals("TurnOff")&&deviceId.equals("34ea34cf2e69")){
			nvps.add(new BasicNameValuePair("CMD", "setting_node_status"));
			nvps.add(new BasicNameValuePair("access_token", "ce9a4a7c-ef10-48f3-a6e2-1f9c0ab5c758"));
			nvps.add(new BasicNameValuePair("serialId", "185f010000"));
			nvps.add(new BasicNameValuePair("status", "00000000000000"));
			nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpClient.execute(httpPost);
		}
	}

	private static TrustManager manager = new X509TrustManager() {
		@Override
		public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private static void enableSSl() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null,new TrustManager[]{manager},null);
		socketFactory = new SSLConnectionSocketFactory(context,NoopHostnameVerifier.INSTANCE);
	}

	private static SSLConnectionSocketFactory socketFactory;

}
