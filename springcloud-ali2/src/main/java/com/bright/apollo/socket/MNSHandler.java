package com.bright.apollo.socket;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.Message;
import com.bright.apollo.enums.ALIDevTypeEnum;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月26日
 * @Version:1.1.0
 */
@Component
public class MNSHandler {
	Logger logger = Logger.getLogger(MNSHandler.class);
	private static final String accessKeyId = "LTAIBE0b86xFi9q5";// LTAImm6aizjagsfp
	private static final String accessKeySecret = "Ym9F1CNAgwhbxt5Sk1Qki1nr6w6e3v";// zNdZ9RuwSU7RG2Lkoon9i2hbVx3gsm
	private static final String endPoint = "http://1563722132092243.mns.cn-shanghai.aliyuncs.com/";// http(s)://1563722132092243.mns.cn-hangzhou.aliyuncs.com/
	private static final String queueStr = "aliyun-iot-" + ALIDevTypeEnum.OBOX.getSouthChinaName();
	private static CloudQueue queue;

	@Autowired
	private AliMessageHandler aliMessageHandler;

	public MNSHandler() {
		CloudAccount account = new CloudAccount(accessKeyId, accessKeySecret, endPoint);
		MNSClient client = account.getMNSClient();
		queue = client.getQueueRef(queueStr);
	}

	/**
	 * 
	 * @Description:
	 */
	public void handler() {
		while (true) {
			try {
				Message popMessage = queue.popMessage(10);
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
