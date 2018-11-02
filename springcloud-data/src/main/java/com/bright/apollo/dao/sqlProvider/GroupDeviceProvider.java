package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TException;
import com.bright.apollo.common.entity.TGroupDevice;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月1日  
 *@Version:1.1.0  
 */
public class GroupDeviceProvider {

	public String addDeviceGroup(final TGroupDevice groupDevice) {
		return new SQL() {
			{
				INSERT_INTO("t_group_device");
				if (!StringUtils.isEmpty(groupDevice.getDeviceSerialId())) {
					VALUES("device_serial_id", "#{deviceSerialId}");
				}
				if (!StringUtils.isEmpty(groupDevice.getGroupId())) {
					VALUES("group_id", "#{groupId}");
				}
				 
		 
			}
		}.toString();
	}

}
