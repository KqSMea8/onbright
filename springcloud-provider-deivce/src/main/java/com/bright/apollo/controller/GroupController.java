package com.bright.apollo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.bright.apollo.common.entity.TGroupDevice;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TServerGroup;
import com.bright.apollo.common.entity.TUSerGroup;
import com.bright.apollo.request.GroupDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.GroupDeviceService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.ServerGroupService;
import com.bright.apollo.service.UserGroupService;
import com.bright.apollo.tool.ByteHelper;
/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月30日
 * @Version:1.1.0
 */
@RequestMapping("group")
@RestController
public class GroupController {
	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private ServerGroupService serverGroupService;
	@Autowired
	private GroupDeviceService groupDeviceService;
	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryGroup/{userId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryGroup(@PathVariable(value = "userId") Integer userId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			List<GroupDTO> gList = new ArrayList<GroupDTO>();
			Map<String, Object> map = new HashMap<String, Object>();
			logger.info("===queryGroup args userId:" + userId);
			List<TUSerGroup> userGroups = userGroupService.queryUserGroup(userId);
			if (userGroups != null && userGroups.size() > 0) {
				logger.info("===userGroups" + userGroups);
				for (TUSerGroup tUserGroup : userGroups) {
					TServerGroup tServerGroup = serverGroupService.querySererGroupById(tUserGroup.getGroupId());
					if (tServerGroup != null) {
						logger.info("===tServerGroup:" + tServerGroup);
						GroupDTO groupDTO = new GroupDTO(tServerGroup);
						List<TOboxDeviceConfig> configs = new ArrayList<TOboxDeviceConfig>();
						if (tServerGroup.getGroupStyle().equals("01")) {
							List<TGroupDevice> tGroupDevices = groupDeviceService
									.queryDeviceGroupByGroupId(tServerGroup.getId());
							for (TGroupDevice tGroupDevice : tGroupDevices) {
								TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
										.queryDeviceConfigBySerialID(tGroupDevice.getDeviceSerialId());
								if (tOboxDeviceConfig != null) {
									configs.add(tOboxDeviceConfig);
								}
							}
						} else {
						}
						groupDTO.setGroupMember(configs);
						gList.add(groupDTO);
					}
				}
			}
			logger.info("===gList:" + gList);
			map.put("groups", gList);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(map);
		} catch (Exception e) {
			logger.error("===queryGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param groupName
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/addServerGroup/{groupName}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addServerGroup(@PathVariable(value = "groupName") String groupName,
			@RequestParam(value = "mList", required = false) List<String> mList) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			logger.info("===addServerGroup args groupName:"+groupName+"===mList:"+mList);
			Map<String, Object> map=new HashMap<String, Object>();
			TServerGroup tServerGroup = new TServerGroup();
			tServerGroup.setGroupName(groupName);
			tServerGroup.setGroupStyle("01");//default is server group
			String addr = "";
			for (int i = 1; i < 65535; i++) {
				String highBit = ByteHelper.int2HexString(i / 255);
				String lowBit = ByteHelper.int2HexString(i % 255);
				List<TServerGroup> groups = null;
				if (tServerGroup != null)
					logger.info("===tServerGroup:"+tServerGroup);
					groups=serverGroupService.queryServerGroupByAddr(highBit+ lowBit);
				if (groups == null||groups.size()<=0) {
					addr = highBit + lowBit;
					break;
				}
			}
			tServerGroup.setGroupAddr(addr);
			tServerGroup.setGroupState("aaaaaadddddd00");//default state
			//add group
		} catch (Exception e) {
			logger.error("===addServerGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}
}
