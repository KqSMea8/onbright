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
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.pool.GroupActionPool;
import com.bright.apollo.request.CmdInfo;
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
	@Autowired
	private GroupActionPool groupActionPool;
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
			if (mList != null && mList.size() > 0) {
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
	@RequestMapping(value = "/deleteServerGroup/{groupId}/{userId}", method = RequestMethod.DELETE)
	ResponseObject<Map<String, Object>> deleteServerGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			logger.info("===deleteServerGroup args groupId:" + groupId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("operate_type", "00");
			TServerGroup serverGroup = serverGroupService.querySererGroupById(groupId);
			if (serverGroup == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				List<TOboxDeviceConfig> tOboxDeviceConfigs = oboxDeviceConfigService.queryDeviceByGroupId(groupId);
				List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
				if (tOboxDeviceConfigs != null && !tOboxDeviceConfigs.isEmpty()) {
					deleteMember(tOboxDeviceConfigs, replyList, serverGroup);
					replyList.clear();
					boolean deleteSuccess = true;
					for (TOboxDeviceConfig deviceConfig : tOboxDeviceConfigs) {
						TGroupDevice tGroupDevice = groupDeviceService.queryDeviceGroup(serverGroup.getId(),
								deviceConfig.getDeviceSerialId());
						if (tGroupDevice != null) {
							deleteSuccess = false;
							replyList.add(deviceConfig);
						}
					}
					if (deleteSuccess) {
						userGroupService.deleteUserGroup(userId, serverGroup.getId());
						serverGroupService.deleteServerGroup(serverGroup.getId());
						GroupDTO groupDTO4 = new GroupDTO(serverGroup);
						groupDTO4.setGroupMember(replyList);
						map.put("groups", groupDTO4);
						res.setData(map);
						res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
						res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
					} else {
						GroupDTO groupDTO5 = new GroupDTO(serverGroup);
						groupDTO5.setGroupMember(replyList);
						map.put("groups", groupDTO5);
						res.setData(map);
						res.setStatus(ResponseEnum.SendOboxFail.getStatus());
						res.setMessage(ResponseEnum.SendOboxFail.getMsg());
					}
				} else {
					userGroupService.deleteUserGroup(userId, groupId);
					// UserBusiness.deleteUserGroup(Integer.parseInt(uid),
					// tServerGroup.getId());
					serverGroupService.deleteServerGroup(groupId);
					// DeviceBusiness.deleteServerGroup(tServerGroup);
					GroupDTO groupDTO6 = new GroupDTO(serverGroup);
					groupDTO6.setGroupMember(replyList);
					map.put("groups", groupDTO6);
					res.setData(map);
					res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
					res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
					// rightReply.add("groups", g2.toJsonTree(groupDTO6));
					// return rightReply;
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
	 * @param groupId
	 * @param userId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/coverChildGroup/{groupId}/{userId}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> coverChildGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(name = "mList", required = false) List<String> mList) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("operate_type", "02");
			TServerGroup tServerGroup = serverGroupService.querySererGroupById(groupId);
			if (tServerGroup == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				List<TOboxDeviceConfig> tOboxDeviceConfigs = oboxDeviceConfigService.queryDeviceByGroupId(groupId);
				List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
				if (tOboxDeviceConfigs != null && !tOboxDeviceConfigs.isEmpty()) {
					deleteMember(tOboxDeviceConfigs, replyList, tServerGroup);
					replyList.clear();
					for (TOboxDeviceConfig deviceConfig : tOboxDeviceConfigs) {
						TGroupDevice tGroupDevice = groupDeviceService.queryDeviceGroup(tServerGroup.getId(),
								deviceConfig.getDeviceSerialId());
						if (tGroupDevice != null) {
							replyList.add(deviceConfig);
						}
					}
				}
				if (mList != null && mList.size() > 0) {
					List<TOboxDeviceConfig> configs = new ArrayList<TOboxDeviceConfig>();
					String devicetype = "";
					String deviceChildType = "";
					boolean isSameType = true;
					for (int i = 0; i < mList.size(); i++) {
						String string = mList.get(i);
						TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
								.queryDeviceConfigBySerialID(string);
						if (tOboxDeviceConfig != null) {
							if (configs.isEmpty()) {
								devicetype = tOboxDeviceConfig.getDeviceType();
								deviceChildType = tOboxDeviceConfig.getDeviceChildType();
							} else {
								if (devicetype.equals(tOboxDeviceConfig.getDeviceType())
										&& deviceChildType.equals(tOboxDeviceConfig.getDeviceChildType())) {

								} else {
									isSameType = false;
								}
							}
							configs.add(tOboxDeviceConfig);
						}
					}
					tServerGroup.setGroupType(devicetype);
					if (isSameType) {
						tServerGroup.setGroupChildType(deviceChildType);
					} else {
						tServerGroup.setGroupChildType("01");
					}
					addMember(configs, replyList, tServerGroup);
				}
				serverGroupService.updateServerGroup(tServerGroup);
				GroupDTO groupDTO8 = new GroupDTO(tServerGroup);
				groupDTO8.setGroupMember(replyList);
				map.put("groups", groupDTO8);
				res.setData(map);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			}
		} catch (Exception e) {
			logger.error("===coverChildGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/removeChildGroup/{groupId}/{userId}", method = RequestMethod.DELETE)
	ResponseObject<Map<String, Object>> removeChildGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(name = "mList", required = false) List<String> mList) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("operate_type", "04");
			TServerGroup tServerGroup = serverGroupService.querySererGroupById(groupId);
			if (tServerGroup == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				List<TOboxDeviceConfig> configs = new ArrayList<TOboxDeviceConfig>();
				List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
				if (mList != null && mList.size() > 0) {
					for (String string : mList) {
						TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
								.queryDeviceConfigBySerialID(string);
						if (tOboxDeviceConfig != null) {
							configs.add(tOboxDeviceConfig);
						}
					}
				}
				if (!configs.isEmpty()) {
					deleteMember(configs, replyList, tServerGroup);
				}
				GroupDTO groupDTO9 = new GroupDTO(tServerGroup);
				groupDTO9.setGroupMember(replyList);
				map.put("groups", groupDTO9);
				res.setData(map);
				res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
				res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
			}
		} catch (Exception e) {
			logger.error("===removeChildGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param groupId
	 * @param userId
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/addChildGroup/{groupId}/{userId}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addChildGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(name = "mList", required = false) List<String> mList) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("operate_type", "03");
			TServerGroup tServerGroup = serverGroupService.querySererGroupById(groupId);
			if (tServerGroup == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
				List<TOboxDeviceConfig> configs2 = new ArrayList<TOboxDeviceConfig>();
				for (String string : mList) {
					TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryDeviceConfigBySerialID(string);
					if (tOboxDeviceConfig != null) {
						configs2.add(tOboxDeviceConfig);
					}
				}
				addMember(configs2, replyList, tServerGroup);
				List<TOboxDeviceConfig> tOboxDeviceConfigs = oboxDeviceConfigService.queryDeviceByGroupId(groupId);
				Boolean isSameType = true;
				String devicetype = "";
				String deviceChildType = "";
				if (!tOboxDeviceConfigs.isEmpty()) {
					for (int i = 0; i < tOboxDeviceConfigs.size(); i++) {
						TOboxDeviceConfig config = tOboxDeviceConfigs.get(i);
						if (i == 0) {
							devicetype = config.getDeviceType();
							deviceChildType = config.getDeviceChildType();
						} else {
							if (devicetype.equals(config.getDeviceType())
									&& deviceChildType.equals(config.getDeviceChildType())) {
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
				}
				serverGroupService.updateServerGroup(tServerGroup);
				GroupDTO groupDTO10 = new GroupDTO(tServerGroup);
				groupDTO10.setGroupMember(replyList);
				map.put("groups", groupDTO10);
				res.setData(map);
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
			}
		} catch (Exception e) {
			logger.error("===addChildGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param groupId
	 * @param groupName
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/reNameGroup/{groupId}/{groupName}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> reNameGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "groupName") String groupName){
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("operate_type", "05");
			TServerGroup tServerGroup = serverGroupService.querySererGroupById(groupId);
			if (tServerGroup == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				tServerGroup.setGroupName(groupName);
				serverGroupService.updateServerGroup(tServerGroup);
				List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
				GroupDTO groupDTO11 = new GroupDTO(tServerGroup);
				groupDTO11.setGroupMember(replyList);
				map.put("groups", groupDTO11);
				res.setData(map);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			}
		} catch (Exception e) {
			logger.error("===reNameGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	
	/**  
	 * @param groupId
	 * @param groupState
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/actionGroup/{groupId}/{groupState}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> actionGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "groupState") String groupState){
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("operate_type", "06");
			TServerGroup tServerGroup = serverGroupService.querySererGroupById(groupId);
			if (tServerGroup == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				List<TOboxDeviceConfig> replyList = new ArrayList<TOboxDeviceConfig>();
				groupActionPool.setGroup(groupState, groupId);
				if (groupState.substring(0, 2).equals("ff")
						|| groupState.substring(0, 2).equals("FF")) {
					List<TGroupDevice> tGroupDevices = groupDeviceService
							.queryDeviceGroupByGroupId(tServerGroup.getId());
					boolean isFound = false;
					for (TGroupDevice tGroupDevice : tGroupDevices) {
						TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryDeviceConfigBySerialID(tGroupDevice.getDeviceSerialId());
						//.queryDeviceConfigByID(tGroupDevice
						//				.getDeviceId());
						if (tOboxDeviceConfig != null) {
							if (!tOboxDeviceConfig.getDeviceState()
									.substring(0, 2).equals("00")
									&& !tOboxDeviceConfig.getDeviceState()
											.substring(0, 2).equals("ff")) {
								isFound = true;
								tServerGroup.setGroupState(tOboxDeviceConfig
										.getDeviceState());
								break;
							}
						}
					}
					if (!isFound) {
						if (tServerGroup.getGroupChildType().equals(DeviceTypeEnum.led_rgb.getValue())) {
							tServerGroup.setGroupState("32"+groupState.substring(2, groupState.length()));
						}else {
							tServerGroup.setGroupState("c8"+groupState.substring(2, groupState.length()));
						}
					}
				}
				serverGroupService.updateServerGroup(tServerGroup);
				GroupDTO groupDTO12 = new GroupDTO(tServerGroup);
				groupDTO12.setGroupMember(replyList);
				map.put("groups", groupDTO12);
				res.setData(map);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				//rightReply.add("groups", g2.toJsonTree(groupDTO12));
				//return rightReply;
			}
		} catch (Exception e) {
			logger.error("===actionGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param userId
	 * @param groupId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryGroupByUserAndGroup/{userId}/{groupId}", method = RequestMethod.GET)
	ResponseObject<TServerGroup> queryGroupByUserAndGroup(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "groupId") Integer groupId){

		ResponseObject<TServerGroup> res = new ResponseObject<TServerGroup>();
		try {
			res.setData(serverGroupService.queryGroupByUserAndGroup(userId,groupId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===actionGroup error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param tOboxDeviceConfigs
	 * @param replyList
	 * @param serverGroup
	 * @Description:
	 */
	private void deleteMember(List<TOboxDeviceConfig> deleteList, List<TOboxDeviceConfig> successList,
			TServerGroup tServerGroup) {
		try {
			//boolean isDone = false;
			//while (!isDone) {
				for (int i = 0; i < deleteList.size(); i++) {
					TOboxDeviceConfig tOboxDeviceConfig = deleteList.get(i);
					if (tOboxDeviceConfig.getIsSend() == 0) {
						byte[] oboxSerildIdBytes = ByteHelper.hexStringToBytes(tOboxDeviceConfig.getOboxSerialId());
						byte[] setBytes = new byte[15];
						System.arraycopy(oboxSerildIdBytes, 0, setBytes, 0, oboxSerildIdBytes.length);
						setBytes[5] = 0x00;
						setBytes[6] = (byte) Integer.parseInt(tOboxDeviceConfig.getDeviceRfAddr(), 16);
						setBytes[7] = 0x01;

						byte[] serverBytes = ByteHelper.hexStringToBytes(serverAddr);
						System.arraycopy(serverBytes, 0, setBytes, 8, serverBytes.length);

						byte[] groupBytes = ByteHelper.hexStringToBytes(tServerGroup.getGroupAddr());
						System.arraycopy(groupBytes, 0, setBytes, 13, groupBytes.length);
						//TObox bestOBOXChannel = null;
						//boolean isFound = false;
					//	List<TDeviceChannel> tDeviceChannels = deviceChannelService
						//		.getDeivceChannelById(tOboxDeviceConfig.getId());
					//	for (TDeviceChannel tDeviceChannel : tDeviceChannels) {
						TObox obox = oboxService.queryOboxsByOboxSerialId(tOboxDeviceConfig.getOboxSerialId());
						//TObox obox = oboxService.queryOboxById(tDeviceChannel.getOboxId());
							if (obox != null && obox.getOboxStatus() == 1) {
								Map<String, Object>map=new HashMap<String, Object>();
								//CmdInfo cmdInfo=new CmdInfo(obox, CMDEnum.set_group, setBytes);
								//map.put("cmdinfo", cmdInfo);
								map.put("serialId", obox.getOboxSerialId());
								map.put("cmd", CMDEnum.set_group);
								map.put("bytes", ByteHelper.bytesToHexString(setBytes));
								feignAliClient.sendCmd(map);
 								tOboxDeviceConfig.setIsSend(1);
								tOboxDeviceConfig.setSendTime(tOboxDeviceConfig.getSendTime() + 1);
								//isFound = true;
								//break;
							}
						//}

						/*if (!isFound) {
							if (bestOBOXChannel == null) {
								tOboxDeviceConfig.setIsSend(1);
								tOboxDeviceConfig.setSendTime(tOboxDeviceConfig.getSendTime() + 1);
							}
						}*/
					}
					/*if (i + 1 == deleteList.size()) {
						isDone = true;
						for (TOboxDeviceConfig deviceConfig : deleteList) {
							if (deviceConfig.getIsSend() == 0) {
								isDone = false;
								break;
							}
						}
						if (isDone) {
							break;
						}
					}*/
				}
			//}
			Thread.sleep(350);
			for (TOboxDeviceConfig deviceConfig : deleteList) {
				TGroupDevice tGroupDevice = groupDeviceService.queryDeviceGroup(tServerGroup.getId(),
						deviceConfig.getDeviceSerialId());
				if (tGroupDevice == null) {
					successList.add(deviceConfig);
				}
			}
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			// e.printStackTrace();
		}
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
		//	boolean isDone = false;
			//while (!isDone) {
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
						//boolean isFound = false;
						TObox bestOBOXChannel = oboxService.queryOboxsByOboxSerialId(tOboxDeviceConfig.getOboxSerialId());
						if(bestOBOXChannel.getOboxStatus()==(byte)1){
							cmdCache.saveGroup(
									bestOBOXChannel.getOboxId().intValue() + ":" + tOboxDeviceConfig.getDeviceSerialId(),
									tServerGroup.getId().intValue() + "");
							//Map<String, CmdInfo>map=new HashMap<String, CmdInfo>();
							//CmdInfo cmdInfo=new CmdInfo(bestOBOXChannel, CMDEnum.set_group, setBytes);
							//map.put("cmdinfo", cmdInfo);
							Map<String, Object>map=new HashMap<String, Object>();
							//CmdInfo cmdInfo=new CmdInfo(obox, CMDEnum.set_group, setBytes);
							//map.put("cmdinfo", cmdInfo);
							map.put("serialId", bestOBOXChannel.getOboxSerialId());
							map.put("cmd", CMDEnum.set_group);
							map.put("bytes", ByteHelper.bytesToHexString(setBytes));
							feignAliClient.sendCmd(map);
							tOboxDeviceConfig.setIsSend(1);
							//isFound = true;
							//break;
						}
						/*List<TDeviceChannel> tDeviceChannels = deviceChannelService
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
						}*/
						/*if (!isFound) {
							if (bestOBOXChannel == null) {
								tOboxDeviceConfig.setIsSend(1);
							}
						}*/
					}
				/*	if (i + 1 == addList.size()) {
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
					}*/
				}
			//}
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
	public static void main(String[] args) {
		List<String> mList=new ArrayList<String>();
		for (int i = 0; i < 2; i++) {
			mList.add(i+"1");
		}
		System.out.println(mList);
	}
}
