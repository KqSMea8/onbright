package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TDeviceLocation;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年11月23日
 * @Version:1.1.0
 */
public class DeviceLocationProvider {

	public String addDeviceLocation(final TDeviceLocation tDeviceLocation) {
		return new SQL() {
			{
				INSERT_INTO("t_device_location");
				if (StringUtils.isEmpty(tDeviceLocation.getLocation())) {
					VALUES("location", "#{location}");
				}

				if (!StringUtils.isEmpty(tDeviceLocation.getxAxis())) {
					VALUES("x_axis", "#{xAxis}");
				}
				if (!StringUtils.isEmpty(tDeviceLocation.getyAxis())) {
					VALUES("y_axis", "#{yAxis}");
				}
				if (!StringUtils.isEmpty(tDeviceLocation.getSerialId())) {
					VALUES("serialId", "#{serialId}");
				}
				if (!StringUtils.isEmpty(tDeviceLocation.getDeviceType())) {
					VALUES("device_type", "#{deviceType}");
				}
			}
		}.toString();
	}

}
