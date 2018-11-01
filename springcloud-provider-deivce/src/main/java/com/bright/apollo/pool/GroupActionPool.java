package com.bright.apollo.pool;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bright.apollo.common.entity.TGroupDevice;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TServerGroup;
import com.bright.apollo.common.entity.TUserOperation;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.service.GroupDeviceService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.ServerGroupService;
import com.bright.apollo.service.UserOperationService;
import com.bright.apollo.tool.ByteHelper;
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月1日  
 *@Version:1.1.0  
 */
@Component
public class GroupActionPool {
	private final Logger log = Logger.getLogger(GroupActionPool.class);
	@Autowired
	private ServerGroupService serverGroupService;
	@Autowired
	private OboxService oboxService;
	@Autowired
	private FeignAliClient feignAliClient;
	@Autowired
	private GroupDeviceService groupDeviceService;
	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;
	@Autowired
	private UserOperationService userOperationService;
	private final static String serverAddr = "020000ffff";
	private static ExecutorService executor;

	public GroupActionPool() {
		executor = Executors.newFixedThreadPool(5);
	}

	public void setGroup(String state,int groupId) {

		executor.submit(new groupAction(state, groupId));
	}

    class groupAction implements Runnable {

		private String state;

		private int groupId;

		public groupAction(String state, int groupId) {
			this.state = state;
			this.groupId = groupId;
		}

		@Override
		public void run() {
			try {
				log.info("===update server group===");
				TServerGroup tServerGroup = serverGroupService
						.querySererGroupById(groupId);
				if (tServerGroup.getGroupStyle().equals("00")) {
					
				} else {
					log.info("===update server group===");
					//TKey tKey = OboxBusiness.queryKeyByID(tServerGroup
					//		.getLicense());
					//if (tKey != null) {
						// update server group
						//log.info("===update server group=== 1674:"+tKey.toString());
						List<TObox> tOboxs = oboxService
								.queryOboxByGroupId(tServerGroup.getId());
						for (TObox tObox : tOboxs) {
							log.info("===update server group===");
							byte[] stateBytes = ByteHelper
									.hexStringToBytes(this.state);
							byte[] bodyBytes = new byte[7+stateBytes.length];
							byte[] oboxSerialIdBytes = ByteHelper
									.hexStringToBytes(serverAddr);
							System.arraycopy(oboxSerialIdBytes, 0, bodyBytes,
									0, oboxSerialIdBytes.length);
							byte[] addrBytes = ByteHelper
									.hexStringToBytes(tServerGroup
											.getGroupAddr());
							System.arraycopy(addrBytes, 0, bodyBytes, 5,
									addrBytes.length);
							System.arraycopy(stateBytes, 0, bodyBytes, 7,
									stateBytes.length);
							feignAliClient.sendCmd(tObox, CMDEnum.setting_node_status, bodyBytes);
							//CMDMessageService.send(tObox,
							//		CMDEnum.setting_node_status, bodyBytes, 0,
							//		0);
						}

						// update child at group
						List<TGroupDevice> tGroupDevices = groupDeviceService
								.queryDeviceGroupByGroupId(tServerGroup.getId());
						for (TGroupDevice tGroupDevice : tGroupDevices) {
							TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
									.queryDeviceConfigBySerialID(tGroupDevice.getDeviceSerialId());
							if (tOboxDeviceConfig != null) {
								TUserOperation tUserOperation = new TUserOperation();
								tUserOperation
										.setDeviceSerialId(tOboxDeviceConfig
												.getDeviceSerialId());
								tUserOperation.setDeviceState(state);
								tUserOperation
										.setDeviceChildType(tOboxDeviceConfig
												.getDeviceChildType());
								tUserOperation.setDeviceType(tOboxDeviceConfig
										.getDeviceType());
								//UserBusiness.addUserOperation(tUserOperation);
								userOperationService.addUserOperation(tUserOperation);
								if (!state.substring(0, 2).equals("ff")
										&& !state.substring(0, 2).equals("FF")) {
									if (!tOboxDeviceConfig.getDeviceState()
											.equals(state)) {
										tOboxDeviceConfig.setDeviceState(state);
										oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
										//OboxBusiness
										//		.updateOboxDeviceConfig(tOboxDeviceConfig);
									}
								}else {
								
									if (tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.led_rgb.getValue())) {
										tOboxDeviceConfig.setDeviceState("32"+state.substring(2, state.length()));
									}else {
										tOboxDeviceConfig.setDeviceState("c8"+state.substring(2, state.length()));
									}
									oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
									//OboxBusiness
									//		.updateOboxDeviceConfig(tOboxDeviceConfig);
								}

							}
						}
					//}
				}

			} catch (Exception e) {
				log.error("===error msg:"+e.getMessage());
			} 
		}
	}
}
