package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TUserAliDev;


public interface AliDeviceService {

    TAliDevice getAliDeviceBySerializeId(String oboxSerialId);
    
    int addAliDevUS(TAliDeviceUS tAliDeviceUS);

    TAliDeviceUS getAliUSDeviceBySerializeId(String oboxSerialId);

    TAliDevTimer getAliDevTimerByTimerId(int id);

    void deleteAliDevTimerBySerializeIdAndType(String deviceSerialId,int isCountdown);

    TAliDevice getAliDeviceByProductKeyAndDeviceName(String productKey, String deviceName);

    TAliDeviceUS getAliUSDeviceByProductKeyAndDeviceName(String productKey, String deviceName);
    
    List<TAliDeviceUS> getAliUSDeviceByProductKeyAndDeviceSerialId(String productKey, String deviceSerialId);

    List<TAliDevice> getAliDeviceByProductKeyAndDeviceSerialId(String productKey, String oboxSerialId);
    
    void updateAliDevice(TAliDevice aliDevice);

    void updateAliUSDevice(TAliDeviceUS aliDevice);

	/**  
	 * @param tAliDevice  
	 * @Description:  
	 */
	int addAliDev(TAliDevice tAliDevice);

    List<TAliDevice> getAliDeviceByProductKey(String productKey);

    List<TAliDevTimer> getAliDevTimerByDeviceSerialId(String oboxSerialId);

    TAliDevTimer getAliDevTimerByDeviceSerialIdAndCountDown(String oboxSerialId);

    TAliDevTimer getAliDevTimerByIdAndDeviceId(String oboxSerialId,Integer id);

    TAliDevTimer getAliDevTimerById(int id);

    void deleteAliDevTimerById(Integer id);

    int addAliDevTimer(TAliDevTimer aliDevTimer);

    void updateAliDevTimer(TAliDevTimer aliDevTimer);

    void deleteAliDeviceUser(String oboxSerialId);

    int addAliDevUser(TUserAliDev userAliDev);


}
