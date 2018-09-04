package com.bright.apollo.dao.device.sqlProvider;

import java.util.List;

import com.bright.apollo.common.entity.TIntelligentFingerPush;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年9月4日
 * @Version:1.1.0
 */
public class TIntelligentFingerPushSqlProvider {
	public String batchTIntelligentFingerPush(final List<TIntelligentFingerPush> pushList, final String serialId) {
 		StringBuilder sb = new StringBuilder("insert into t_intelligent_finger_push (serialId,`cmd`,`value`) values ");
 		for (int i = 0; i < pushList.size(); i++) {
			sb.append("(").append(serialId).append(",").append(pushList.get(i).getCmd()).append(",")
			.append(pushList.get(i).getValue()).append(")");
			if (i < pushList.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
