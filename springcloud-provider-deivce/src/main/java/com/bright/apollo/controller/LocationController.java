package com.bright.apollo.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TDeviceLocation;
import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUserLocation;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.DeviceLocationService;
import com.bright.apollo.service.LocationService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.UserLocationService;
import com.zz.common.util.StringUtils;

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
			@RequestBody(required = false) List<String> mList) {
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
					tUserLocation.setLocationId(tLocationId);
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
				//搭建完图片服务器再做处理
				if (tLocation.getDownloadUrl() != null) {
					File dFile = new File("/data" + tLocation.getDownloadUrl().replace("%20", " "));
					if (dFile != null) {
						dFile.delete();
					}
				}
				if (tLocation.getThumUrl() != null) {
					File tFile = new File("/data" + tLocation.getThumUrl().replace("%20", " "));
					if (tFile != null) {
						tFile.delete();
					}
				}
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
			@RequestParam(value = "building",required=false) String building,
			@RequestParam(name="room",required=false) String room,
			@RequestBody(required=true)List<String> mList
			) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// String location = null;
		try {
			//覆盖，先删除location里面的设备(删映射)，再添加进去（添加映射）。
			//Assert.notNull(serialId, "serialId can't be null!");
			List<TDeviceLocation> devices = deviceLocationService.queryDevicesByLocation(location);
			//List<TDeviceLocation> devices = DeviceBusiness.queryDevicesByLocation(Integer.parseInt(location));
			if (devices != null) {
				for (TDeviceLocation device : devices) {
					deviceLocationService.deleteDeviceLocation(device.getId());
					//DeviceBusiness.deleteDeviceLocation(device.getId());
				}
			}
			TLocation tLocation = locationService.queryLocationByUserIdAndId(userId, location);
			//TLocation tLocation = queryLocationByWeight(user, Integer.parseInt(location));
			if (tLocation == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}else {
				if (!StringUtils.isEmpty(building) || !StringUtils.isEmpty(room)) {
					tLocation.setBuilding(building);
					tLocation.setRoom(room);
					locationService.updateLocation(tLocation);
					//DeviceBusiness.updateLocation(tLocation);
				}				
			}
			if (mList!=null&&mList.size()>0) {
				//@SuppressWarnings("unchecked")
				//List<String> mList = (List<String>) ObjectUtils.fromJsonToObject(serialId, List.class);
				for (String string : mList) {
					TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getDeviceByUserAndSerialId(userId, string);
					//TOboxDeviceConfig tOboxDeviceConfig = queryDeviceByWeight(user, string);
					if (tOboxDeviceConfig == null) {
						TObox tObox =oboxService.getOboxByUserAndoboxSerialId(userId, string);
						//TObox tObox = queryOboxByWeight(user, string);
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
							//DeviceBusiness.addDeviceLocation(tDeviceLocation);
						}
					} else {// 设备为device的情况
						TDeviceLocation tDeviceLocation = new TDeviceLocation();

						tDeviceLocation.setLocation(location);
						tDeviceLocation.setxAxis(0);
						tDeviceLocation.setyAxis(0);
						tDeviceLocation.setSerialId(tOboxDeviceConfig.getDeviceSerialId());
						tDeviceLocation.setDeviceType(tOboxDeviceConfig.getDeviceType());
						deviceLocationService.addDeviceLocation(tDeviceLocation);
						//DeviceBusiness.addDeviceLocation(tDeviceLocation);
					}
				}
			}else {
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
	@RequestMapping(value = "/addDeviceLocation/{serialId}/{location}/{xAxis}/{yAxis}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addDeviceLocation(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "location") Integer location, @PathVariable(value = "xAxis") Integer xAxis,
			@PathVariable(value = "yAxis") Integer yAxis) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		// String location = null;
		try {

		} catch (Exception e) {
			logger.error("===addDeviceLocation error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

}