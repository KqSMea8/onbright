package com.bright.apollo.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.bright.apollo.vo.IotDevConncetion;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月26日
 * @Version:1.1.0
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class WIFIHandler {
	private static final Logger logger = LoggerFactory.getLogger(WIFIHandler.class);
	//private static final String accessKeyId = "LTAIBE0b86xFi9q5";// LTAImm6aizjagsfp
	//private static final String accessKeySecret = "Ym9F1CNAgwhbxt5Sk1Qki1nr6w6e3v";// zNdZ9RuwSU7RG2Lkoon9i2hbVx3gsm
	//private static final String endPoint = "http://1563722132092243.mns.cn-shanghai.aliyuncs.com/";// http(s)://1563722132092243.mns.cn-hangzhou.aliyuncs.com/
	//private static final String queueStr = "aliyun-iot-" + ALIDevTypeEnum.DEVICE.getSouthChinaName();
	private static CloudQueue queue;

	@Autowired
	private AliMessageHandler aliMessageHandler;
	@Autowired
	@Lazy
	private IotDevConncetion iotDevConncetion;
	public CloudQueue getCloudQueue() {
		CloudAccount account = new CloudAccount(iotDevConncetion.getAccessKeyId(), iotDevConncetion.getAccessKeySecret(), iotDevConncetion.getEndPoint());
		MNSClient client = account.getMNSClient();
		return client.getQueueRef(iotDevConncetion.getWifiSouthChinaUrl());
	}

	/**
	 * 
	 * @Description:
	 */
	public void handler() {
		logger.info("====== WIFIHandler handler ======");
		while (true) {
			try {
				if(queue==null)
					queue=getCloudQueue();
				Message popMessage = queue.popMessage(5);
				if (popMessage != null) {
					logger.info("======popMessage:" + popMessage);
					queue.deleteMessage(popMessage.getReceiptHandle());
					// handler message
					aliMessageHandler.handler(popMessage);
				}
			} catch (Exception e) {
				logger.error("===error msg:" + e.getMessage());
			}

		}

	}
}
