package com.bright.apollo.dao.device.sqlProvider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月8日
 * @Version:1.1.0
 */
public class IntelligentFingerRemoteUserDynaSqlProvider {

	public String IntelligentFingerRemoteUser(final TIntelligentFingerRemoteUser fingerRemoteUser) {
		return new SQL() {
			{
				INSERT_INTO("t_intelligent_finger_remote_user");
				if (fingerRemoteUser.getUserSerialid() != null && fingerRemoteUser.getUserSerialid().intValue() != 0) {
					VALUES("user_serialId", "#{userSerialid}");
				}
				if (fingerRemoteUser.getMobile() != null) {
					VALUES("mobile", "#{mobile}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getSerialid())) {
					VALUES("serialId", "#{serialid}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getNickName())) {
					VALUES("nick_name", "#{nickName}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getStartTime())) {
					VALUES("start_time", "#{startTime}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getEndTime())) {
					VALUES("end_time", "#{endTime}");
				}
				if (fingerRemoteUser.getTimes() != null) {
					VALUES("times", "#{times}");
				}
				if (fingerRemoteUser.getUseTimes() != null) {
					VALUES("use_times", "#{useTimes}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getPwd())) {
					VALUES("pwd", "#{pwd}");
				}
				if (fingerRemoteUser.getIsend() != null) {
					VALUES("isEnd", "#{isend}");
				}
				if (fingerRemoteUser.getIsmax() != null) {
					VALUES("isMax", "#{ismax}");
				}
			}
		}.toString();
	}

	public String updateTintelligentFingerRemoteUser(final TIntelligentFingerRemoteUser fingerRemoteUser) {
		return new SQL() {
			{
				UPDATE("t_intelligent_finger_remote_user");
				if (fingerRemoteUser.getUserSerialid() != null && fingerRemoteUser.getUserSerialid().intValue() != 0) {
					SET("user_serialId", "#{userSerialid}");
				}
				if (fingerRemoteUser.getMobile() != null) {
					SET("mobile", "#{mobile}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getSerialid())) {
					SET("serialId", "#{serialid}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getNickName())) {
					SET("nick_name", "#{nickName}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getStartTime())) {
					SET("start_time", "#{startTime}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getEndTime())) {
					SET("end_time", "#{endTime}");
				}
				if (fingerRemoteUser.getTimes() != null) {
					SET("times", "#{times}");
				}
				if (fingerRemoteUser.getUseTimes() != null) {
					SET("use_times", "#{useTimes}");
				}
				if (!StringUtils.isEmpty(fingerRemoteUser.getPwd())) {
					SET("pwd", "#{pwd}");
				}
				if (fingerRemoteUser.getIsend() != null) {
					SET("isEnd", "#{isend}");
				}
				if (fingerRemoteUser.getIsmax() != null) {
					SET("isMax", "#{ismax}");
				}
				WHERE("id=#{id}");
			}
		}.toString();
	}
}
