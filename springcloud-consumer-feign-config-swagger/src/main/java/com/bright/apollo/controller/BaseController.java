package com.bright.apollo.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import com.bright.apollo.common.entity.TDeviceStatus;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.ConditionTypeEnum;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.enums.EnvironmentalSensorEnum;
import com.bright.apollo.request.IntelligentFingerRemoteUserDTO;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
import com.bright.apollo.request.IntelligentFingerWarnItemDTO;
import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.request.TIntelligentFingerPushDTO;
import com.bright.apollo.response.DeviceStatusDTO;
import com.bright.apollo.response.IntelligentOpenRecordItemDTO;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.DateHelper;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月28日  
 *@Version:1.1.0  
 */
public class BaseController {
	public static String salt = "eqcs231@gfdgaqweqxaa4648}{";
	public final static long max_waitting_time = 15000l;
	/**
	 * @param sceneConditionDTOs
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	protected static boolean isNotCommonObox(List<List<SceneConditionDTO>> sceneConditionDTOs, String oboxSerialId) {
		for (int i = 0; i < sceneConditionDTOs.size(); i++) {
			List<SceneConditionDTO> list = sceneConditionDTOs.get(i);
			if (list != null && list.size() > 0) {
				for (SceneConditionDTO conditionDTO : list) {
					if (conditionDTO.getConditionType().equals(ConditionTypeEnum.device.getValue())
							&& !conditionDTO.getOboxSerialId().equals(oboxSerialId)) {
						if (!conditionDTO.getOboxSerialId().equals(oboxSerialId))
							return true;
					}

				}
			}
		}
		return false;
	}
	protected static String dateToString(java.util.Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ctime = formatter.format(time);

		return ctime;
	}
	protected static String extractPathFromPattern(final HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	}
	/**
	 * @param startTime
	 * @return
	 * @Description:
	 */
	protected static List<IntelligentOpenRecordItemDTO> init(long startTime) {
		List<IntelligentOpenRecordItemDTO> items = new ArrayList<IntelligentOpenRecordItemDTO>();
		for (long i = 0; i < 30; i++) {
			IntelligentOpenRecordItemDTO item = new IntelligentOpenRecordItemDTO();
			item.setDateline(DateHelper.formatDate(startTime - i * 24 * 60 * 60 * 1000, DateHelper.FORMAT));
			item.setList(new ArrayList<IntelligentOpenRecordDTO>());
			items.add(item);
		}
		return items;
	}
	/**
	 * @param dto
	 * @param items
	 * @param startTime
	 * @Description:
	 */
	protected static void handlerDTO(IntelligentOpenRecordDTO dto, List<IntelligentOpenRecordItemDTO> items, long startTime) {
		if (dto.getTimeStamp() <= startTime) {
			int temp = (int) ((startTime - dto.getTimeStamp()) / (24 * 60 * 60 * 1000));
			if (temp <= 30) {
				dto.setOpenTime(DateHelper.formatDate(dto.getTimeStamp(), DateHelper.FORMATHOUR));
				items.get(temp).getList().add(dto);
			}
		}
	}
	/**
	 * @param startTime
	 * @return
	 * @Description:
	 */
	protected static List<IntelligentFingerWarnItemDTO> initWarnRecord(long startTime) {
		List<IntelligentFingerWarnItemDTO> items = new ArrayList<IntelligentFingerWarnItemDTO>();
		for (long i = 0; i < 30; i++) {
			IntelligentFingerWarnItemDTO item = new IntelligentFingerWarnItemDTO();
			item.setDateline(DateHelper.formatDate(startTime - i * 24 * 60 * 60 * 1000, DateHelper.FORMAT));
			item.setList(new ArrayList<IntelligentFingerWarnDTO>());
			items.add(item);
		}
		return items;
	}
	/**
	 * @param dto
	 * @param items
	 * @param startTime
	 * @Description:
	 */
	protected static void handlerWarnDTO(IntelligentFingerWarnDTO dto, List<IntelligentFingerWarnItemDTO> items,
			long startTime) {
		if (dto.getTimeStamp() <= startTime) {
			int temp = (int) ((startTime - dto.getTimeStamp()) / (24 * 60 * 60 * 1000));
			if (temp <= 30) {
				dto.setWarnTime(DateHelper.formatDate(dto.getTimeStamp(), DateHelper.FORMATHOUR));
				items.get(temp).getList().add(dto);
			}
		}
	}
	/**
	 * @param data
	 * @return
	 * @Description:
	 */
	protected static List<IntelligentFingerRemoteUserDTO> transformToDTO(List<TIntelligentFingerRemoteUser> list) {
		List<IntelligentFingerRemoteUserDTO> dtos = new ArrayList<IntelligentFingerRemoteUserDTO>();
		for (TIntelligentFingerRemoteUser user : list) {
			dtos.add(new IntelligentFingerRemoteUserDTO(user));
		}
		return dtos;
	}

