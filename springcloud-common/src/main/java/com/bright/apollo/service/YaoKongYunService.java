package com.bright.apollo.service;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TYaoKongYunBrand;
import com.bright.apollo.common.entity.TYaokonyunDevice;

import java.util.List;

public interface YaoKongYunService {

    List<TYaoKongYunBrand> getYaoKongYunByTId(Integer tId);

    List<TYaoKongYunBrand> getYaoKongYunByTIdAndDeviceType(String tId,String deviceType);

    TYaokonyunDevice getYaoKongYunDevice();

    void updateYaoKongDevice(String deviceId);

    void addYaoKongDevice(TYaokonyunDevice yaokonyunDevice);

}
