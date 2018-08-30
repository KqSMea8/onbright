package com.bright.apollo.dao.device.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TDeviceStatus;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月30日  
 *@Version:1.1.0  
 */
public class DeviceStatusSqlProvider {

	public String addDeviceStatus(final TDeviceStatus tDeviceStatus) {
		return new SQL() {
			{
				INSERT_INTO("t_device_status");
				if (!StringUtils.isEmpty(tDeviceStatus.getDeviceSerialId())) {
					VALUES("device_serial_id", "#{deviceSerialId}");
				}
				if (!StringUtils.isEmpty(tDeviceStatus.getDeviceState())) {
					VALUES("device_state", "#{deviceState}");
				}
			}
		}.toString();
	}

}
