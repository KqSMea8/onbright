package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TMsg;
import com.bright.apollo.common.entity.TUSerGroup;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月31日  
 *@Version:1.1.0  
 */
public class USerGroupProvider {
	public String addUserGroup(final TUSerGroup tUserGroup) {
		return new SQL() {
			{
				INSERT_INTO("t_user_group");
				if (!StringUtils.isEmpty(tUserGroup.getUserId())) {
					VALUES("user_id", "#{userId}");
				}
				if (!StringUtils.isEmpty(tUserGroup.getGroupId())) {
					VALUES("group_id", "#{groupId}");
				}
				 
			}
		}.toString();
	}
}
