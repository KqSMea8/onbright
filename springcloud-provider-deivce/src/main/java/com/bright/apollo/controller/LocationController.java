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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TDeviceLocation;
import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.common.entity.TLocationScene;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TServerGroup;
import com.bright.apollo.common.entity.TUserLocation;
import com.bright.apollo.enums.ConditionTypeEnum;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.request.SceneActionDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.request.SceneDTO;
import com.bright.apollo.response.DeviceDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.DeviceLocationService;
import com.bright.apollo.service.LocationSceneService;
import com.bright.apollo.service.LocationService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.SceneActionService;
import com.bright.apollo.service.SceneConditionService;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.service.ServerGroupService;
import com.bright.apollo.service.UserLocationService;
import com.bright.apollo.tool.NumberHelper;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年11月23日
 * @Version:1.1.0
 */
@RequestMapping("location")
@RestController
public class LocationController {
	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;
	@Autowired
	private OboxService oboxService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private DeviceLocationService deviceLocationService;
	@Autowired
	private UserLocationService userLocationService;
	@Autowired
	private SceneService sceneService;
	@Autowired
	private SceneConditionService sceneConditionService;
	@Autowired
	private SceneActionService sceneActionService;
	@Autowired
	private ServerGroupService serverGroupService;
	@Autowired
	private LocationSceneService locationSceneService;