	protected static String sessionKey(int uid, String appKey) {
		if (!StringUtils.isEmpty(appKey))
			return appKey + "#" + uid;
		return "#" + uid;
	}

	/**
	 * @param list
	 * @return
	 * @Description:
	 */
	protected static List<TIntelligentFingerPushDTO> pushToDTO(List<TIntelligentFingerPush> list) {
		if (list == null)
			return null;
		List<TIntelligentFingerPushDTO> dtos = new ArrayList<TIntelligentFingerPushDTO>();
		for (TIntelligentFingerPush push : list) {
			dtos.add(new TIntelligentFingerPushDTO(push));
		}
		return dtos;
	}
	protected static boolean isEnvironmentalSensor(TOboxDeviceConfig config){
		boolean flag=false;
		if(config!=null&&config.getDeviceChildType().equals(
				DeviceTypeEnum.sensor_environment.getValue()))
			flag=true;
		return flag;
	}
	protected static long getDistanceTimes(java.util.Date one, java.util.Date two) {
		long day = 0;
		try {

			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff;
			if (time1 < time2) {
				diff = time2 - time1;
			} else {
				diff = time1 - time2;
			}
			day = diff / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return day;
	}
	protected static DeviceStatusDTO handlerDeviceStatusDTO(long from,
			TOboxDeviceConfig config, TDeviceStatus status, boolean isEnviron) {
		DeviceStatusDTO deviceStatusDTO = new DeviceStatusDTO();
		if (isEnviron) {
			if (status != null)
				deviceStatusDTO.setStatus(calculateStatus(status.getDeviceState()));
			else
				deviceStatusDTO.setStatus(calculateStatus(config.getDeviceState()));
		} else {
			if (status != null)
				deviceStatusDTO.setStatus(status.getDeviceState());
			else
				deviceStatusDTO.setStatus(config.getDeviceState());
		}
		deviceStatusDTO.setTime(from);
		return deviceStatusDTO;
	}
	protected static String calculateStatus(String status) {
		if (!StringUtils.isEmpty(status) || status.length() == 24) {
			String TVOC = status.substring(0, 1);
			String PM = status.substring(4, 5);
			String CO = status.substring(8, 9);
			String TEMPERATURE = status.substring(12, 13);
			String HUMIDITY = status.substring(16, 17);
			String CO2 = status.substring(20, 21);
			if (TVOC.equals(EnvironmentalSensorEnum.TVOC.getValue())
					&& PM.equals(EnvironmentalSensorEnum.PM.getValue())
					&& CO.equals(EnvironmentalSensorEnum.CO.getValue())
					&& TEMPERATURE.equals(EnvironmentalSensorEnum.TEMPERATURE
							.getValue())
					&& HUMIDITY.equals(EnvironmentalSensorEnum.HUMIDITY
							.getValue())
					&& CO2.equals(EnvironmentalSensorEnum.CO2.getValue())) {
				StringBuilder sb = new StringBuilder();
				return sb.append(getHandler(status.substring(0, 4)))
						.append(getHandler(status.substring(4, 8)))
						.append(getHandler(status.substring(8, 12)))
						.append(getHandler(status.substring(12, 16)))
						.append(getHandler(status.substring(16, 20)))
						.append(getHandler(status.substring(20, 24)))
						.toString();
			}
		}
		return status;
	}
	private static String getHandler(String str){
		byte[] src = ByteHelper.hexStringToBytes(str);
		int byteIndexValid = ByteHelper.byteIndexValid(src[0], 0, 4);
		int validByte = ByteHelper.validByte(src[1]);
		int i=byteIndexValid<<8+validByte;
		String byteArryToHexString = ByteHelper.byteArryToHexString(ByteHelper.int2byte(i));
		return byteArryToHexString;
	}
}
