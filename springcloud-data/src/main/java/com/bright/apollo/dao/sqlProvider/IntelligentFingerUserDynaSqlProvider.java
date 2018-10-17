package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */
public class IntelligentFingerUserDynaSqlProvider {
	public String addIntelligentFingerUser(final TIntelligentFingerUser intelligentFingerUser) {
		return new SQL() {
			{
				INSERT_INTO("t_intelligent_finger_user");
				if (intelligentFingerUser.getPin() != null ) {
					VALUES("pin", "#{pin}");
				}
				if (intelligentFingerUser.getMobile() != null) {
					VALUES("mobile", "#{mobile}");
				}
				if (!StringUtils.isEmpty(intelligentFingerUser.getSerialid())) {
					VALUES("serialId", "#{serialid}");
				}
				if (!StringUtils.isEmpty(intelligentFingerUser.getNickName())) {
					VALUES("nick_name", "#{nickName}");
				}
				if(intelligentFingerUser.getExistForce()!=null ){
					VALUES("exist_force", "#{existForce}");
				}
				if(intelligentFingerUser.getIdentity()!=null ){
					VALUES("identity", "#{identity}");
				}
			}
		}.toString();
	}
}
