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
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.TDeviceChannel;
import com.bright.apollo.common.entity.TGroupDevice;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TServerGroup;
import com.bright.apollo.common.entity.TUSerGroup;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.request.GroupDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.DeviceChannelService;
import com.bright.apollo.service.GroupDeviceService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
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
	@Autowired
	private DeviceChannelService deviceChannelService;
	@Autowired
	private OboxService oboxService;
	@Autowired
	private CmdCache cmdCache;
	@Autowired
	private FeignAliClient feignAliClient;
	private final static String serverAddr = "020000ffff";

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
	@RequestMapping(value = "/addServerGroup/{groupName}/{userId}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addServerGroup(@PathVariable(value = "groupName") String groupName,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(value = "mList", required = false) List<String> mList) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			logger.info("===addServerGroup args groupName:" + groupName + "===mList:" + mList);
			Map<String, Object> map = new HashMap<String, Object>();
			TServerGroup tServerGroup = new TServerGroup();
			tServerGroup.setGroupName(groupName);
			tServerGroup.setGroupStyle("01");// default is server group
			String addr = "";
			for (int i = 1; i < 65535; i++) {
				String highBit = ByteHelper.int2HexString(i / 255);
				String lowBit = ByteHelper.int2HexString(i % 255);
				List<TServerGroup> groups = null;
				if (tServerGroup != null)
					logger.info("===tServerGroup:" + tServerGroup);
				groups = serverGroupService.queryServerGroupByAddr(highBit + lowBit);
				if (groups == null || groups.size() <= 0) {
					addr = highBit + lowBit;
					break;
				}
			}
			tServerGroup.setGroupAddr(addr);
			tServerGroup.setGroupState("aaaaaadddddd00");// default state
			serverGroupService.addServerGroup(tServerGroup);
			TUSerGroup tUserGroup = new TUSerGroup();
			tUserGroup.setGroupId(tServerGroup.getId());
			tUserGroup.setUserId(userId);
			userGroupService.addUserGroup(tUserGroup);
			List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
			List<TOboxDeviceConfig> configs = new ArrayList<TOboxDeviceConfig>();
			if (mList == null && mList.size() == 0) {
				for (int i = 0; i < mList.size(); i++) {
					String string = mList.get(i);
					TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getDeviceByUserAndSerialId(userId,
							string);
					if (tOboxDeviceConfig != null) {
						configs.add(tOboxDeviceConfig);
					}
				}
				addMember(configs, replyList, tServerGroup);
				String devicetype = "";
				String deviceChildType = "";
				boolean isSameType = true;
				for (int i = 0; i < replyList.size(); i++) {
					TOboxDeviceConfig tOboxDeviceConfig = replyList.get(i);
					if (i == 0) {
						devicetype = tOboxDeviceConfig.getDeviceType();
						deviceChildType = tOboxDeviceConfig.getDeviceChildType();
					} else {
						if (devicetype.equals(tOboxDeviceConfig.getDeviceType())
								&& deviceChildType.equals(tOboxDeviceConfig.getDeviceChildType())) {

						} else {
							isSameType = false;
						}
					}
				}
				tServerGroup.setGroupType(devicetype);
				if (isSameType) {
					tServerGroup.setGroupChildType(deviceChildType);
				} else {
					tServerGroup.setGroupChildType("01");
				}
				serverGroupService.updateServerGroup(tServerGroup);
			} else {
				tServerGroup.setGroupChildType("01");
				tServerGroup.setGroupType("01");
				serverGroupService.updateServerGroup(tServerGroup);
			}
			GroupDTO groupDTO = new GroupDTO(tServerGroup);
			groupDTO.setGroupMember(replyList);
			map.put("groups", groupDTO);
			map.put("operate_type", "01");
			res.setData(map);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===addServerGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param groupId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/deleteServerGroup/{groupId}", method = RequestMethod.DELETE)
	ResponseObject<Map<String, Object>> deleteServerGroup(@PathVariable(value = "groupId") Integer groupId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			logger.info("===deleteServerGroup args groupId:" + groupId);
			Map<String, Object> map = new HashMap<String, Object>();
			TServerGroup serverGroup = serverGroupService.querySererGroupById(groupId);
			if (serverGroup == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				List<TOboxDeviceConfig> tOboxDeviceConfigs = oboxDeviceConfigService.queryDeviceByGroupId(groupId);
				if (tOboxDeviceConfigs!=null&&!tOboxDeviceConfigs.isEmpty()) {
					List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
					deleteMember(tOboxDeviceConfigs, replyList,
							 serverGroup);
				}
			}
		} catch (Exception e) {
			logger.error("===deleteServerGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param configs
	 * @param replyList
	 * @param tServerGroup
	 * @Description:
	 */
	private void addMember(List<TOboxDeviceConfig> addList, List<TOboxDeviceConfig> successList,
			TServerGroup tServerGroup) {
		try {
			boolean isDone = false;
			while (!isDone) {
				for (int i = 0; i < addList.size(); i++) {
					TOboxDeviceConfig tOboxDeviceConfig = addList.get(i);
					if (tOboxDeviceConfig.getIsSend() == 0) {
						byte[] oboxSerildIdBytes = ByteHelper.hexStringToBytes(tOboxDeviceConfig.getOboxSerialId());
						byte[] setBytes = new byte[15];
						System.arraycopy(oboxSerildIdBytes, 0, setBytes, 0, oboxSerildIdBytes.length);
						setBytes[5] = 0x00;
						setBytes[6] = (byte) Integer.parseInt(tOboxDeviceConfig.getDeviceRfAddr(), 16);
						setBytes[7] = 0x02;
						// 当serverAddr 为空的时候
						if (!StringUtils.isEmpty(serverAddr)) {
							byte[] serverBytes = ByteHelper.hexStringToBytes(serverAddr);
							System.arraycopy(serverBytes, 0, setBytes, 8, serverBytes.length);
						}
						byte[] groupBytes = ByteHelper.hexStringToBytes(tServerGroup.getGroupAddr());
						System.arraycopy(groupBytes, 0, setBytes, 13, groupBytes.length);
						TObox bestOBOXChannel = null;
						boolean isFound = false;
						List<TDeviceChannel> tDeviceChannels = deviceChannelService
								.getDeivceChannelById(tOboxDeviceConfig.getId());
						for (TDeviceChannel tDeviceChannel : tDeviceChannels) {
							TObox obox = oboxService.queryOboxById(tDeviceChannel.getOboxId());
							if (obox != null && obox.getOboxStatus() == 1) {
								bestOBOXChannel = obox;
								cmdCache.saveGroup(
										obox.getOboxId().intValue() + ":" + tOboxDeviceConfig.getDeviceSerialId(),
										tServerGroup.getId().intValue() + "");
								logger.info("-------setBytes------:" + setBytes.toString());
								feignAliClient.sendCmd(obox, CMDEnum.set_group, setBytes);
								tOboxDeviceConfig.setIsSend(1);
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							if (bestOBOXChannel == null) {
								tOboxDeviceConfig.setIsSend(1);
							}
						}
					}
					if (i + 1 == addList.size()) {
						isDone = true;
						for (TOboxDeviceConfig deviceConfig : addList) {
							if (deviceConfig.getIsSend() == 0) {
								isDone = false;
								break;
							}
						}
						if (isDone) {
							break;
						}
					}
				}
			}
			Thread.sleep(350);
			for (TOboxDeviceConfig deviceConfig : addList) {
				TGroupDevice tGroupDevice = groupDeviceService.queryDeviceGroup(tServerGroup.getId(),
						deviceConfig.getDeviceSerialId());
				tServerGroup.setGroupState(deviceConfig.getDeviceState());
				if (tGroupDevice != null) {
					successList.add(deviceConfig);
				}
			}
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
		}
	}
}
