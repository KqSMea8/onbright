package com.bright.apollo.dao.device.sqlProvider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.bright.apollo.common.entity.TIntelligentFingerPush;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年9月4日
 * @Version:1.1.0
 */
public class TIntelligentFingerPushSqlProvider {
	public String batchTIntelligentFingerPush(Map<String, List<TIntelligentFingerPush>> map) {
		List<TIntelligentFingerPush> list = map.get("list");
 		StringBuilder sb = new StringBuilder("insert into t_intelligent_finger_push (serialId,`cmd`,`value`) values ");
 		MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].serialid,jdbcType=VARCHAR}, " +
                "#'{'list[{0}].cmd,jdbcType=VARCHAR}, #'{'list[{0}].value,jdbcType=VARCHAR})");
 		for (int i = 0; i < list.size(); i++) {
 			sb.append(messageFormat.format(new Integer[]{i}));
 			sb.append(",");
        }
 		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
