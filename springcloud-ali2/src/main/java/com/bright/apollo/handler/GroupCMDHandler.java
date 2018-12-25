package com.bright.apollo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TGroupDevice;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TServerGroup;
import com.bright.apollo.session.ClientSession;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年11月1日
 * @Version:1.1.0
 */
@Component
public class GroupCMDHandler extends BasicHandler {
	private static final Logger logger = LoggerFactory.getLogger(GroupCMDHandler.class);

	/*
	 * (non=Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.handler.BasicHandler#process(com.bright.apollo.session.
	 * ClientSession, com.bright.apollo.bean.Message)
	 */
	@Override
	public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
		// 014794000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
		logger.info("=======GroupCMDHandler=======msg:" + msg.toString());
		String data = msg.getData();
		String isSuccess = data.substring(0, 2);
		if ("01".equals(isSuccess)) {
			String set = data.substring(16, 18);
			String oboxSerialId = data.substring(2, 12);
			String groupAddr = data.substring(12, 14);
			String nodeAddr = data.substring(14, 16);
			logger.info("===set:" + set + " ===oboxSerialId:" + oboxSerialId + " ===groupAddr:" + groupAddr
					+ " ===nodeAddr:" + nodeAddr);
			TObox obox = oboxService.queryOboxsByOboxSerialId(oboxSerialId);
			if (obox == null) {
				logger.error(String.format("not found %s obox!", clientSession.getUid()));
				return null;
			}

			logger.info("===obox:" + new JSONObject().toJSONString(obox));
			TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getOboxId(),
					nodeAddr);
			if (tOboxDeviceConfig == null) {
				return null;
			}

			logger.info("===tOboxDeviceConfig:" + new JSONObject().toJSONString(tOboxDeviceConfig));
			if (set.equals("00")) {
				if (!tOboxDeviceConfig.getGroupAddr().equals("00")
						&& tOboxDeviceConfig.getGroupAddr().equals(groupAddr)) {
					// before
					updateGroupType(oboxSerialId, tOboxDeviceConfig.getGroupAddr());
				}
				tOboxDeviceConfig.setGroupAddr(groupAddr);
				oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
				if (!tOboxDeviceConfig.getGroupAddr().equals("00")) {
					// now
					updateGroupType(oboxSerialId, tOboxDeviceConfig.getGroupAddr());
				}

			} else if (set.equals("02")) {
				// add server group
				String serverAddr = data.substring(18, 28);
				String addr = data.substring(28, 32);
				logger.info("===serverAddr:" + serverAddr + " ===addr:" + addr);
				// TKey tKey = OboxBusiness.queryKeyByAddr(serverAddr);
				// if (tKey != null) {
				// logger.info("===tKey:" + new
				// JSONObject().toJSONString(tKey));
				// String serveGroupId = RedisCache.getJedisCache().get(
				// obox.getOboxId().intValue() + ":"
				// + tOboxDeviceConfig.getDeviceSerialId());
				String serveGroupId = cmdCache
						.getValue(obox.getOboxId().intValue() + ":" + tOboxDeviceConfig.getDeviceSerialId());
				TServerGroup tServerGroup = null;
				if (!StringUtils.isEmpty(serveGroupId)) {
					logger.info("====serveGroupId:" + serveGroupId);
					cmdCache.deleteKey(obox.getOboxId().intValue() + ":" + tOboxDeviceConfig.getDeviceSerialId());
					// RedisCache.getJedisCache().expire(obox.getOboxId().intValue()
					// + ":"
					// + tOboxDeviceConfig.getDeviceSerialId(), =1);
					tServerGroup = serverGroupService.querySererGroupById(Integer.valueOf(serveGroupId));
				} else {
					return null;
					// tServerGroup = DeviceBusiness
					// .queryServerGroupByLicenseAndAddr(addr,
					// tKey.getId());
				}
				if (tServerGroup != null) {
					logger.info("===tServerGroup:" + new JSONObject().toJSONString(tServerGroup));
					TGroupDevice tGroupDevice = groupDeviceService.queryDeviceGroup(tServerGroup.getId(),
							tOboxDeviceConfig.getDeviceSerialId());
					if (tGroupDevice == null) {
						logger.info("===tGroupDevice:" + new JSONObject().toJSONString(tGroupDevice));
						TGroupDevice groupDevice = new TGroupDevice();
						groupDevice.setDeviceSerialId(tOboxDeviceConfig.getDeviceSerialId());
						groupDevice.setGroupId(tServerGroup.getId());
						groupDeviceService.addDeviceGroup(groupDevice);
						// DeviceBusiness.addDeviceGroup(groupDevice);
					}

				}
				// }
			} else if (set.equals("01")) {
				// delete group
				String serverAddr = data.substring(18, 28);
				String addr = data.substring(28, 32);
				// TKey tKey = OboxBusiness.queryKeyByAddr(serverAddr);
				// if (tKey != null) {
				// TServerGroup tServerGroup = DeviceBusiness
				// .queryServerGroupByLicenseAndAddr(addr,
				// tKey.getId());
				// if (tServerGroup != null) {
				// groupDeviceService.deleteDeviceGroup(tServerGroup.getId(),
				// tOboxDeviceConfig.getDeviceSerialId());
				// }
				// }
			}

		}
		return null;
	}

	/**
	 * @param oboxSerialId
	 * @param groupAddr
	 * @Description:
	 */
	private void updateGroupType(String oboxSerialId, String groupAddr) {

	}

	public static void main(String[] args) {
		String data = "014794000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		String isSuccess = data.substring(0, 2);
		String set = data.substring(16, 18);
		String oboxSerialId = data.substring(2, 12);
		String groupAddr = data.substring(12, 14);
		String nodeAddr = data.substring(14, 16);
		String serverAddr = data.substring(18, 28);
		String addr = data.substring(28, 32);
		System.out.println("isSuccess:"+isSuccess);
		System.out.println("set:"+set);
		System.out.println("oboxSerialId:"+oboxSerialId);
		System.out.println("groupAddr:"+groupAddr);
		System.out.println("nodeAddr:"+nodeAddr);
		System.out.println("serverAddr:"+serverAddr);
		System.out.println("addr:"+addr);
	}
}
