package com.bright.apollo.service;

import java.util.concurrent.Future;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20170420.PubRequest;
import com.aliyuncs.iot.model.v20170420.PubResponse;
import com.aliyuncs.iot.model.v20170420.RRpcRequest;
import com.aliyuncs.iot.model.v20170420.RRpcResponse;
import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.handler.CMDHandlerManager;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年5月4日
 * @Version:1.1.0
 */

@Component
public class TopicServer {

	private Logger logger = Logger.getLogger(TopicServer.class);

	private static final int packageLength = 64;
	private static final String head = "38383838";

	@Autowired
	private AliDevCache AliDevCache;

	@Autowired
	private IotClient iotClient;

	@Autowired
	private AliDeviceService aliDeviceService;

	@Autowired
	private CMDHandlerManager cmdHandlerManager;

	private String setCache(TAliDevice device, String deviceSerial) {
		logger.info(" ====== setCache start ====== ");
		String productKey = "";
		String deviceName = "";
		String region = "";
		if (device == null) {
			TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceBySerializeId(deviceSerial);
			if (tAliDeviceUS == null) {
				return null;
			}
			productKey = tAliDeviceUS.getProductKey();
			deviceName = tAliDeviceUS.getDeviceName();
			region = AliRegionEnum.AMERICA.name();
			AliDevCache.saveDevInfo(productKey, deviceSerial, deviceName, AliRegionEnum.AMERICA);

		} else {
			productKey = device.getProductKey();
			deviceName = device.getDeviceName();
			region = AliRegionEnum.SOURTHCHINA.name();
			AliDevCache.saveDevInfo(productKey, deviceSerial, deviceName, AliRegionEnum.SOURTHCHINA);
		}
		logger.info(" ====== setCache end ====== ");
		return region;
	}

	private void sendRequest(PubRequest request, String region, PubResponse response) {
		logger.info(" ====== sendRequest start ====== ");
		try {
			DefaultAcsClient client = null;
			if (region.equals(AliRegionEnum.AMERICA)) {
				client = iotClient.getClient(AliRegionEnum.AMERICA.getValue());
			} else {
				client = iotClient.getClient(AliRegionEnum.SOURTHCHINA.getValue());
			}

			if (client != null) {
				response = client.getAcsResponse(request);
			} else {
				logger.error("===the DefaultAcsClient is null");
			}
		} catch (ClientException e) {
			logger.info("====== sendRequest exception ======" + e.getMessage());
		} finally {
			logger.info(" rep success topic: " + response.getSuccess());
			logger.info(" ====== sendRequest end ====== ");
			logger.error("======rep fail topic:" + response.getErrorMessage());
			// System.out.println("rep success topic:" + response.getSuccess());
			// System.out.println("rep fail topic:" +
			// response.getErrorMessage());
		}
	}

	private RRpcResponse sendRPCRequest(RRpcRequest request, String region, RRpcResponse response) {
		logger.info(" ====== sendRPCRequest start ====== ");
		try {
			if (region.equals(AliRegionEnum.AMERICA)) {
				response = iotClient.getClient(AliRegionEnum.AMERICA.getValue()).getAcsResponse(request);
			} else {
				response = iotClient.getClient(AliRegionEnum.SOURTHCHINA.getValue()).getAcsResponse(request);
			}

		} catch (ClientException e) {

			e.printStackTrace();
		} finally {
			logger.info(" ====== sendRPCRequest end ====== ");
			return response;
		}
	}

	public void pubTopic(CMDEnum cmd, byte[] data, String deviceSerial) throws Exception {
		logger.info(" ====== pubTopic start ====== ");
		String mString = com.bright.apollo.tool.StringUtils.bytes2String(cmd, data, packageLength, head);
		// String mString = "";
		PubRequest request = new PubRequest();
		String productKey = AliDevCache.getProductKey(deviceSerial);
		String deviceName = AliDevCache.getDeviceName(deviceSerial);
		String region = AliDevCache.getProductRegion(deviceSerial);
		if (StringUtils.isEmpty(productKey)) {
			TAliDevice device = aliDeviceService.getAliDeviceBySerializeId(deviceSerial);
			region = setCache(device, deviceSerial);
		}
		request.setProductKey(productKey);
		request.setMessageContent(Base64.encodeBase64String(mString.getBytes()));
		request.setTopicFullName("/" + productKey + "/" + deviceName + "/get");
		request.setQos(0); // QoS0 设备在线时发送 ，QoS1 设备不在线时，能在IOT HUB 上保存7天，上线后发送
		PubResponse response = null;
		sendRequest(request, region, response);

		logger.info(" ====== pubTopic end ====== ");
	}

