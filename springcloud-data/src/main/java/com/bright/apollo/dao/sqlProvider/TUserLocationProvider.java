package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.common.entity.TUserLocation;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
public class TUserLocationProvider {

	public String addUserLocation(final TUserLocation tUserLocation) {
		return new SQL() {
			{
				INSERT_INTO("t_user_location");
				if (!StringUtils.isEmpty(tUserLocation.getUserId())) {
					VALUES("user_id", "#{userId}");
				}
				if (!StringUtils.isEmpty(tUserLocation.getLocationId())) {
					VALUES("location_id", "#{locationId}");
				}
				 
			}
		}.toString();
	}


}
