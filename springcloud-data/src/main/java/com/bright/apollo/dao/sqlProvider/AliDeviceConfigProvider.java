package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月17日  
 *@Version:1.1.0  
 */
public class AliDeviceConfigProvider {
	public String addAliDevConfig(final TAliDeviceConfig tAliDeviceConfig) {
		return new SQL() {
			{
				INSERT_INTO("t_ali_device_config");
				if (StringUtils.isEmpty(tAliDeviceConfig.getDeviceSerialId())) {
					VALUES("device_serial_id", "#{deviceSerialId}");
				}

				if (!StringUtils.isEmpty(tAliDeviceConfig.getType())) {
					VALUES("type", "#{type}");
				}
				if (!StringUtils.isEmpty(tAliDeviceConfig.getAction())) {
					VALUES("action", "#{action}");
				}
				if (!StringUtils.isEmpty(tAliDeviceConfig.getState())) {
					VALUES("state", "#{state}");
				}
				if (!StringUtils.isEmpty(tAliDeviceConfig.getName())) {
					VALUES("name", "#{name}");
				}
			}
		}.toString();
	}
}
