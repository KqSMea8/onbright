package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TLocation;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
public class LocationProvider {
	public String addLocation(final TLocation tLocation) {
		return new SQL() {
			{
				INSERT_INTO("t_location");
				if (!StringUtils.isEmpty(tLocation.getBuilding())) {
					VALUES("building", "#{building}");
				}

				if (!StringUtils.isEmpty(tLocation.getRoom())) {
					VALUES("room", "#{room}");
				}
				if (!StringUtils.isEmpty(tLocation.getDownloadUrl())) {
					VALUES("download_url", "#{downloadUrl}");
				}
				if (!StringUtils.isEmpty(tLocation.getLicense())) {
					VALUES("license", "#{license}");
				}
				if (!StringUtils.isEmpty(tLocation.getThumUrl())) {
					VALUES("thum_url", "#{thumUrl}");
				}
				if (!StringUtils.isEmpty(tLocation.getStatus())) {
					VALUES("status", "#{status}");
				}
				if (tLocation.getUserName()!=null) {
					VALUES("user_name", "#{userName}");
				}
			}
		}.toString();
	}
	public String updateLocation(final TLocation tLocation) {
 
			return new SQL() {
				{
					UPDATE("t_location");
					if (!StringUtils.isEmpty(tLocation.getBuilding())) {
						SET("building=#{building}");
					}
					if (!StringUtils.isEmpty(tLocation.getRoom())) {
						SET("room=#{room}");
					}
					if (!StringUtils.isEmpty(tLocation.getThumUrl())) {
						SET("thum_url=#{thumUrl}");
					}
					if (!StringUtils.isEmpty(tLocation.getDownloadUrl())) {
						SET("download_url=#{downloadUrl}");
					}
					WHERE("id=#{id}");
				}
			}.toString();
		
 
	}
}
