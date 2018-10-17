package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.common.entity.TIntelligentFingerWarn;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */
public class IntelligentFingerWarnDynaSqlProvider {
	public String addTIntelligentFingerWarn(final TIntelligentFingerWarn fingerWarn) {
		return new SQL() {
			{
				INSERT_INTO("t_intelligent_finger_warn");
				if (!StringUtils.isEmpty(fingerWarn.getSerialid())) {
					VALUES("serialId", "#{serialid}");
				}
				if (!StringUtils.isEmpty(fingerWarn.getOperation())) {
					VALUES("operation", "#{operation}");
				}
			}
		}.toString();
	}
}