	public void pubTopicDev(JSONObject object, String productKey, String deviceName, AliRegionEnum eAliRegionEnum)
			throws Exception {
		logger.info(" ====== pubTopicDev start ====== ");
		PubRequest request = new PubRequest();
		request.setProductKey(productKey);
		logger.info(" TopicService.pubTopicDev(): " + object.toString());
		request.setMessageContent(Base64.encodeBase64String(object.toString().getBytes()));
		request.setTopicFullName("/" + productKey + "/" + deviceName + "/get");
		request.setQos(0); // QoS0 设备在线时发送 ，QoS1 设备不在线时，能在IOT HUB 上保存7天，上线后发送
		PubResponse response = null;
		sendRequest(request, eAliRegionEnum.getValue(), response);
		/*
		 * if (eAliRegionEnum.equals(AliRegionEnum.AMERICA)) { response =
		 * iotClient.getClient(AliRegionEnum.AMERICA.name()).getAcsResponse(
		 * request); }else { response =
		 * iotClient.getClient(AliRegionEnum.SOURTHCHINA.name()).getAcsResponse(
		 * request); }
		 */
		logger.info(" rep success topic: " + response.getSuccess());
		logger.info(" ====== pubTopicDev end ====== ");
	}

	public void pubTopic(CMDEnum cmd, byte[] data, String productKey, String deviceName, AliRegionEnum eAliRegionEnum)
			throws Exception {
		logger.info(" ====== pubTopic start ====== ");
		String mString = com.bright.apollo.tool.StringUtils.bytes2String(cmd, data, packageLength, head);
		logger.info(" pub mStr: " + mString);
		PubRequest request = new PubRequest();
		request.setProductKey(productKey);
		request.setMessageContent(Base64.encodeBase64String(mString.getBytes()));
		request.setTopicFullName("/" + productKey + "/" + deviceName + "/get");
		request.setQos(0); // QoS0 设备在线时发送 ，QoS1 设备不在线时，能在IOT HUB 上保存7天，上线后发送
		PubResponse response = null;
		/*
		 * if (eAliRegionEnum.equals(AliRegionEnum.AMERICA)) { response =
		 * iotClient.getClient(AliRegionEnum.AMERICA.name()).getAcsResponse(
		 * request); }else { response =
		 * iotClient.getClient(AliRegionEnum.SOURTHCHINA.name()).getAcsResponse(
		 * request); }
		 */
		sendRequest(request, eAliRegionEnum.getValue(), response);

	}

	public JSONObject requestDev(JSONObject object, String deviceSerial) throws Exception {
		logger.info(" ====== requestDev start ====== ");
		RRpcRequest rrpcRequest = new RRpcRequest();
		// 设备所属产品的Key
		String productKey = AliDevCache.getProductKey(deviceSerial);
		String deviceName = AliDevCache.getDeviceName(deviceSerial);
		String eAliRegionEnum = AliDevCache.getProductRegion(deviceSerial);
		if (StringUtils.isEmpty(productKey)) {
			TAliDevice device = aliDeviceService.getAliDeviceBySerializeId(deviceSerial);
			setCache(device, deviceSerial);

		}
		logger.info(" TopicService.requestDev() productkey: " + productKey + " devicename:" + deviceName + " object: "
				+ object.toString());
		rrpcRequest.setProductKey(productKey);
		rrpcRequest.setDeviceName(deviceName); // 设备名称
		rrpcRequest.setRequestBase64Byte(Base64.encodeBase64String(object.toString().getBytes())); // 发给设备的数据，要求二进制数据做一次Base64编码
		rrpcRequest.setTimeout(3500); // 超时时间，单位毫秒，如果超过这个时间设备没反应则返回"TIMEOUT"
		RRpcResponse rrpcResponse = null;
		sendRPCRequest(rrpcRequest, eAliRegionEnum, rrpcResponse);
		/*
		 * if (eAliRegionEnum.equals(AliRegionEnum.AMERICA)) { rrpcResponse =
		 * iotClient.getClient(AliRegionEnum.AMERICA.name()).getAcsResponse(
		 * rrpcRequest); }else { rrpcResponse =
		 * iotClient.getClient(AliRegionEnum.SOURTHCHINA.name()).getAcsResponse(
		 * rrpcRequest); }
		 */

		if (rrpcResponse.getSuccess()) {
			if (rrpcResponse.getRrpcCode().equals("SUCCESS")) {
				byte[] contentBytes = Base64.decodeBase64(rrpcResponse.getPayloadBase64Byte());
				String aString = new String(contentBytes, "utf-8");
				System.out.println("TopicService.requestDev() success resp:" + aString);
				logger.info(" ====== requestDev end ====== ");
				return new JSONObject(aString);
			} else {
				logger.info(" ====== requestDev end ====== ");
				return null;
			}
		} else {
			logger.info(" ====== requestDev end ====== ");
			return null;
		}
	}

