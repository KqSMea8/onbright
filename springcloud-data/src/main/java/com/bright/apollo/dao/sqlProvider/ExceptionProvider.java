package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TException;
import com.bright.apollo.common.entity.TMsg;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月19日  
 *@Version:1.1.0  
 */
public class ExceptionProvider {
	public String addException(final TException tException) {
		return new SQL() {
			{
				INSERT_INTO("t_exception");
				if (!StringUtils.isEmpty(tException.getChildType())) {
					VALUES("child_type", "#{childType}");
				}
				if (!StringUtils.isEmpty(tException.getDeviceName())) {
					VALUES("device_name", "#{deviceName}");
				}
				if (!StringUtils.isEmpty(tException.getDeviceSerialId())) {
					VALUES("device_serial_id", "#{deviceSerialId}");
				}
				if (!StringUtils.isEmpty(tException.getRelevancyId())) {
					VALUES("relevancy_id", "#{relevancyId}");
				}
				if (!StringUtils.isEmpty(tException.getState())) {
					VALUES("state", "#{state}");
				}
				if (!StringUtils.isEmpty(tException.getType())) {
					VALUES("type", "#{type}");
				}
		 
			}
		}.toString();
	}
}
