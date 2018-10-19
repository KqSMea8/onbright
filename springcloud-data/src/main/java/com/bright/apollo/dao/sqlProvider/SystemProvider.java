package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TSystem;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月18日  
 *@Version:1.1.0  
 */
public class SystemProvider {
	public String addSystem(final TSystem tSystem) {
		return new SQL() {
			{
				INSERT_INTO("t_system");
				if (!StringUtils.isEmpty(tSystem.getChildType())) {
					VALUES("child_type", "#{childType}");
				}
				if (!StringUtils.isEmpty(tSystem.getRelevancyId())) {
					VALUES("relevancy_id", "#{relevancyId}");
				}
				if (!StringUtils.isEmpty(tSystem.getType())) {
					VALUES("type", "#{type}");
				}
 
			}
		}.toString();
	}
}
