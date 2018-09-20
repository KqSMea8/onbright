package com.bright.apollo.dao.device.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月19日  
 *@Version:1.1.0  
 */
public class UserProvider {

	public String updateUser(final TUser user) {
		return new SQL() {
			{
				UPDATE("t_user");
				if (!StringUtils.isEmpty(user.getPassword())) {
					SET("password", "#{password}");
				}
				if (!StringUtils.isEmpty(user.getOpenId())) {
					SET("open_id", "#{openId}");
				}
				if (!StringUtils.isEmpty(user.getNickname())) {
					SET("nickname", "#{nickname}");
				}
				if (!StringUtils.isEmpty(user.getHeadimgurl())) {
					SET("headimgurl", "#{headimgurl}");
				}
				if (!StringUtils.isEmpty(user.getHeadimgurl())) {
					SET("headimgurl", "#{headimgurl}");
				}	
				WHERE("id=#{id}");
			}
		}.toString();
	}

}
