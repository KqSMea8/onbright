package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TUserAliDevice;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月17日  
 *@Version:1.1.0  
 */
public class UserAliDeviceProvider {
	public String addUserAliDev(final TUserAliDevice tUserAliDev) {
		return new SQL() {
			{
				INSERT_INTO("t_user_ali_device");
				if (!StringUtils.isEmpty(tUserAliDev.getUserId())) {
					VALUES("user_id", "#{userId}");
				}
				if (!StringUtils.isEmpty(tUserAliDev.getDeviceSerialId())) {
					VALUES("device_serial_id", "#{deviceSerialId}");
				}
				 
			}
		}.toString();
	}

}
