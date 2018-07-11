package com.bright.apollo.handler;

import java.util.List;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.session.ClientSession;

public class NodeStatusCMDHandler extends BasicHandler{

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String isSuccess = data.substring(0, 2);

        if ("01".equals(isSuccess)) {
            String physicalOBOXSerialId = data.substring(2, 12);
            String group_rf_addr = data.substring(12, 14);
            String rf_addr = data.substring(14, 16);
            String state = data.substring(16, 30);


            TObox physicalOBOX = oboxService.queryOboxsByOboxSerialId(physicalOBOXSerialId);
//            if (physicalOBOX == null) {
//                TKey tKey = OboxBusiness.queryKeyByAddr(physicalOBOXSerialId);
//                if (tKey != null) {
//                    return null;
//                } else {
//                    return null;
//                }
//            }

            if (!"00".equals(rf_addr) && !"ff".equals(rf_addr)) {

                TOboxDeviceConfig tOboxDeviceConfig
                        = oboxDeviceConfigService.queryOboxConfigByRFAddr(physicalOBOX.getOboxId(),rf_addr);
//                if (tOboxDeviceConfig != null) {
//
//                    TUserOperation tUserOperation = new TUserOperation();
//                    tUserOperation.setDeviceSerialId(tOboxDeviceConfig
//                            .getDeviceSerialId());
//                    tUserOperation.setDeviceState(state);
//                    tUserOperation.setDeviceChildType(tOboxDeviceConfig
//                            .getDeviceChildType());
//                    tUserOperation.setDeviceType(tOboxDeviceConfig
//                            .getDeviceType());
//                    UserBusiness.addUserOperation(tUserOperation);
//
//                    updateDeviceState(tOboxDeviceConfig, state);
//					/*tOboxDeviceConfig.setDeviceState(state);
//					//TODO 6合1
//					OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);*/
//                }
            } else if ("ff".equals(rf_addr)) {
//				if (physicalOBOX.getOboxVersion().substring(8, 12)
//						.equals("0b01")) {
//					// egelerise
//					for (int i = 1; i < 5; i++) {
//						TOboxDeviceConfig tOboxDeviceConfig = OboxBusiness
//								.queryOboxConfigByAddr(
//										physicalOBOX.getOboxId(),
//										ByteHelper.int2HexString(i));
//						if (tOboxDeviceConfig != null) {
//							if (tOboxDeviceConfig.getDeviceState() == null) {
//								updateDeviceState(tOboxDeviceConfig, state);
//								/*tOboxDeviceConfig.setDeviceState(state);
//								OboxBusiness
//										.updateOboxDeviceConfig(tOboxDeviceConfig);*/
//							} else {
//								if (!tOboxDeviceConfig.getDeviceState().equals(
//										state)) {
//									updateDeviceState(tOboxDeviceConfig, state);
//									/*tOboxDeviceConfig.setDeviceState(state);
//									OboxBusiness
//											.updateOboxDeviceConfig(tOboxDeviceConfig);*/
//								}
//							}
//						}
//					}
//				}
            } else if (!"00".equals(group_rf_addr)
                    && !"ff".equals(group_rf_addr)) {
                // group udpate

                List<TOboxDeviceConfig> tOboxDeviceConfigs
                        = oboxDeviceConfigService.getTOboxDeviceConfigByOboxSerialIdAndGroupAddress(physicalOBOXSerialId,group_rf_addr);
                if (!tOboxDeviceConfigs.isEmpty()) {
                    for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
                        oboxDeviceConfigService.updateTOboxDeviceConfigStatus(tOboxDeviceConfig, state);
//                        updateDeviceState(tOboxDeviceConfig, state);
//                        TUserOperation tUserOperation = new TUserOperation();
//                        tUserOperation.setDeviceSerialId(tOboxDeviceConfig
//                                .getDeviceSerialId());
//                        tUserOperation.setDeviceState(state);
//                        tUserOperation.setDeviceChildType(tOboxDeviceConfig
//                                .getDeviceChildType());
//                        tUserOperation.setDeviceType(tOboxDeviceConfig
//                                .getDeviceType());
//                        UserBusiness.addUserOperation(tUserOperation);

                    }
                }
//                TServerOboxGroup tServerOboxGroup = DeviceBusiness
//                        .queryOBOXGroupByAddr(physicalOBOXSerialId,
//                                group_rf_addr);
//                if (tServerOboxGroup != null) {
//                    TServerGroup tServerGroup = DeviceBusiness
//                            .querySererGroupById(tServerOboxGroup.getServerId());
//                    if (tServerGroup != null) {
//                        if (tServerGroup.getGroupState() == null) {
//                            tServerGroup.setGroupState(state);
//                            DeviceBusiness.updateServerGroupName(tServerGroup);
//                        } else {
//                            if (!tServerGroup.getGroupState().equals(state)) {
//                                tServerGroup.setGroupState(state);
//                                DeviceBusiness
//                                        .updateServerGroupName(tServerGroup);
//                            }
//                        }
//
//                    }
//                }
            }
        }
        return null;
    }
}