	/**
	 * @param userId
	 * @param building
	 * @param room
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/createLocation/{userId}/{building}/{room}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> createLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "building") String building, @PathVariable(value = "room") String room,
			//@RequestBody(required = false) List<String> mList
			@RequestParam(required=false,name="mList")List<String> mList) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		String location = null;
		try {
			if (mList != null && mList.size() > 0) {
				for (String string : mList) {
					TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getDeviceByUserAndSerialId(userId,
							string);
					// TOboxDeviceConfig tOboxDeviceConfig =
					// queryDeviceByWeight(user, string);
					if (tOboxDeviceConfig == null) {
						TObox tObox = oboxService.getOboxByUserAndoboxSerialId(userId, string);
						// TObox tObox = queryOboxByWeight(user, string);
						if (tObox == null) {
							res.setStatus(ResponseEnum.UnKonwUser.getStatus());
							res.setMessage(ResponseEnum.UnKonwUser.getMsg());
							return res;
						} else {// 如果设备是Obox的情况
							TLocation location1 = locationService.queryLocationByUserId(building, room, userId);
							/*
							 * if
							 * (Integer.parseInt(UserWeightEnum.ROOT.getValue(),
							 * 16) == user.getWeight()) { location1 =
							 * DeviceBusiness.queryLocationByInfo(building,
							 * room,user.getLicense()); }else if
							 * (Integer.parseInt(UserWeightEnum.ADMIN.getValue()
							 * , 16) == user.getWeight()) { location1 =
							 * DeviceBusiness.queryLocationByUserId(building,
							 * room, user.getUserId()); }
							 */
							if (location1 != null) {// location已经存在，把obox存进去，把x和Y设为0
								TDeviceLocation tDeviceLocation = new TDeviceLocation();
								tDeviceLocation.setLocation(location1.getId());
								tDeviceLocation.setxAxis(0);
								tDeviceLocation.setyAxis(0);
								tDeviceLocation.setSerialId(tObox.getOboxSerialId());
								tDeviceLocation.setDeviceType("0a");
								deviceLocationService.addDeviceLocation(tDeviceLocation);
								// DeviceBusiness.addDeviceLocation(tDeviceLocation);
							} else {// location不存在，创建location，添加obox与location的映射
								TLocation tLocation = new TLocation();
								tLocation.setBuilding(building);
								tLocation.setRoom(room);
								int tLocationId = locationService.addLocation(tLocation);
								location = String.valueOf(
										tLocation.getId() != null ? tLocation.getId().intValue() : tLocationId);
								TDeviceLocation tDeviceLocation = new TDeviceLocation();
								tDeviceLocation.setLocation(tLocationId);
								tDeviceLocation.setxAxis(0);
								tDeviceLocation.setyAxis(0);
								tDeviceLocation.setSerialId(tObox.getOboxSerialId());
								tDeviceLocation.setDeviceType("0a");
								deviceLocationService.addDeviceLocation(tDeviceLocation);
							}
						}
					} else {// 设备为device的情况
						/*
						 * TLocation location1 = null; if
						 * (Integer.parseInt(UserWeightEnum.ROOT.getValue(), 16)
						 * == user.getWeight()) { location1 =
						 * DeviceBusiness.queryLocationByInfo(building, room,
						 * user.getLicense()); } else if
						 * (Integer.parseInt(UserWeightEnum.ADMIN.getValue(),
						 * 16) == user.getWeight()) { location1 =
						 * DeviceBusiness.queryLocationByUserId(building, room,
						 * user.getUserId()); }
						 */
						TLocation location1 = locationService.queryLocationByUserId(building, room, userId);
						if (location1 != null) {// location已经存在，把device存进去，把X和Y设为0
							TDeviceLocation tDeviceLocation = new TDeviceLocation();

							tDeviceLocation.setLocation(location1.getId());
							tDeviceLocation.setxAxis(0);
							tDeviceLocation.setyAxis(0);
							tDeviceLocation.setSerialId(tOboxDeviceConfig.getDeviceSerialId());
							tDeviceLocation.setDeviceType(tOboxDeviceConfig.getDeviceType());
							deviceLocationService.addDeviceLocation(tDeviceLocation);
						} else {// location不存在，创建location，添加device与location的映射
							TLocation tLocation = new TLocation();
							tLocation.setBuilding(building);
							tLocation.setRoom(room);
							// tLocation.setLicense(tOboxDeviceConfig.get);
							// int tLocationId =
							// DeviceBusiness.addLocation(tLocation);
							// location = String.valueOf(tLocationId);
							int tLocationId = locationService.addLocation(tLocation);
							location = String
									.valueOf(tLocation.getId() != null ? tLocation.getId().intValue() : tLocationId);
							/*
							 * if
							 * (Integer.parseInt(UserWeightEnum.ADMIN.getValue()
							 * , 16) == user.getWeight()) { TUserLocation
							 * tUserLocation = new TUserLocation();
							 * tUserLocation.setLocationId(tLocationId);
							 * tUserLocation.setUserId(user.getUserId());
							 * UserBusiness.addUserLocation(tUserLocation); }
							 */
							TUserLocation tUserLocation = new TUserLocation();
							tUserLocation.setLocationId(
									tLocation.getId() != null ? tLocation.getId().intValue() : tLocationId);
							tUserLocation.setUserId(userId);
							userLocationService.addUserLocation(tUserLocation);
							// UserBusiness.addUserLocation(tUserLocation);
							TDeviceLocation tDeviceLocation = new TDeviceLocation();
							tDeviceLocation.setLocation(tLocationId);
							tDeviceLocation.setxAxis(0);
							tDeviceLocation.setyAxis(0);
							tDeviceLocation.setSerialId(tOboxDeviceConfig.getDeviceSerialId());
							tDeviceLocation.setDeviceType(tOboxDeviceConfig.getDeviceType());
							deviceLocationService.addDeviceLocation(tDeviceLocation);
						}
					}
				}
			} else {
				TLocation location1 = locationService.queryLocationByUserId(building, room, userId);
				/*
				 * if (Integer.parseInt(UserWeightEnum.ROOT.getValue(), 16) ==
				 * user.getWeight()) { location1 =
				 * DeviceBusiness.queryLocationByInfo(building,
				 * room,user.getLicense()); }else if
				 * (Integer.parseInt(UserWeightEnum.ADMIN.getValue(), 16) ==
				 * user.getWeight()) { location1 =
				 * DeviceBusiness.queryLocationByUserId(building, room,
				 * user.getUserId()); }
				 */
				if (location1 != null) {// location已经存在，把device存进去，把X和Y设为0
					location = String.valueOf(location1.getId());
				} else {// location不存在，创建location，添加device与location的映射
					TLocation tLocation = new TLocation();
					tLocation.setBuilding(building);
					tLocation.setRoom(room);
					// tLocation.setLicense(user.getLicense());
					int tLocationId = locationService.addLocation(tLocation);
					// DeviceBusiness.addLocation(tLocation);
					location = String.valueOf(tLocation.getId() != null ? tLocation.getId().intValue() : tLocationId);
					/*
					 * if (Integer.parseInt(UserWeightEnum.ADMIN.getValue(), 16)
					 * == user.getWeight()) { TUserLocation tUserLocation = new
					 * TUserLocation();
					 * tUserLocation.setLocationId(tLocationId);
					 * tUserLocation.setUserId(user.getUserId());
					 * UserBusiness.addUserLocation(tUserLocation); }
					 */
					TUserLocation tUserLocation = new TUserLocation();
					tUserLocation.setLocationId(Integer.parseInt(location));
					tUserLocation.setUserId(userId);
					userLocationService.addUserLocation(tUserLocation);
					// UserBusiness.addUserLocation(tUserLocation);
				}
			}
			map.put("location", location);
			res.setData(map);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===createLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/deleteLocation/{location}/{userId}", method = RequestMethod.DELETE)
	ResponseObject<Map<String, Object>> deleteLocation(@PathVariable(value = "location") Integer location,
			@PathVariable(value = "userId") Integer userId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// String location = null;
		try {
			// 删除，先删除device与location的映射,然后把location删了
			List<TDeviceLocation> devices = deviceLocationService.queryDevicesByLocation(location);
			if (devices != null) {
				for (TDeviceLocation device : devices) {
					deviceLocationService.deleteDeviceLocation(device.getId());
				}
			}
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			// TLocation tLocation = queryLocationByWeight(user,
			// Integer.parseInt(location));
			if (tLocation != null) {
				// 搭建完图片服务器再做处理
				/*
				 * if (tLocation.getDownloadUrl() != null) { File dFile = new
				 * File("/data" + tLocation.getDownloadUrl().replace("%20",
				 * " ")); if (dFile != null) { dFile.delete(); } } if
				 * (tLocation.getThumUrl() != null) { File tFile = new
				 * File("/data" + tLocation.getThumUrl().replace("%20", " "));
				 * if (tFile != null) { tFile.delete(); } }
				 */
				userLocationService.deleteUserLocation(tLocation.getId());
				// UserBusiness.deleteUserLocation(tLocation.getId());
				locationService.deleteLocation(tLocation.getId());
				// DeviceBusiness.deleteLocation(tLocation.getId());

				// wait a minute
				// SceneBusiness.deleteSceneLocationByLocationId(tLocation.getId());
			} else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			map.put("location", location);
			res.setData(map);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===deleteLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/updateLocation/{location}/{userId}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> updateLocation(@PathVariable(value = "location") Integer location,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(value = "building", required = false) String building,
			@RequestParam(name = "room", required = false) String room,
			@RequestBody(required = false) List<String> mList) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// String location = null;
		try {
			// 覆盖，先删除location里面的设备(删映射)，再添加进去（添加映射）。
			// Assert.notNull(serialId, "serialId can't be null!");
			List<TDeviceLocation> devices = deviceLocationService.queryDevicesByLocation(location);
			// List<TDeviceLocation> devices =
			// DeviceBusiness.queryDevicesByLocation(Integer.parseInt(location));
			if (devices != null) {
				for (TDeviceLocation device : devices) {
					deviceLocationService.deleteDeviceLocation(device.getId());
					// DeviceBusiness.deleteDeviceLocation(device.getId());
				}
			}
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			// TLocation tLocation = queryLocationByWeight(user,
			// Integer.parseInt(location));
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			} else {
				if (!StringUtils.isEmpty(building) || !StringUtils.isEmpty(room)) {
					tLocation.setBuilding(building);
					tLocation.setRoom(room);
					locationService.updateLocation(tLocation);
					// DeviceBusiness.updateLocation(tLocation);
				}
			}
			if (mList != null && mList.size() > 0) {
				// @SuppressWarnings("unchecked")
				// List<String> mList = (List<String>)
				// ObjectUtils.fromJsonToObject(serialId, List.class);
				for (String string : mList) {
					TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getDeviceByUserAndSerialId(userId,
							string);
					// TOboxDeviceConfig tOboxDeviceConfig =
					// queryDeviceByWeight(user, string);
					if (tOboxDeviceConfig == null) {
						TObox tObox = oboxService.getOboxByUserAndoboxSerialId(userId, string);
						// TObox tObox = queryOboxByWeight(user, string);
						if (tObox == null) {
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						} else {// 如果设备是Obox的情况
							TDeviceLocation tDeviceLocation = new TDeviceLocation();
							tDeviceLocation.setLocation(location);
							tDeviceLocation.setxAxis(0);
							tDeviceLocation.setyAxis(0);
							tDeviceLocation.setSerialId(tObox.getOboxSerialId());
							tDeviceLocation.setDeviceType("0a");
							deviceLocationService.addDeviceLocation(tDeviceLocation);
							// DeviceBusiness.addDeviceLocation(tDeviceLocation);
						}
					} else {// 设备为device的情况
						TDeviceLocation tDeviceLocation = new TDeviceLocation();

						tDeviceLocation.setLocation(location);
						tDeviceLocation.setxAxis(0);
						tDeviceLocation.setyAxis(0);
						tDeviceLocation.setSerialId(tOboxDeviceConfig.getDeviceSerialId());
						tDeviceLocation.setDeviceType(tOboxDeviceConfig.getDeviceType());
						deviceLocationService.addDeviceLocation(tDeviceLocation);
						// DeviceBusiness.addDeviceLocation(tDeviceLocation);
					}
				}
			} else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			map.put("location", location);
			res.setData(map);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===updateLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param serialId
	 * @param location
	 * @param xAxis
	 * @param yAxis
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/addDeviceLocation/{userId}/{serialId}/{location}/{xAxis}/{yAxis}/{deviceType}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addDeviceLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "location") Integer location,
			@PathVariable(value = "xAxis") Integer xAxis, @PathVariable(value = "yAxis") Integer yAxis,
			@PathVariable(value = "deviceType") String deviceType) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		// Map<String, Object> map = new HashMap<String, Object>();
		// String location = null;
		try {
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getDeviceByUserAndSerialId(userId, serialId);
			TObox tObox = oboxService.getOboxByUserAndoboxSerialId(userId, serialId);
			// if (tObox == null && tOboxDeviceConfig == null) {}
			if (tObox != null || tOboxDeviceConfig != null) {
				TDeviceLocation location2 = deviceLocationService.queryDevicesByLocationAndSerialIdAndType(location,
						serialId, deviceType);

				// TDeviceLocation location2 =
				// DeviceBusiness.queryDeviceLocation(tLocation.getId(),
				// tObox.getOboxId(), "0a");
				if (location2 != null) {
					location2.setxAxis(xAxis);
					location2.setyAxis(yAxis);
					// DeviceBusiness.updateDeviceLocation(location2);
					deviceLocationService.updateDeviceLocation(location2);
					res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
					res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				} else {
					TDeviceLocation tDeviceLocation = new TDeviceLocation();
					tDeviceLocation.setLocation(tLocation.getId());
					tDeviceLocation.setxAxis(xAxis);
					tDeviceLocation.setyAxis(yAxis);
					tDeviceLocation.setSerialId(serialId);
					tDeviceLocation.setDeviceType(deviceType);
					deviceLocationService.addDeviceLocation(tDeviceLocation);
					res.setStatus(ResponseEnum.AddSuccess.getStatus());
					res.setMessage(ResponseEnum.AddSuccess.getMsg());
				}
			} /*
				 * else if(tOboxDeviceConfig!=null){ TDeviceLocation location2 =
				 * deviceLocationService.
				 * queryDevicesByLocationAndSerialIdAndType(location, serialId,
				 * "00"); //TDeviceLocation location2 =
				 * DeviceBusiness.queryDeviceLocation(tLocation.getId(),
				 * tOboxDeviceConfig.getId(), "00"); if (location2 != null) {
				 * location2.setxAxis(xAxis); location2.setyAxis(yAxis);
				 * //DeviceBusiness.updateDeviceLocation(location2);
				 * deviceLocationService.addDeviceLocation(location2);
				 * //deviceLocationService.deleteDeviceLocation(location2.getId(
				 * )); }else { TDeviceLocation tDeviceLocation = new
				 * TDeviceLocation();
				 * tDeviceLocation.setLocation(tLocation.getId());
				 * tDeviceLocation.setxAxis(xAxis);
				 * tDeviceLocation.setyAxis(yAxis);
				 * tDeviceLocation.setSerialId(serialId);
				 * tDeviceLocation.setDeviceType("0a");
				 * deviceLocationService.addDeviceLocation(location2);
				 * res.setStatus(ResponseEnum.RequestParamError.getStatus());
				 * res.setMessage(ResponseEnum.RequestParamError.getMsg());
				 * return res; } }
				 */else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			
		} catch (Exception e) {
			logger.error("===addDeviceLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @param location
	 * @param xAxis
	 * @param yAxis
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteDeviceLocation/{userId}/{serialId}/{location}/{deviceType}", method = RequestMethod.DELETE)
	ResponseObject deleteDeviceLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "location") Integer location,
			@PathVariable(value = "deviceType") String deviceType
			) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		// String location = null;
		try {
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getDeviceByUserAndSerialId(userId, serialId);
			TObox tObox = oboxService.getOboxByUserAndoboxSerialId(userId, serialId);
			// if (tObox == null && tOboxDeviceConfig == null) {}
			if (tObox != null || tOboxDeviceConfig != null) {
				TDeviceLocation location2 = deviceLocationService.queryDevicesByLocationAndSerialIdAndType(location,
						serialId,deviceType);
				 
				if (location2 != null) {
					deviceLocationService.deleteDeviceLocation(location2.getId());
					// DeviceBusiness.deleteDeviceLocation(location2.getId());
				} else {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
			} else {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===deleteDeviceLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param userId
	 * @param locationId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryLocation/{userId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryLocation(@PathVariable(value = "userId") Integer userId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		// List<TLocation> oList = new ArrayList<TLocation>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<TLocation> oList = locationService.queryLocationByUser(userId);
			// TLocation tLocation =
			// locationService.queryLocationByUserIdAndId(userId, locationId);
			// if (tLocation != null)
			// oList.add(tLocation);
			/*
			 * List<TUserLocation> tUserLocations =
			 * userLocationService.queryUserLocationByUser(userId); for
			 * (TUserLocation tUserLocation : tUserLocations) { TLocation
			 * tLocation =
			 * locationService.queryLocationById(tUserLocation.getLocationId());
			 * if (tLocation != null) { oList.add(tLocation); } }
			 */
			map.put("locations", oList);
			res.setData(map);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===queryLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param userId
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryLocationByUserAndLocation/{userId}/{location}", method = RequestMethod.GET)
	ResponseObject<TLocation> queryLocationByUserAndLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location) {

		ResponseObject<TLocation> res = new ResponseObject<TLocation>();
		try {
			// List<TLocation> oList =
			// locationService.queryLocationByUser(userId);
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			// if (tLocation != null)
			// oList.add(tLocation);
			/*
			 * List<TUserLocation> tUserLocations =
			 * userLocationService.queryUserLocationByUser(userId); for
			 * (TUserLocation tUserLocation : tUserLocations) { TLocation
			 * tLocation =
			 * locationService.queryLocationById(tUserLocation.getLocationId());
			 * if (tLocation != null) { oList.add(tLocation); } }
			 */
			// map.put("locations", oList);
			res.setData(tLocation);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===queryLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param userId
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryDeviceLocation/{userId}/{locationId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryDeviceLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "locationId") Integer locationId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DeviceDTO> ouDeviceDTOs = new ArrayList<DeviceDTO>();
			List<TDeviceLocation> tDeviceLocations = deviceLocationService.queryDevicesByLocation(locationId);
			// List<TDeviceLocation> tDeviceLocations =
			// DeviceBusiness.queryDevicesByLocation(Integer.parseInt(location));
			for (TDeviceLocation tDeviceLocation : tDeviceLocations) {
				if (!tDeviceLocation.getDeviceType().equals("0a")) {
					TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
							.queryDeviceConfigBySerialID(tDeviceLocation.getSerialId());
					// TOboxDeviceConfig tOboxDeviceConfig =
					// DeviceBusiness.queryDeviceConfigByID(tDeviceLocation.getDeviceId());
					if (tOboxDeviceConfig != null) {
						DeviceDTO dto = new DeviceDTO(tOboxDeviceConfig);
						dto.setxAxis(tDeviceLocation.getxAxis());
						dto.setyAxis(tDeviceLocation.getyAxis());
						ouDeviceDTOs.add(dto);
					}
				} else {
					TObox tObox = oboxService.queryOboxsByOboxSerialId(tDeviceLocation.getSerialId());
					// TObox tObox =
					// OboxBusiness.queryOboxById(tDeviceLocation.getDeviceId());
					if (tObox != null) {
						DeviceDTO dto = new DeviceDTO();
						dto.setDeviceSerialId(tObox.getOboxSerialId());
						dto.setDeviceType("0a");
						dto.setxAxis(tDeviceLocation.getxAxis());
						dto.setyAxis(tDeviceLocation.getyAxis());
						ouDeviceDTOs.add(dto);
					}
				}
			}
			// Gson g2 = new
			// GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			// JsonObject jsonObject = respRight();
			map.put("devices", ouDeviceDTOs);
			res.setData(map);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			// jsonObject.add("devices", g2.toJsonTree(ouDeviceDTOs));
			// return jsonObject;
		} catch (Exception e) {
			logger.error("===queryDeviceLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param userId
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/querySceneLocation/{userId}/{location}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> querySceneLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			TLocation tLocation = locationService.queryLocationById(location);
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			List<TScene> tScenes = sceneService.querySceneByLocation(location);
			// List<TScene> tScenes =
			// SceneBusiness.querySceneByLocation(tLocation.getId());
			List<SceneDTO> tDtos = new ArrayList<SceneDTO>();

			if (tScenes != null) {
				for (TScene tScene : tScenes) {
					SceneDTO sceneDTO = new SceneDTO(tScene);

					List<List<SceneConditionDTO>> conditions = new ArrayList<List<SceneConditionDTO>>();
					List<TSceneCondition> tSceneConditions = sceneConditionService
							.getSceneConditionBySceneNum(tScene.getSceneNumber());
					// List<TSceneCondition> tSceneConditions =
					// SceneBusiness.querySceneConditionsBySceneNumber(tScene.getSceneNumber());
					for (int i = 0; i < 3; i++) {
						List<SceneConditionDTO> tConditionDTOs = new ArrayList<SceneConditionDTO>();
						for (TSceneCondition tSceneCondition : tSceneConditions) {
							if (tSceneCondition.getConditionGroup() == i) {
								SceneConditionDTO sceneConditionDTO = new SceneConditionDTO();
								sceneConditionDTO.setCondition(tSceneCondition.getCond());
								sceneConditionDTO.setConditionType(ConditionTypeEnum.time.getValue());
								if (tSceneCondition.getSerialid() != null) {
									// node condition
									TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
											.queryDeviceConfigBySerialID(tSceneCondition.getSerialid());
									// TOboxDeviceConfig tOboxDeviceConfig =
									// DeviceBusiness.queryDeviceConfigBySerialID(tSceneCondition.getSerialId());
									if (tOboxDeviceConfig != null) {
										sceneConditionDTO.setConditionID(tOboxDeviceConfig.getDeviceId());
										sceneConditionDTO.setDeviceRfAddr(tOboxDeviceConfig.getDeviceRfAddr());
										sceneConditionDTO.setDeviceSerialId(tOboxDeviceConfig.getDeviceSerialId());
										sceneConditionDTO.setOboxSerialId(tOboxDeviceConfig.getOboxSerialId());
										sceneConditionDTO.setDeviceChildType(tOboxDeviceConfig.getDeviceChildType());
										sceneConditionDTO.setDeviceType(tOboxDeviceConfig.getDeviceType());
										sceneConditionDTO.setConditionType(ConditionTypeEnum.device.getValue());
									} else {
										/*
										 * TRemoter tRemoter =
										 * DeviceBusiness.queryRemoter(
										 * tSceneCondition.getSerialId()); if
										 * (tRemoter != null) {
										 * sceneConditionDTO.setDeviceSerialId(
										 * tRemoter.getRemoter());
										 * sceneConditionDTO.setConditionType(
										 * ConditionTypeEnum.remoter.getValue())
										 * ; }
										 */}
								} else {
									// time condition

								}
								tConditionDTOs.add(sceneConditionDTO);
							}
						}
						if (tConditionDTOs.size() != 0) {
							conditions.add(tConditionDTOs);
						}
					}
					sceneDTO.setConditions(conditions);
					List<SceneActionDTO> tActionDTOs = new ArrayList<SceneActionDTO>();
					List<TSceneAction> tSceneActions = sceneActionService
							.getSceneActionBySceneNumber(tScene.getSceneNumber());
					// List<TSceneAction> tSceneActions =
					// SceneBusiness.querySceneActionsBySceneNumber(tScene.getSceneNumber());
					if (tSceneActions != null) {
						for (TSceneAction tSceneAction : tSceneActions) {
							if (tSceneAction.getNodeType().equals(NodeTypeEnum.single.getValue())) {
								TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
										.queryDeviceConfigBySerialID(tSceneAction.getActionid());
								// TOboxDeviceConfig tOboxDeviceConfig =
								// DeviceBusiness.queryDeviceConfigByID(tSceneAction.getActionID());
								if (tOboxDeviceConfig != null) {
									SceneActionDTO sceneActionDTO = new SceneActionDTO();
									sceneActionDTO.setAction(tSceneAction.getAction());
									sceneActionDTO.setActionName(tOboxDeviceConfig.getDeviceId());
									sceneActionDTO.setDeviceRfAddr(tOboxDeviceConfig.getDeviceRfAddr());
									sceneActionDTO.setDeviceSerialId(tOboxDeviceConfig.getDeviceSerialId());
									sceneActionDTO.setOboxSerialId(tOboxDeviceConfig.getOboxSerialId());
									sceneActionDTO.setDeviceChildType(tOboxDeviceConfig.getDeviceChildType());
									sceneActionDTO.setDeviceType(tOboxDeviceConfig.getDeviceType());
									sceneActionDTO.setNodeType(NodeTypeEnum.single.getValue());
									tActionDTOs.add(sceneActionDTO);
								}
							} else {
								TServerGroup tServerGroup = null;
								if (NumberHelper.isNumeric(tSceneAction.getActionid()))
									tServerGroup = serverGroupService
											.querySererGroupById(Integer.parseInt(tSceneAction.getActionid()));
								// TServerGroup tServerGroup =
								// DeviceBusiness.querySererGroupById(tSceneAction.getActionID());
								if (tServerGroup != null) {
									SceneActionDTO sceneActionDTO = new SceneActionDTO();
									sceneActionDTO.setAction(tSceneAction.getAction());
									sceneActionDTO.setActionName(tServerGroup.getGroupName());
									sceneActionDTO.setDeviceChildType(tServerGroup.getGroupChildType());
									sceneActionDTO.setDeviceType(tServerGroup.getGroupType());
									sceneActionDTO.setGroupId(String.valueOf(tServerGroup.getId()));
									sceneActionDTO.setNodeType(NodeTypeEnum.group.getValue());
									tActionDTOs.add(sceneActionDTO);
								}
							}

						}
					}
					sceneDTO.setActions(tActionDTOs);

					tDtos.add(sceneDTO);
				}
			}
			map.put("scenes", tDtos);
			res.setData(map);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			// JsonObject respJsonObject = respRight();

			// respJsonObject.add(RespFiledEnum.scenes.name(), new
			// Gson().toJsonTree(tDtos));

			// return respJsonObject;
		} catch (Exception e) {
			logger.error("===querySceneLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param userId
	 * @param location
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/setSceneLocation/{userId}/{location}/{sceneNumber}", method = RequestMethod.POST)
	ResponseObject setSceneLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location,
			@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			// TLocation tLocation = queryLocationByWeight(user,
			// Integer.parseInt(location));
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TScene tScene = sceneService.querySceneBySceneNumberAndUserId(sceneNumber, userId);
			// TScene tScene = querySceneByWeight(user,
			// Integer.parseInt(sceneNumber));
			if (tScene == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TLocationScene tLocationScene = new TLocationScene();
			tLocationScene.setLocationId(tLocation.getId());
			tLocationScene.setSceneNumber(tScene.getSceneNumber());
			locationSceneService.addSceneLocation(tLocationScene);
			// SceneBusiness.addSceneLocation(tLocationScene);

			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===setSceneLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param userId
	 * @param location
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteSceneLocation/{userId}/{location}/{sceneNumber}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location,
			@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			// TLocation tLocation = queryLocationByWeight(user,
			// Integer.parseInt(location));
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TScene tScene = sceneService.querySceneBySceneNumberAndUserId(sceneNumber, userId);
			// TScene tScene = querySceneByWeight(user,
			// Integer.parseInt(sceneNumber));
			if (tScene == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			locationSceneService.deleteSceneLocation(tScene.getSceneNumber(), location);
			// SceneBusiness.deleteSceneLocation(tScene.getSceneNumber(),
			// tLocation.getId());
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===setSceneLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param tLocation
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateLocationByObj", method = RequestMethod.PUT)
	ResponseObject updateLocationByObj(@RequestBody TLocation tLocation) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			locationService.updateLocation(tLocation);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===updateLocationByObj error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param tLocation
	 * @Description:
	 */
	@RequestMapping(value = "/addLocation", method = RequestMethod.POST)
	ResponseObject<TLocation> addLocation(@RequestBody TLocation tLocation) {
		ResponseObject<TLocation> res = new ResponseObject<TLocation>();
		try {
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			locationService.addLocation(tLocation);
			res.setData(tLocation);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===updateLocationByObj error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param tUserLocation
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addUserLocation", method = RequestMethod.POST)
	ResponseObject addUserLocation(@RequestBody TUserLocation tUserLocation) {

		ResponseObject res = new ResponseObject();
		try {
			if (tUserLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			userLocationService.addUserLocation(tUserLocation);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===updateLocationByObj error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param location
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/queryLocationByLocationId/{location}", method = RequestMethod.GET)
	ResponseObject<TLocation> queryLocationByLocationId(@PathVariable(value = "location") Integer location){
		ResponseObject<TLocation> res = new ResponseObject<TLocation>();
		try {
			res.setData(locationService.queryLocationById(location));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===updateLocationByObj error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	}
}