	@Async
	public Future<OboxResp> request(CMDEnum cmd, byte[] data, String deviceSerial) throws Exception {
		logger.info(" ====== request start ====== ");
		String mString = com.bright.apollo.tool.StringUtils.bytes2String(cmd, data, packageLength, head);
		logger.info(" request mStr: " + mString);
		RRpcRequest rrpcRequest = new RRpcRequest();
		// 设备所属产品的Key
		String productKey = AliDevCache.getProductKey(deviceSerial);
		String deviceName = AliDevCache.getDeviceName(deviceSerial);
		String eAliRegionEnum = AliDevCache.getProductRegion(deviceSerial);
		if (StringUtils.isEmpty(productKey)) {
			TAliDevice device = aliDeviceService.getAliDeviceBySerializeId(deviceSerial);
			setCache(device, deviceSerial);
			if (device != null) {
				productKey = device.getProductKey();
				deviceName = device.getDeviceName();
			}
		}
		rrpcRequest.setProductKey(productKey);
		rrpcRequest.setDeviceName(deviceName); // 设备名称
		rrpcRequest.setRequestBase64Byte(Base64.encodeBase64String(mString.getBytes())); // 发给设备的数据，要求二进制数据做一次Base64编码
		rrpcRequest.setTimeout(5000); // 超时时间，单位毫秒，如果超过这个时间设备没反应则返回"TIMEOUT"
		RRpcResponse rrpcResponse = null;
		rrpcResponse = sendRPCRequest(rrpcRequest, eAliRegionEnum, rrpcResponse);
		/*
		 * if (eAliRegionEnum.equals(AliRegionEnum.AMERICA)) { rrpcResponse =
		 * iotClient.getClient(AliRegionEnum.AMERICA.name()).getAcsResponse(
		 * rrpcRequest); }else { rrpcResponse =
		 * iotClient.getClient(AliRegionEnum.SOURTHCHINA.name()).getAcsResponse(
		 * rrpcRequest); }
		 */
		OboxResp oboxResp = null;
		if (rrpcResponse.getSuccess()) {
			if (rrpcResponse.getRrpcCode().equals("SUCCESS")) {
				byte[] contentBytes = Base64.decodeBase64(rrpcResponse.getPayloadBase64Byte());
				final String aString = new String(contentBytes, "utf-8");
				cmdHandlerManager.processTopic(productKey, deviceName, aString);
				return new AsyncResult<OboxResp>(new OboxResp(OboxResp.Type.success, aString.substring(22)));
			} else if (rrpcResponse.getRrpcCode().equals("UNKNOW")) {
				return new AsyncResult<OboxResp>(new OboxResp(OboxResp.Type.socket_write_error));
			} else if (rrpcResponse.getRrpcCode().equals("TIMEOUT")) {
				return new AsyncResult<OboxResp>(new OboxResp(OboxResp.Type.socket_write_error));
			} else if (rrpcResponse.getRrpcCode().equals("OFFLINE")) {
				return new AsyncResult<OboxResp>(new OboxResp(OboxResp.Type.not_online));
			} else if (rrpcResponse.getRrpcCode().equals("HALFCONN")) {
				return new AsyncResult<OboxResp>(new OboxResp(OboxResp.Type.not_online));
			}
		} else {
			return new AsyncResult<OboxResp>(new OboxResp(OboxResp.Type.not_online));
		}
		// 得到的数据是设备返回二进制数据然后再经过Base64编码之后的字符串，需要转换一下才能拿到原始的二进制数据
		// 对应的响应码(UNKNOW/SUCCESS/TIMEOUT/OFFLINE/HALFCONN等)
		// 得到设备返回的数据信息
		logger.info(" ====== request end ====== ");
		return new AsyncResult<OboxResp>(new OboxResp(OboxResp.Type.socket_write_error));
	}

}