package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TUser;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月9日
 * @Version:1.1.0
 */
public interface MsgService {
	static final String appKey = "key-c798706a897d37c8757f966806ce61e3";
	static final String batch_send = "http://sms-api.luosimao.com/v1/send_batch.json";
	static final String send = "http://sms-api.luosimao.com/v1/send.json";

	String sendAlter(String msg, List<TUser> list);

	String sendWelcome(String phone);

	String sendGoodBye(String phone);

	String sendNotice(String phone, String msg);

	String sendAuthCode(int code, String phone);

	String sendCode(int code, String phone);

	 
}
