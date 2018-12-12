package com.bright.apollo.dao.sqlProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.boot.autoconfigure.BackgroundPreinitializer;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TRemoteLed;
import com.bright.apollo.common.entity.TUser;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月11日  
 *@Version:1.1.0  
 */
public class TRemoteLedSqlProvider {

	public String batchRemoteLeds(Map<String, List<TRemoteLed>> map) {
		List<TRemoteLed> list = map.get("list");
 		StringBuilder sb = new StringBuilder("insert into t_remote_led (serialId,`name`,`channel`) values ");
 		MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].serialid,jdbcType=VARCHAR}, " +
                "#'{'list[{0}].name,jdbcType=VARCHAR}, #'{'list[{0}].channel,jdbcType=VARCHAR})");
 		for (int i = 0; i < list.size(); i++) {
 			sb.append(messageFormat.format(new Integer[]{i}));
 			sb.append(",");
        }
 		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
	 


	public String updateRemote(final TRemoteLed remoteLed) {
		return new SQL() {
			{
				UPDATE("t_remote_led");
				if (!StringUtils.isEmpty(remoteLed.getName())) {
					SET("name=#{name}");
				}
				if (!StringUtils.isEmpty(remoteLed.getChannel())) {
					SET("channel=#{channel}");
				}
				if (!StringUtils.isEmpty(remoteLed.getSerialid())) {
					SET("serialId=#{serialid}");
				}
				 	
				WHERE("id=#{id}");
			}
		}.toString();
	}


	public static void main(String[] args) {
		List<TRemoteLed> list=new ArrayList<TRemoteLed>();
		for (int i = 0; i <2; i++) {
			TRemoteLed led=new TRemoteLed(i+"", "111111", i+"dfasfas");
			list.add(led);
		}
		Map<String, List<TRemoteLed>> map=new HashMap<String, List<TRemoteLed>>();
		map.put("list", list);
		System.out.println(new TRemoteLedSqlProvider().batchRemoteLeds(map));
	}

}
