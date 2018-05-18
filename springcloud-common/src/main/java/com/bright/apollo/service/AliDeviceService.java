package com.bright.apollo.service;

import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import org.springframework.stereotype.Service;


public interface AliDeviceService {

    TAliDevice getAliDeviceBySerializeId(String oboxSerialId);

    TAliDeviceUS getAliUSDeviceBySerializeId(String oboxSerialId);

    TAliDevTimer getAliDevTimerByTimerId(int id);

    void deleteAliDevTimerBySerializeIdAndType(String deviceSerialId,int isCountdown);

    TAliDevice getAliDeviceByProductKeyAndDeviceName(String productKey, String deviceName);

    TAliDeviceUS getAliUSDeviceByProductKeyAndDeviceName(String productKey, String deviceName);

    void updateAliDevice(TAliDevice aliDevice);

    void updateAliUSDevice(TAliDeviceUS aliDevice);
}
