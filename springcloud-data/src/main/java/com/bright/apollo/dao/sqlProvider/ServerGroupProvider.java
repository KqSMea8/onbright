package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TServerGroup;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月31日  
 *@Version:1.1.0  
 */
public class ServerGroupProvider {
	public String addServerGroup(final TServerGroup tServerGroup) {
		return new SQL() {
			{
				INSERT_INTO("t_server_group");
				if (!StringUtils.isEmpty(tServerGroup.getGroupName())) {
					VALUES("group_name", "#{groupName}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupState())) {
					VALUES("group_state", "#{groupState}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupType())) {
					VALUES("group_type", "#{groupType}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupChildType())) {
					VALUES("group_child_type", "#{groupChildType}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupAddr())) {
					VALUES("group_addr", "#{groupAddr}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupStyle())) {
					VALUES("group_style", "#{groupStyle}");
				}
 
			}
		}.toString();
	}
	
	public String updateServerGroup(final TServerGroup tServerGroup) {
		return new SQL() {
			{
				UPDATE("t_server_group");
				if (!StringUtils.isEmpty(tServerGroup.getGroupName())) {
					SET("group_name", "#{groupName}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupState())) {
					SET("group_state", "#{groupState}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupType())) {
					SET("group_type", "#{groupType}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupChildType())) {
					SET("group_child_type", "#{groupChildType}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupAddr())) {
					SET("group_addr", "#{groupAddr}");
				}
				if (!StringUtils.isEmpty(tServerGroup.getGroupStyle())) {
					SET("group_style", "#{groupStyle}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
}
