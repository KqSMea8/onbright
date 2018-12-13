package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerAuth;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月9日  
 *@Version:1.1.0  
 */
public class TIntelligentFingerAuthDynaSqlProvider {
	public String updateTintelligentFingerAuth(final TIntelligentFingerAuth fingerAuth) {
		return new SQL() {
			{
				UPDATE("t_intelligent_finger_auth");
				if (!StringUtils.isEmpty(fingerAuth.getSerialid())) {
					SET("serialId=#{serialid}");
				}
				if (!StringUtils.isEmpty(fingerAuth.getPwd())) {
					SET("pwd=#{pwd}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
}
