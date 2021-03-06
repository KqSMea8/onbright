package com.bright.apollo.handler;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TDeviceChannel;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;

@Component
public class SearchResultHandler extends BasicHandler {
	private static Logger logger = LoggerFactory.getLogger(SearchResultHandler.class);
	@Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        byte [] bodyBytes = ByteHelper.hexStringToBytes(msg.getData());

        byte suc = bodyBytes[0];
        if (suc != 1) {
            return null;
        }

      //  logger.info("SearchResultHandler.process():"+msg.getData());
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
            	logger.info("==queryDeviceConfigBySerialID is not null and serialId:"+dbConfig.getDeviceSerialId());
                TOboxDeviceConfig deviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getOboxId(), device_rf_addr);
                if (deviceConfig != null) {
                    if (!deviceConfig.getDeviceRfAddr().equals(dbConfig.getDeviceRfAddr())) {
//                        if (!deviceConfig.getGroupAddr().equals("00")) {
//                            DeviceBusiness.deleteOBOXGroupByAddr(deviceConfig.getOboxSerialId(), deviceConfig.getGroupAddr());
//                        }
                    	logger.info("===delete device serialId:"+deviceConfig.getDeviceSerialId());
                        userDeviceService.deleteUserDevice(deviceConfig.getDeviceSerialId());
//                        DeviceBusiness.deleteUserDeviceByDeviceId(deviceConfig.getOboxId());
                        deviceChannelService.deleteDeviceChannel(deviceConfig.getId());
//                        DeviceBusiness.delDeviceChannel(deviceConfig.getOboxId());
 //                       oboxDeviceConfigService.deleteTOboxDeviceConfig(deviceConfig.getId());
                        oboxDeviceConfigService.deleteTOboxDeviceConfigById(deviceConfig.getId());
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
                    	logger.info("===delete device serialId:"+deviceConfig.getDeviceSerialId());
                        userDeviceService.deleteUserDevice(deviceConfig.getDeviceSerialId());
//                        DeviceBusiness.deleteUserDeviceByDeviceId(deviceConfig.getOboxId());
                        deviceChannelService.deleteDeviceChannel(deviceConfig.getId());
//                        DeviceBusiness.delDeviceChannel(deviceConfig.getOboxId());
//                        oboxDeviceConfigService.deleteTOboxDeviceConfig(deviceConfig.getOboxId());
                        oboxDeviceConfigService.deleteTOboxDeviceConfigById(deviceConfig.getId());
//                        DeviceBusiness.deleteDeviceById(deviceConfig.getOboxId());
                    }
                }else{
//                    if (!dbConfig.getGroupAddr().equals("00")) {
//                        DeviceBusiness.deleteOBOXGroupByAddr(dbConfig.getOboxSerialId(), dbConfig.getGroupAddr());
//                    }
                	logger.info("===delete device serialId:"+dbConfig.getDeviceSerialId());
                    userDeviceService.deleteUserDevice(dbConfig.getDeviceSerialId());
//                        DeviceBusiness.deleteUserDeviceByDeviceId(deviceConfig.getOboxId());
                    deviceChannelService.deleteDeviceChannel(dbConfig.getId());
//                        DeviceBusiness.delDeviceChannel(deviceConfig.getOboxId());
                    oboxDeviceConfigService.deleteTOboxDeviceConfigById(dbConfig.getId());
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
                deviceConfig.setOboxId(obox.getOboxId());
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
                deviceConfig.setDeviceVersion("0000000000000000");
                deviceConfig.setGroupAddr("00");
//                Integer returnIndex = OboxBusiness.addOboxConfig(deviceConfig);
                Integer returnIndex =  oboxDeviceConfigService.addTOboxDeviceConfig(deviceConfig);
                final TOboxDeviceConfig tempDeviceConfig = deviceConfig;
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (tempDeviceConfig.getDeviceType().equals(DeviceTypeEnum.doorlock.getValue()) && (tempDeviceConfig
								.getDeviceChildType().equals(DeviceTypeEnum.capacity_finger.getValue()))) {
							try {
								List<TIntelligentFingerPush> list = intelligentFingerService
										.queryTIntelligentFingerPushsBySerialId(tempDeviceConfig.getDeviceSerialId());
								if (list!=null&&list.size()<=0) {
									List<TIntelligentFingerPush> initPush = initPush(tempDeviceConfig.getDeviceSerialId());
									intelligentFingerService.batchTIntelligentFingerPush(initPush
											);
								}
							} catch (Exception e) {
								logger.error("===error msg:"+e.getMessage());
							}
						}
					}
				}).start();
//                List<TUserObox> tUserOboxs = OboxBusiness.queryUserOboxsByOboxId(obox.getOboxId());
                List<TUserObox> tUserOboxs = userOboxService.getUserOboxBySerialId(obox.getOboxSerialId());
                 if (!tUserOboxs.isEmpty()) {
                    for (TUserObox tUserObox : tUserOboxs) {
                    	TUserDevice userDevice=userDeviceService.getUserDeviceByUserIdAndSerialId(tUserObox.getUserId(),device_serial_id);
                    	//TUserDevice userDevice=new TUserDevice();
                    	if(userDevice==null){
                    		userDevice=new TUserDevice();
                    		userDevice.setUserId(tUserObox.getUserId());
                        	userDevice.setDeviceSerialId(device_serial_id);
                        	userDeviceService.addUserDevice(userDevice);
                    	}
                    	//DeviceBusiness.addUserDevice(tUserObox.getUserId(), returnIndex);
                    }
                }
//	
                returnIndex=(deviceConfig.getId()!=null&&deviceConfig.getId()!=0)?deviceConfig.getId():returnIndex;
                TDeviceChannel tDeviceChannel =deviceChannelService.getDeviceChannelById(returnIndex,obox.getOboxId());
                if(tDeviceChannel==null){
                	tDeviceChannel.setDeviceId(returnIndex);
                	tDeviceChannel.setOboxId(obox.getOboxId());
                	tDeviceChannel.setSignalIntensity(15);
                	deviceChannelService.addDeviceChannel(tDeviceChannel);
                }else{
                	tDeviceChannel.setSignalIntensity(15);
                	deviceChannelService.updateDeviceChannel(tDeviceChannel);
                }
//                DeviceBusiness.addDeviceChannel(tDeviceChannel);

            }
        } catch (Exception e) {
        	logger.error("===SearchResultHandler error msg:"+e.getMessage());
        }
        return null;
    }
	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		//0181024c616d70330000000000000000000000e9660100004494000000000da000000000000000000000000000000000000000000000
		//0181024c616d70370000000000000000000000f26601000044940000000011a000000000000000000000000000000000000000000000
		//0181024c616d70370000000000000000000000f26601000044940000000011a000000000000000000000000000000000000000000000
		//0181024c616d70350000000000000000000000eb660100004494000000000fa000000000000000000000000000000000000000000000
		//0181024c616d70320000000000000000000000a75a010000939b0000000002a000000000000000000000000000000000000000000000
		//01000500001e000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
		byte [] bodyBytes = ByteHelper.hexStringToBytes("0181024c616d70310000000000000000000000af5a0100002394000000000aa000000000000000000000000000000000000000000000");

        byte suc = bodyBytes[0];
        
     //   logger.info("SearchResultHandler.process():"+msg.getData());
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
        
        
        System.out.println("ID:"+ID+"  device_type:"+device_type+" device_child_type:"+device_child_type
        		+"  device_serial_id:"+device_serial_id+"  obox_serial_id:"+obox_serial_id
        		+" device_rf_addr:"+device_rf_addr
        		);
	}
	
}
