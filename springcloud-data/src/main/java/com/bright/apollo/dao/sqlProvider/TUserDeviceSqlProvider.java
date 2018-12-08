package com.bright.apollo.dao.sqlProvider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.bright.apollo.common.entity.TUserDevice;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月8日  
 *@Version:1.1.0  
 */
public class TUserDeviceSqlProvider {
	public String batchAddUserDevice(Map<String, List<TUserDevice>> map) {
		List<TUserDevice> userDevices = map.get("userDevices");
 		StringBuilder sb = new StringBuilder("insert into t_user_device (device_serial_id,user_id) values ");
 		MessageFormat messageFormat = new MessageFormat("(" +
                "#'{'userDevices[{0}].deviceSerialId,jdbcType=VARCHAR}, #'{'userDevices[{0}].userId,jdbcType=INTEGER})");
 		for (int i = 0; i < userDevices.size(); i++) {
 			sb.append(messageFormat.format(new Integer[]{i}));
 			sb.append(",");
        }
 		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
