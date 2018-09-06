package com.bright.apollo.dao.device.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerRecord;
import com.bright.apollo.common.entity.TIntelligentFingerUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */
public class TIntelligentFingerRecordDynaSqlProvider {
	public String addIntelligentFingerRecord(final TIntelligentFingerRecord fingerRecord) {
		return new SQL() {
			{
				INSERT_INTO("t_intelligent_finger_record");
				if (!StringUtils.isEmpty(fingerRecord.getSerialid())) {
					VALUES("serialId", "#{serialid}");
				}
				if (!StringUtils.isEmpty(fingerRecord.getNickName())) {
					VALUES("nick_name", "#{nickName}");
				}
				if (!StringUtils.isEmpty(fingerRecord.getFingerUserId())) {
					VALUES("finger_user_id", "#{fingerUserId}");
				}
				if (!StringUtils.isEmpty(fingerRecord.getOperation())) {
					VALUES("operation", "#{operation}");
				}
			}
		}.toString();
	}
}
