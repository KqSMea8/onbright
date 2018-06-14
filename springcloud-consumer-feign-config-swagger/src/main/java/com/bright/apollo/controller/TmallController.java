package com.bright.apollo.controller;


import com.bright.apollo.redis.RedisBussines;

import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
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
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月16日  
 *@Version:1.1.0  
 */
//@Api("device Controller")
@RequestMapping("tmall")
@RestController
public class TmallController {
	Logger logger = Logger.getLogger(TmallController.class);

	@Autowired
	private RedisBussines redisBussines;

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/tmallCmd", method = RequestMethod.POST)
	public Object tmallCmd(@RequestBody Object object, HttpServletResponse response) throws IOException {

		logger.info("====== messageID ======"+object);
		Map<String,Object> requestMap = (Map<String, Object>) object;
		Map<String,Object> requestHeaderMap = (Map<String, Object>) requestMap.get("header");
		JSONObject map = new JSONObject();
		JSONObject headerMap = new JSONObject();
		JSONObject playloadMap = new JSONObject();
		String responstStr = "Response";
//		response.sendRedirect("http://localhost:8201/user/forget/13522333323");
		if(requestHeaderMap.get("namespace").equals("AliGenie.Iot.Device.Discovery")){//天猫精灵发现设备



			headerMap.put("namespace","AliGenie.Iot.Device.Discovery");
			headerMap.put("name","DiscoveryDevicesResponse");
			headerMap.put("messageId",requestHeaderMap.get("messageId"));
			headerMap.put("payLoadVersion","1");
			map.put("header",headerMap);

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

			devices.put("deviceId","34ea34cf2e61");//34ea34cf2e63
			devices.put("deviceName","大灯");
			devices.put("deviceType","light");
			devices.put("zone","阳台");
			devices.put("brand","1");
			devices.put("model","1");
			devices.put("icon","https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/uploads/aicloud/aicloud-proxy-service/41baa00903a71c97e3533cf4e19a88bb/image.png");
			devices.put("properties",propertiesJsonArray);
			String[] actions = new String[2];
			actions[0] = "TurnOn";
			actions[1] = "TurnOff";
			devices.put("actions",actions);
			devices.put("extensions",extensionsMap);

			JSONObject devices2 = new JSONObject();

			devices2.put("deviceId","34ea34cf2e63");//34ea34cf2e61
			devices2.put("deviceName","灯");
			devices2.put("deviceType","light");
			devices2.put("zone","客厅");
			devices2.put("brand","1");
			devices2.put("model","1");
			devices2.put("icon","https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/uploads/aicloud/aicloud-proxy-service/41baa00903a71c97e3533cf4e19a88bb/image.png");
			devices2.put("properties",propertiesJsonArray);
			String[] actions2 = new String[2];
			actions2[0] = "TurnOn";
			actions2[1] = "TurnOff";
			devices2.put("actions",actions);
			devices2.put("extensions",extensionsMap);

			JSONObject devices3 = new JSONObject();

			devices3.put("deviceId","34ea34cf2e69");
			devices3.put("deviceName","窗帘");
			devices3.put("deviceType","curtain");
			devices3.put("zone","");
			devices3.put("brand","1");
			devices3.put("model","1");
			devices3.put("icon","https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/uploads/aicloud/aicloud-proxy-service/41baa00903a71c97e3533cf4e19a88bb/image.png");
			devices3.put("properties",propertiesJsonArray);
			String[] actions3 = new String[2];
			actions3[0] = "TurnOn";
			actions3[1] = "TurnOff";
			devices3.put("actions",actions);
			devices3.put("extensions",extensionsMap);


			List<JSONObject> list = new ArrayList<JSONObject>();
			list.add(devices);
			list.add(devices2);
			list.add(devices3);
			jsonArray.put(list);
			System.out.println("jsonArray ====== "+jsonArray);
			playloadMap.put("devices",list);

			map.put("payload",playloadMap);

		}else{//天猫精灵控制设备
			try {
				enableSSl();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
			Map<String,Object> playLoadMap = (Map<String, Object>) requestMap.get("payload");
			String name = (String)requestHeaderMap.get("name");
			String deviceId = (String)playLoadMap.get("deviceId");

			RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
			HttpPost httpPost =new HttpPost("https://cloud.on-bright.com/common");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Registry<ConnectionSocketFactory> socketFactoryRegistry =
					RegistryBuilder.<ConnectionSocketFactory>create().
							register("http", PlainConnectionSocketFactory.INSTANCE).
							register("https", socketFactory).build();
			PoolingHttpClientConnectionManager connectionManager =
					new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			CloseableHttpClient httpClient = HttpClients.custom().
					setConnectionManager(connectionManager)
					.setDefaultRequestConfig(requestConfig).build();

			if(name.equals("TurnOn")&&deviceId.equals("34ea34cf2e63")){
				nvps.add(new BasicNameValuePair("CMD", "set_group"));
				nvps.add(new BasicNameValuePair("access_token", "b0a81b1b-844e-4fe5-a52c-251fbbbe4db3"));
				nvps.add(new BasicNameValuePair("operate_type", "06"));
				nvps.add(new BasicNameValuePair("group_id", "104"));
				nvps.add(new BasicNameValuePair("group_state", "ff000000000002"));
				nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				httpClient.execute(httpPost);
//				("https://cloud.on-bright.com/common?CMD=set_group&access_token=b0a81b1b-844e-4fe5-a52c-251fbbbe4db3&operate_type=06&group_id=104&group_state=ff000000000002&appkey=00000000-2898-fa39-a85f-89320033c587");
			}
			if(name.equals("TurnOff")&&deviceId.equals("34ea34cf2e63")){
				nvps.add(new BasicNameValuePair("CMD", "set_group"));
				nvps.add(new BasicNameValuePair("access_token", "b0a81b1b-844e-4fe5-a52c-251fbbbe4db3"));
				nvps.add(new BasicNameValuePair("operate_type", "06"));
				nvps.add(new BasicNameValuePair("group_id", "104"));
				nvps.add(new BasicNameValuePair("group_state", "00000000000002"));
				nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				httpClient.execute(httpPost);
//				response.sendRedirect("https://cloud.on-bright.com/common?CMD=set_group&access_token=b0a81b1b-844e-4fe5-a52c-251fbbbe4db3&operate_type=06&group_id=104&group_state=00000000000002&appkey=00000000-2898-fa39-a85f-89320033c587");
			}
			if(name.equals("TurnOn")&&deviceId.equals("34ea34cf2e61")){
				nvps.add(new BasicNameValuePair("CMD", "set_group"));
				nvps.add(new BasicNameValuePair("access_token", "b0a81b1b-844e-4fe5-a52c-251fbbbe4db3"));
				nvps.add(new BasicNameValuePair("operate_type", "06"));
				nvps.add(new BasicNameValuePair("group_id", "105"));
				nvps.add(new BasicNameValuePair("group_state", "ff000000000002"));
				nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				httpClient.execute(httpPost);
//				("https://cloud.on-bright.com/common?CMD=set_group&access_token=b0a81b1b-844e-4fe5-a52c-251fbbbe4db3&operate_type=06&group_id=104&group_state=ff000000000002&appkey=00000000-2898-fa39-a85f-89320033c587");
			}
			if(name.equals("TurnOff")&&deviceId.equals("34ea34cf2e61")){
				nvps.add(new BasicNameValuePair("CMD", "set_group"));
				nvps.add(new BasicNameValuePair("access_token", "b0a81b1b-844e-4fe5-a52c-251fbbbe4db3"));
				nvps.add(new BasicNameValuePair("operate_type", "06"));
				nvps.add(new BasicNameValuePair("group_id", "105"));
				nvps.add(new BasicNameValuePair("group_state", "00000000000002"));
				nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				httpClient.execute(httpPost);
//				response.sendRedirect("https://cloud.on-bright.com/common?CMD=set_group&access_token=b0a81b1b-844e-4fe5-a52c-251fbbbe4db3&operate_type=06&group_id=104&group_state=00000000000002&appkey=00000000-2898-fa39-a85f-89320033c587");
			}
			if(name.equals("TurnOn")&&deviceId.equals("34ea34cf2e69")){
				nvps.add(new BasicNameValuePair("CMD", "setting_node_status"));
				nvps.add(new BasicNameValuePair("access_token", "b0a81b1b-844e-4fe5-a52c-251fbbbe4db3"));
				nvps.add(new BasicNameValuePair("serialId", "185f010000"));
				nvps.add(new BasicNameValuePair("status", "02000000000000"));
				nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				httpClient.execute(httpPost);
//				("https://cloud.on-bright.com/common?CMD=set_group&access_token=b0a81b1b-844e-4fe5-a52c-251fbbbe4db3&operate_type=06&group_id=104&group_state=ff000000000002&appkey=00000000-2898-fa39-a85f-89320033c587");
			}
			if(name.equals("TurnOff")&&deviceId.equals("34ea34cf2e69")){
				nvps.add(new BasicNameValuePair("CMD", "setting_node_status"));
				nvps.add(new BasicNameValuePair("access_token", "b0a81b1b-844e-4fe5-a52c-251fbbbe4db3"));
				nvps.add(new BasicNameValuePair("serialId", "185f010000"));
				nvps.add(new BasicNameValuePair("status", "00000000000000"));
				nvps.add(new BasicNameValuePair("appkey", "00000000-2898-fa39-a85f-89320033c587"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				httpClient.execute(httpPost);
//				response.sendRedirect("https://cloud.on-bright.com/common?CMD=set_group&access_token=b0a81b1b-844e-4fe5-a52c-251fbbbe4db3&operate_type=06&group_id=104&group_state=00000000000002&appkey=00000000-2898-fa39-a85f-89320033c587");
			}
			headerMap.put("namespace","AliGenie.Iot.Device.Control");
			headerMap.put("name",name+"Response");
			headerMap.put("messageId",requestHeaderMap.get("messageId"));
			headerMap.put("payLoadVersion","1");
			map.put("header",headerMap);
			playloadMap.put("deviceId",playLoadMap.get("deviceId"));
			map.put("payload",playloadMap);
		}
		logger.info("map ====== "+map);
		return map.toString();
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
