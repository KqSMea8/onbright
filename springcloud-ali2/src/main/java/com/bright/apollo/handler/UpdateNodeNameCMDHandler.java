package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;
import org.springframework.stereotype.Component;

@Component
public class UpdateNodeNameCMDHandler extends BasicHandler{

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String isSuccess = data.substring(0, 2);

        if ("01".equals(isSuccess)) {
            String set = data.substring(2, 4);
            String oboxSerialId = data.substring(4, 14);
            String groupAddr = data.substring(14, 16);
            String nodeAddr = data.substring(16, 18);


            TObox obox =  oboxService.queryOboxsByOboxSerialId(oboxSerialId);
            if (obox == null) {
                return null;
            }
            if ("00".equals(set)) {
                //delete
                if ("00".equals(groupAddr)) {
                    //node

                    TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getOboxId(), nodeAddr);
                    if (tOboxDeviceConfig == null) {
                        return null;
                    }
                    userDeviceService.deleteUserDevice(tOboxDeviceConfig.getOboxSerialId());
//                    DeviceBusiness.deleteUserDeviceByDeviceId(tOboxDeviceConfig.getOboxId());
                    oboxDeviceConfigService.deleteTOboxDeviceConfigByOboxIdAndNodeAddress(obox.getOboxId(),nodeAddr);
//                    OboxBusiness.deleteOBOXDeviceConfig(obox.getOboxId(), nodeAddr);
//                    IrCfgBusiness.delTIrCfgBySerialId(tOboxDeviceConfig.getDeviceSerialId());
                }else{
                    //group
//                    TServerOboxGroup tServerOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(oboxSerialId, groupAddr);
//                    if (tServerOboxGroup != null) {
//                        List<TOboxDeviceConfig> tOboxDeviceConfigs = DeviceBusiness.queryOBOXGroupMemberByAddr(tServerOboxGroup.getOboxSerialId(), tServerOboxGroup.getGroupAddr());
//                        for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
//                            tOboxDeviceConfig.setGroupAddr("00");
//                            OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
//                        }
//                        TServerGroup tServerGroup = DeviceBusiness.querySererGroupById(tServerOboxGroup.getServerId());
//                        if (tServerGroup != null) {
//                            DeviceBusiness.deleteServerGroup(tServerGroup);
//
//                        }
//                        DeviceBusiness.deleteOBOXGroup(tServerOboxGroup);
//                    }
                }
            }else if ("01".equals(set)) {
                //add group
//                if (!"00".equals(groupAddr)) {
//                    String name = ByteHelper.fromHexAscii(data.substring(18, 50));
//                    TServerOboxGroup dbOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(oboxSerialId, groupAddr);
//                    if (dbOboxGroup != null) {
//                        dbOboxGroup.setGroupName(name);
//                        DeviceBusiness.updateOBOXGroupName(dbOboxGroup);
//                        TServerGroup tServerGroup = DeviceBusiness.querySererGroupById(dbOboxGroup.getServerId());
//                        if (tServerGroup != null) {
//                            tServerGroup.setGroupName(name);
//                            DeviceBusiness.updateServerGroupName(tServerGroup);
//                        }
//                    }else {
//                        TServerOboxGroup tServerOboxGroup = new TServerOboxGroup();
//                        tServerOboxGroup.setGroupAddr(groupAddr);
//                        tServerOboxGroup.setGroupName(name);
//                        tServerOboxGroup.setOboxSerialId(oboxSerialId);
//
//                        TServerGroup tServerGroup = new TServerGroup();
//                        tServerGroup.setGroupName(name);
//                        tServerGroup.setGroupStyle("00");
//                        if (obox.getLicense() != null) {
//                            tServerGroup.setLicense(obox.getLicense());
//                        }
//                        tServerOboxGroup.setServerId(DeviceBusiness.addServerGroup(tServerGroup));
//
//                        DeviceBusiness.addOBOXGroup(tServerOboxGroup);
//
//                        List<TUserObox> tUserOboxs = UserBusiness.queryUserOboxByOBOXId(obox.getOboxId());
//                        for (TUserObox tUserObox : tUserOboxs) {
//                            TUserGroup tUserGroup = new TUserGroup();
//                            tUserGroup.setGroupId(tServerOboxGroup.getServerId());
//                            tUserGroup.setUserId(tUserObox.getUserId());
//                            UserBusiness.addUserGroup(tUserGroup);
//                        }
//                    }
//                }

            }else if ("02".equals(set)) {
                //rename
                String name = ByteHelper.fromHexAscii(data.substring(18, 50));
                if ("00".equals(groupAddr)) {
                    //node

                    TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getOboxId(),nodeAddr);
                    if (tOboxDeviceConfig == null) {
                        return null;
                    }
                    tOboxDeviceConfig.setDeviceId(name);
                    oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
//                    OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
                }else {
                    //group
//                    TServerOboxGroup tServerOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(oboxSerialId, groupAddr);
//                    if (tServerOboxGroup != null) {
//                        tServerOboxGroup.setGroupName(name);
//                        DeviceBusiness.updateOBOXGroupName(tServerOboxGroup);
//                    }
                }
            }
        }
        return null;
    }
}
