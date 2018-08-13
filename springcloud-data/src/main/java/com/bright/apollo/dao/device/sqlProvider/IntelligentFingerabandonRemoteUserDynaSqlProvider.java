package com.bright.apollo.dao.device.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月8日  
 *@Version:1.1.0  
 */
public class IntelligentFingerabandonRemoteUserDynaSqlProvider {
	public String addIntelligentFingerAbandonRemoteUser(final TIntelligentFingerAbandonRemoteUser abandonRemoteUser) {
		return new SQL() {
			{
				INSERT_INTO("t_intelligent_finger_remote_user");
				if (abandonRemoteUser.getUserSerialid() != null && abandonRemoteUser.getUserSerialid().intValue() != 0) {
					VALUES("user_serialId", "#{userSerialid}");
				}

				if (!StringUtils.isEmpty(abandonRemoteUser.getSerialid())) {
					VALUES("serialId", "#{serialid}");
				}
				 
			}
		}.toString();
	}
}
