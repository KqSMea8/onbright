package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TDeviceChannel;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.service.*;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchResultHandler extends BasicHandler {

    private Logger log = Logger.getLogger(ControlPWCMDHandler.class);

    @Autowired
    private OboxService oboxService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private DeviceChannelService deviceChannelService;

    @Autowired
    private UserOboxService userOboxService;

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        byte [] bodyBytes = ByteHelper.hexStringToBytes(msg.getData());

        byte suc = bodyBytes[0];
        if (suc != 1) {
            return null;
        }

        System.out.println("SearchResultHandler.process():"+msg.getData());
        byte [] idBytes = new byte [16];
        System.arraycopy(bodyBytes, 3, idBytes, 0, idBytes.length);
        String ID = ByteHelper.bytesToHexString(idBytes);
        ID = ByteHelper.fromHexAscii(ID);

        byte [] deviceTypeBytes = { bodyBytes[1] };
        byte device_type_int = (byte)(deviceTypeBytes [0] & 0x1f);
        deviceTypeBytes [0] = device_type_int;
        String device_type = ByteHelper.bytesToHexString(deviceTypeBytes);

        byte [] deviceChildTypeBytes = { bodyBytes[2] };
        String device_child_type = ByteHelper.bytesToHexString(deviceChildTypeBytes);

        byte [] serialBytes = new byte [5];
        System.arraycopy(bodyBytes, 19, serialBytes, 0, serialBytes.length);
        String device_serial_id = ByteHelper.bytesToHexString(serialBytes);

        byte [] oboxSrialBytes = new byte[5];
        System.arraycopy(bodyBytes, 24, oboxSrialBytes, 0, oboxSrialBytes.length);
        String obox_serial_id = ByteHelper.bytesToHexString(oboxSrialBytes);

        byte [] addrBytes = { bodyBytes[30] };
        String device_rf_addr = ByteHelper.bytesToHexString(addrBytes);
        if ("00".equals(device_rf_addr)) {
            return null;
        }

        TObox obox = null;
        try {
            obox = oboxService.queryOboxsByOboxSerialId(obox_serial_id);
            if (obox == null) {
                return null;
            }

            TOboxDeviceConfig dbConfig = oboxDeviceConfigService.queryDeviceConfigBySerialID(device_serial_id);
            if (dbConfig != null && obox.getOboxSerialId().equals(dbConfig.getOboxSerialId())) {

                TOboxDeviceConfig deviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getOboxId(), device_rf_addr);
                if (deviceConfig != null) {
                    if (!deviceConfig.getDeviceRfAddr().equals(dbConfig.getDeviceRfAddr())) {
//                        if (!deviceConfig.getGroupAddr().equals("00")) {
//                            DeviceBusiness.deleteOBOXGroupByAddr(deviceConfig.getOboxSerialId(), deviceConfig.getGroupAddr());
//                        }
                        userDeviceService.deleteUserDevice(deviceConfig.getOboxSerialId());
//                        DeviceBusiness.deleteUserDeviceByDeviceId(deviceConfig.getOboxId());
                        deviceChannelService.deleteDeviceChannel(deviceConfig.getOboxId());
//                        DeviceBusiness.delDeviceChannel(deviceConfig.getOboxId());
                        oboxDeviceConfigService.deleteTOboxDeviceConfig(deviceConfig.getOboxId());
//                        DeviceBusiness.deleteDeviceById(deviceConfig.getOboxId());
                    }
                }
                dbConfig.setOboxId(obox.getOboxId());
//                dbConfig.setLicense(obox.getLicense());
                dbConfig.setDeviceRfAddr(device_rf_addr);
                dbConfig.setDeviceId(ID);
                dbConfig.setDeviceType(device_type);
                dbConfig.setDeviceChildType(device_child_type);
                oboxDeviceConfigService.updateTOboxDeviceConfig(dbConfig);

            }else {
                if (dbConfig == null) {
                    TOboxDeviceConfig deviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getOboxId(), device_rf_addr);
                    if (deviceConfig != null) {
//                        if (!deviceConfig.getGroupAddr().equals("00")) {
//                            DeviceBusiness.deleteOBOXGroupByAddr(deviceConfig.getOboxSerialId(), deviceConfig.getGroupAddr());
//                        }
                        userDeviceService.deleteUserDevice(deviceConfig.getOboxSerialId());
//                        DeviceBusiness.deleteUserDeviceByDeviceId(deviceConfig.getOboxId());
                        deviceChannelService.deleteDeviceChannel(deviceConfig.getOboxId());
//                        DeviceBusiness.delDeviceChannel(deviceConfig.getOboxId());
                        oboxDeviceConfigService.deleteTOboxDeviceConfig(deviceConfig.getOboxId());
//                        DeviceBusiness.deleteDeviceById(deviceConfig.getOboxId());
                    }
                }else{
//                    if (!dbConfig.getGroupAddr().equals("00")) {
//                        DeviceBusiness.deleteOBOXGroupByAddr(dbConfig.getOboxSerialId(), dbConfig.getGroupAddr());
//                    }
                    userDeviceService.deleteUserDevice(dbConfig.getOboxSerialId());
//                        DeviceBusiness.deleteUserDeviceByDeviceId(deviceConfig.getOboxId());
                    deviceChannelService.deleteDeviceChannel(dbConfig.getOboxId());
//                        DeviceBusiness.delDeviceChannel(deviceConfig.getOboxId());
                    oboxDeviceConfigService.deleteTOboxDeviceConfig(dbConfig.getOboxId());
//                        DeviceBusiness.deleteDeviceById(deviceConfig.getOboxId());
                }


                TOboxDeviceConfig deviceConfig = new TOboxDeviceConfig();
//                deviceConfig.setOboxId(obox.getOboxId());
//                deviceConfig.setLicense(obox.getLicense());
                deviceConfig.setDeviceRfAddr(device_rf_addr);
                deviceConfig.setDeviceSerialId(device_serial_id);
                deviceConfig.setDeviceId(ID);
                deviceConfig.setDeviceType(device_type);
                deviceConfig.setDeviceChildType(device_child_type);
                deviceConfig.setDeviceState("aaaaaadddddd00");

                if (deviceConfig.getDeviceType().equals("0b") ) {
                    if (deviceConfig.getDeviceChildType().equals("03")) {
                        //rader
                        deviceConfig.setDeviceState("ff000000000000");
                    }else if (deviceConfig.getDeviceChildType().equals("02")) {
                        //leap
                        deviceConfig.setDeviceState("ff000000000000");
                    }else if (deviceConfig.getDeviceChildType().equals("0a")) {
                        //light
                        deviceConfig.setDeviceState("ff010000000000");
                    }else if (deviceConfig.getDeviceChildType().equals("0b")) {
                        //temp & humidity
                        deviceConfig.setDeviceState("ff46ff32000000");
                    }else if (deviceConfig.getDeviceChildType().equals("0c")) {
                        //smoke
                        deviceConfig.setDeviceState("ff000000000000");
                    }
                }

                deviceConfig.setOboxSerialId(obox_serial_id);
//                Integer returnIndex = OboxBusiness.addOboxConfig(deviceConfig);
                Integer returnIndex =  oboxDeviceConfigService.addTOboxDeviceConfig(deviceConfig);
//
//                List<TUserObox> tUserOboxs = OboxBusiness.queryUserOboxsByOboxId(obox.getOboxId());
                List<TUserObox> tUserOboxs = userOboxService.getUserOboxBySerialId(obox.getOboxSerialId());
                TUserObox userObox = null;
                if (!tUserOboxs.isEmpty()) {
                    for (TUserObox tUserObox : tUserOboxs) {
                        userObox = new TUserObox();
                        userObox.setOboxSerialId(obox.getOboxSerialId());
                        userObox.setUserId(tUserObox.getUserId());
                        userOboxService.addUserObox(userObox);
//                        DeviceBusiness.addUserDevice(tUserObox.getUserId(), returnIndex);
                    }
                }
//
                TDeviceChannel tDeviceChannel = new TDeviceChannel();
                tDeviceChannel.setDeviceId(returnIndex);
                tDeviceChannel.setOboxId(obox.getOboxId());
                tDeviceChannel.setSignalIntensity(15);
                deviceChannelService.addDeviceChannel(tDeviceChannel);
//                DeviceBusiness.addDeviceChannel(tDeviceChannel);

            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
