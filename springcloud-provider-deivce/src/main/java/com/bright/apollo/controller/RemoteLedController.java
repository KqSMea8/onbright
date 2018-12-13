package com.bright.apollo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TRemoteLed;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.RemoteLedService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年12月11日
 * @Version:1.1.0
 */
@RequestMapping("remoteLed")
@RestController
public class RemoteLedController {
	private static final Logger logger = LoggerFactory.getLogger(RemoteLedController.class);
	@Autowired
	private RemoteLedService remoteLedService;

	/**
	 * @param serialId
	 * @param names
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/setRemoteLedName/{serialId}", method = RequestMethod.POST)
	ResponseObject setRemoteLedName(@PathVariable(value = "serialId", required = true) String serialId,
			@RequestParam(value = "names", required = true) String names) {
		ResponseObject res = new ResponseObject();
		Map<String, Object> map = null;
		try {
			// 解析names为Map
			logger.info("===names:" + names + " ===serialId:" + serialId);
			if (!jsonIsMap(names)) {
				names="";
			}
			List<TRemoteLed> list = remoteLedService.getListBySerialId(serialId);
			// delete
			// remoteLedService.addRemoteLed(remoteLed);
			if (list == null || list.size() == 0) {
				// add
				TRemoteLed remoteLed = new TRemoteLed(serialId, names);
				remoteLedService.addRemoteLed(remoteLed);
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
				return res;
			} else {
				TRemoteLed tRemoteLed = list.get(0);
				tRemoteLed.setValue(names);
				remoteLedService.updateRemote(tRemoteLed);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				return res;
			}

			// 判断remoteLedNames 是否存在
			// List<TRemoteLed> list =
			// remoteLedService.getListBySerialId(serialId);
			/*
			 * if (list == null || list.size() == 0) { // add TRemoteLed
			 * remoteLed = new TRemoteLed(serialId, names);
			 * remoteLedService.addRemoteLed(remoteLed); }
			 */
			/*
			 * if (list == null || list.size() == 0) { // add // 构建List list =
			 * buildRemoteLeds(serialId, map);
			 * remoteLedService.batchRemoteLeds(list);
			 * res.setStatus(ResponseEnum.AddSuccess.getStatus());
			 * res.setMessage(ResponseEnum.AddSuccess.getMsg()); } else { //
			 * update List List<TRemoteLed> updateList = buildRemotes(list, map,
			 * true); if (updateList != null && updateList.size() != 0) { for
			 * (TRemoteLed remoteLed : updateList) {
			 * remoteLedService.updateRemote(remoteLed); } if (updateList.size()
			 * == map.size()) {
			 * res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			 * res.setMessage(ResponseEnum.UpdateSuccess.getMsg()); return res;
			 * } List<TRemoteLed> addList = buildRemotes(list, map, false);
			 * if(addList!=null&&addList.size()>0){
			 * remoteLedService.batchRemoteLeds(addList); }
			 * res.setStatus(ResponseEnum.AddSuccess.getStatus());
			 * res.setMessage(ResponseEnum.AddSuccess.getMsg()); }else{ list =
			 * buildRemoteLeds(serialId, map);
			 * remoteLedService.batchRemoteLeds(list);
			 * res.setStatus(ResponseEnum.AddSuccess.getStatus());
			 * res.setMessage(ResponseEnum.AddSuccess.getMsg()); } }
			 */
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
	@RequestMapping(value = "/queryRemoteLedName/{serialId}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> queryRemoteLedName(
			@PathVariable(value = "serialId", required = true) String serialId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		//Map<String, Object> data = new HashMap<String, Object>();
		try {
			logger.info("===serialId:" + serialId);
			List<TRemoteLed> list = remoteLedService.getListBySerialId(serialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			if (list != null && list.size() > 0) {
				/*for (TRemoteLed remoteLed : list) {
					map.put(remoteLed.getChannel(), remoteLed.getName());
				}*/
				//if(jsonIsMap(str_json))
				map.put("names", json2map(list.get(0).getValue()));
			}
			//data.put("names", map);
			res.setData(map);
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param list
	 * @param map
	 * @return
	 * @Description:
	 */
	@Deprecated
	private List<TRemoteLed> buildRemotes(List<TRemoteLed> list, Map<String, Object> map, boolean flag) {
		List<TRemoteLed> remoteLeds = new ArrayList<TRemoteLed>();
		for (TRemoteLed remoteLed : list) {
			if (!StringUtils.isEmpty(map.get(remoteLed.getChannel()))) {
				if (flag) {
					remoteLed.setName(map.get(remoteLed.getChannel()).toString());
					remoteLeds.add(remoteLed);
				} else {
					// map.remove(key, value)
					map.remove(remoteLed.getChannel());
				}
			}
		}
		if (!flag && map.size() > 0) {
			return buildRemoteLeds(list.get(0).getSerialid(), map);
		}
		return remoteLeds;
	}

	/**
	 * @param serialId
	 * @param map
	 * @return
	 * @Description:
	 */
	private List<TRemoteLed> buildRemoteLeds(String serialId, Map<String, Object> map) {
		List<TRemoteLed> list = new ArrayList<TRemoteLed>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			TRemoteLed remoteLed = new TRemoteLed(entry.getValue().toString(), serialId, entry.getKey());
			list.add(remoteLed);
		}
		return list;
	}

	private static Map<String, Object> json2map(String str_json) {
		Map<String, Object> res = null;
		Gson gson = new Gson();
		try {
			
			res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			logger.error("===error msg:" + e.getMessage());
		}
		if(res==null){
			return gson.fromJson("{}", new TypeToken<Map<String, Object>>() {
			}.getType());
		}
		return res;
	}

	private static boolean jsonIsMap(String str_json) {
		// Map<String, Object> res = null;
		try {
			Gson gson = new Gson();
			gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
			}.getType());

		} catch (JsonSyntaxException e) {
			logger.error("===error msg:" + e.getMessage());
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(json2map("{}"));
	}
}
