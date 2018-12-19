package com.bright.apollo.service;

import com.bright.apollo.common.entity.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public interface YaoKongYunService {

    List<TYaoKongYunBrand> getYaoKongYunByTId(Integer tId);

    TYaoKongYunBrand getYaoKongYunByTIdAndDeviceType(String tId,String deviceType);

    TYaokonyunDevice getYaoKongYunDevice();

    void updateYaoKongDevice(String deviceId);

    void addYaoKongDevice(TYaokonyunDevice yaokonyunDevice);

    List<TYaokonyunKeyCode> getYaoKongKeyCodeByIndex(Integer index);

    List<TYaokonyunDeviceType> getYaoKongYunDeviceType();

    void addTYaokonyunDeviceType(TYaokonyunDeviceType yaokonyunDeviceType);

    List<TYaokonyunRemoteControl> getYaokonyunRemoteControlByIds();

    void addYaokonyunRemoteControl(TYaokonyunRemoteControl yaokonyunRemoteControl);

    List<TYaoKongYunBrand> getYaoKongYunBrandByDeviceType(String deviceTypeId);

    void addTYaoKongYunBrand(TYaoKongYunBrand yaoKongYunBrand);

    TYaokonyunRemoteControl getYaokonyunRemoteControlByRemoteId(String remoteId);

    void addTYaokonyunKeyCode(TYaokonyunKeyCode yaokonyunKeyCode);

    List<TYaokonyunKeyCode> getYaoKongKeyCodeByRemoteId(Integer index);

    void deleteTYaokonyunKeyCode(String serialId,String index);

    void deleteTYaokonyunKeyCodeByKeyName(String serialId,String index,String keyName,String keyType);

    void deleteTYaokonyunKeyCodeBySerialId(String serialId);

    List<TYaokonyunKeyCode> getYaoKongKeyCodeBySerialId(String serialId);

    void updateYaoKongKeyCodeNameBySerialIdAndIndex(String serialId,String index,String name);

    TYaokonyunKeyCode getYaoKongKeyCodeByKeyAndSerialIdAndIndex(Integer index,String serialId,String key);

    void updateYaoKongKeyCodeNameBySerialIdAndIndexAndKey(String serialId,String index,String key,String codeSrc);

    List<Map<String,Object>> getUserIRDevice(Integer userId);

    List<TYaokonyunKeyCode> getIRDeviceByIndex(Integer index);

    TYaokonyunKeyCode getIRDeviceByIndexAndKey(Integer index,String key);

    List<TYaokonyunKeyCode> getYaoKongKeyCodeBySerialIdAndIndex(String serialId,Integer index);
}
