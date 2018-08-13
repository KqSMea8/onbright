package com.bright.apollo.cache;

import com.bright.apollo.enums.AliRegionEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.redis.RedisBussines;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年5月4日
 * @Version:1.1.0
 */
@Component
public class AliDevCache {

	@Autowired
	private RedisBussines redisBussines;

	private static int dev_available_time = 60 * 10;

	private static int dev_control_time = 60 * 60 * 24 * 7;

	public void saveAliDevWait(String productKey, String deviceName, String region) {
		redisBussines.setValueWithExpire(aliDevAvailableKey(productKey, deviceName), region, dev_available_time);

	}

	public void delAliDevWait(String productKey, String deviceName) {
		redisBussines.delete(aliDevAvailableKey(productKey, deviceName));
	}

	public String getAliDevAvailable(String productKey, String deviceName) {
		return redisBussines.get(aliDevAvailableKey(productKey, deviceName));
	}

	public void saveDevInfo(String productKey, String obox_serial_id, String deviceName, AliRegionEnum eAliRegionEnum) {
		redisBussines.delete(aliDevAvailableKey(productKey, deviceName));
		redisBussines.setValueWithExpire(productInfoKey(obox_serial_id), productKey, dev_control_time);
		redisBussines.setValueWithExpire(devInfoKey(obox_serial_id), deviceName, dev_control_time);
		redisBussines.setValueWithExpire(regionInfoKey(obox_serial_id), eAliRegionEnum.name(), dev_control_time);
		redisBussines.setValueWithExpire(oboxInfoKey(productKey, deviceName), obox_serial_id, dev_control_time);

	}

	public void DelDevInfo(String obox_serial_id) {
		String productKey = getProductKey(obox_serial_id);
		String deviceName = getDeviceName(obox_serial_id);
		String region = getProductRegion(obox_serial_id);
		if (!StringUtils.isEmpty(productKey) && !StringUtils.isEmpty(deviceName)) {
			String serialId = getOboxSerialId(productKey, deviceName);
			if (!StringUtils.isEmpty(serialId)) {
				redisBussines.delete(oboxInfoKey(productKey, deviceName));

			}
		}
		if (!StringUtils.isEmpty(productKey)) {
			redisBussines.delete(productInfoKey(obox_serial_id));
		}
		if (!StringUtils.isEmpty(deviceName)) {
			redisBussines.delete(devInfoKey(obox_serial_id));
		}
		if (!StringUtils.isEmpty(region)) {
			redisBussines.delete(regionInfoKey(obox_serial_id));
		}
	}

	public String getProductKey(String obox_serial_id) {
		return redisBussines.get(productInfoKey(obox_serial_id));
	}

	public String getProductRegion(String obox_serial_id) {
		return redisBussines.get(regionInfoKey(obox_serial_id));
	}

	public String getDeviceName(String obox_serial_id) {
		return redisBussines.get(devInfoKey(obox_serial_id));
	}

	public String getOboxSerialId(String productKey, String deviceName) {
		return redisBussines.get(oboxInfoKey(productKey, deviceName));
	}

	public void setKey(String key,String value,long time){
		redisBussines.setValueWithExpire(key,value,time);
	}

	public String getValue(String key){
		return redisBussines.get(key);
	}


	private static String aliDevAvailableKey(String productKey, String deviceName) {
		return "available_" + productKey + "_" + deviceName;
	}

	private static String productInfoKey(String obox_serial_id) {
		return "productKey_" + obox_serial_id;
	}

	private static String devInfoKey(String obox_serial_id) {
		return "deviceName_" + obox_serial_id;
	}

	private static String regionInfoKey(String obox_serial_id) {
		return "region_" + obox_serial_id;
	}

	private static String oboxInfoKey(String productKey, String deviceName) {
		return productKey + "_" + deviceName;
	}

}
