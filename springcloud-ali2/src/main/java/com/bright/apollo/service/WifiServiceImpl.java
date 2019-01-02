package com.bright.apollo.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.KeyCode;
import com.bright.apollo.common.entity.QueryRemoteBySrcDTO2;
import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TYaokonyunKeyCode;
import com.bright.apollo.controller.AliDeviceController;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月28日  
 *@Version:1.1.0  
 */
@Service
public class WifiServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(WifiServiceImpl.class);
	@Autowired
	private YaoKongYunService yaoKongYunService;
	@Autowired
	private CmdCache cmdCache;
	@Autowired
	@Lazy
	private TopicServer topServer;
	@Autowired
	private AliDeviceConfigService aliDeviceConfigService;
	public boolean controlIr(String serialId,Integer index,String key) throws Exception{
		Map<String, Object> requestMap = new HashMap<String, Object>();
		TYaokonyunKeyCode yaokonyunKeyCode = yaoKongYunService.getYaoKongKeyCodeByKeyAndSerialIdAndIndex(index,serialId,key);
		String brandId = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+index);
		String deviceType = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("deviceType_"+index);
		if(brandId !=null &&!brandId.equals("")&&yaokonyunKeyCode==null){
			List<QueryRemoteBySrcDTO2> dtoSrcList = cmdCache.getIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlListSrc");
			for(QueryRemoteBySrcDTO2 dto :dtoSrcList){
				Integer dotIdx = dto.getIndex();
				if(dotIdx.equals(index)){
					com.alibaba.fastjson.JSONArray jsonArray = dto.getKeys();
					for(int i=0;i<jsonArray.size();i++){
						com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
						Map map = (Map) jsonObject.get("keyCodeMap");
						Iterator keyMapItor = map.keySet().iterator();
						while (keyMapItor.hasNext()){
							if(keyMapItor.next().equals(key)){
								KeyCode keyCode = (KeyCode)map.get(key);
								if(yaokonyunKeyCode==null){
									yaokonyunKeyCode = new TYaokonyunKeyCode();
									yaokonyunKeyCode.setSrc(keyCode.getSrcCode());
								}
							}
						}
					}
				}
			}
		}
		if(yaokonyunKeyCode==null){
			return false;
		}else{
			String srcCode = yaokonyunKeyCode.getSrc();
			logger.info(" src ========= "+srcCode);
			requestMap.put("command","set");
			com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			json.put("functionId",1);
			jsonArray.add(json);
			json.put("data",yaokonyunKeyCode.getSrc());
			requestMap.put("value",jsonArray);
			topServer.pubIrRPC(requestMap,serialId);
			return true;
		}
	}
	@SuppressWarnings("rawtypes")
	public ResponseObject setAliDevice(String deviceId,Object value) throws Exception{
		ResponseObject res = new ResponseObject();
		String val = (String) value;
		JSONArray array = JSONArray.parseArray(val);
		for (int i = 0; i < array.size(); i++) {
			com.alibaba.fastjson.JSONObject jsonObject = array.getJSONObject(i);
			if (jsonObject.getBoolean("data")) {
				jsonObject.put("data", true);
			} else {
				jsonObject.put("data", false);
			}
		}
		net.sf.json.JSONObject object = new net.sf.json.JSONObject();
		object.put("command", "set");
		object.element("value", array);
		// object.put("value", value);
		JSONObject resJson = topServer.requestDev(object, deviceId, array.toJSONString());
		if (resJson == null) {
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
			return res;
		}
		TAliDeviceConfig aliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceId);
		if (aliDeviceConfig != null) {
			aliDeviceConfig.setState(array.toJSONString());
			aliDeviceConfigService.update(aliDeviceConfig);
		}
		logger.info("array ====== " + array.toJSONString());
		res.setData(array.toJSONString());
		res.setStatus(ResponseEnum.SelectSuccess.getStatus());
		res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		return res;
	}
	public static void main(String[] args) {
		JSONArray array = JSONArray.parseArray("[{'data' : 1,'functionId' : 1}]");
		for (int i = 0; i < array.size(); i++) {
			com.alibaba.fastjson.JSONObject jsonObject = array.getJSONObject(i);
			if (jsonObject.getBoolean("data")) {
				jsonObject.put("data", true);
			} else {
				jsonObject.put("data", false);
			}
		}
		System.out.println(array.toJSONString());
	}
}
