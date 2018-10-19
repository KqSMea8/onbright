package com.bright.apollo.dao.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TMsg;
import com.bright.apollo.common.entity.TSystem;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月18日  
 *@Version:1.1.0  
 */
public class TMsgProvider {
	public String addMsg(final TMsg tMsg) {
		return new SQL() {
			{
				INSERT_INTO("t_msg");
				if (!StringUtils.isEmpty(tMsg.getContent())) {
					VALUES("content", "#{content}");
				}
				if (!StringUtils.isEmpty(tMsg.getRelevancyId())) {
					VALUES("relevancy_id", "#{relevancyId}");
				}
				if (!StringUtils.isEmpty(tMsg.getSendTime())) {
					VALUES("send_time", "#{sendTime}");
				}
				if (!StringUtils.isEmpty(tMsg.getState())) {
					VALUES("state", "#{state}");
				}
				if (!StringUtils.isEmpty(tMsg.getType())) {
					VALUES("type", "#{type}");
				}
				if (!StringUtils.isEmpty(tMsg.getUrl())) {
					VALUES("url", "#{url}");
				}
				if (!StringUtils.isEmpty(tMsg.getUserId())) {
					VALUES("user_id", "#{userId}");
				}
			}
		}.toString();
	}
}
