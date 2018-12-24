package com.bright.apollo.service.impl;


import com.bright.apollo.common.entity.*;
import com.bright.apollo.dao.device.mapper.YaoKongYunMapper;
import com.bright.apollo.service.YaoKongYunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class YaoKongYunServiceImpl implements YaoKongYunService {

    @Autowired
    private YaoKongYunMapper yaoKongYunMapper;

    @Override
    public List<TYaoKongYunBrand> getYaoKongYunByTId(Integer tId) {
        return yaoKongYunMapper.getYaoKongYunByTId(tId);
    }

    @Override
    public TYaoKongYunBrand getYaoKongYunByTIdAndDeviceType(String tId, String deviceType) {
        return yaoKongYunMapper.getYaoKongYunByTIdAndDeviceType(tId,deviceType);
    }

    @Override
    public TYaokonyunDevice getYaoKongYunDevice() {
        return yaoKongYunMapper.getYaoKongYunDevice();
    }

    @Override
    public void updateYaoKongDevice(String deviceId) {
        yaoKongYunMapper.updateYaoKongYunDevice(deviceId);
    }

    @Override
    public void addYaoKongDevice(TYaokonyunDevice yaokonyunDevice) {
        yaoKongYunMapper.addYaoKongYunDevice(yaokonyunDevice);
    }

    @Override
    public List<TYaokonyunKeyCode> getYaoKongKeyCodeByIndex(Integer index) {
        return yaoKongYunMapper.getYaoKongKeyCodeByIndex(index);
    }

    @Override
    public List<TYaokonyunDeviceType> getYaoKongYunDeviceType() {
        return yaoKongYunMapper.getYaoKongYunDeviceType();
    }

    @Override
    public void addTYaokonyunDeviceType(TYaokonyunDeviceType yaokonyunDeviceType) {
        yaoKongYunMapper.addTYaokonyunDeviceType(yaokonyunDeviceType);
    }

    @Override
    public List<TYaokonyunRemoteControl> getYaokonyunRemoteControlByIds() {
        return yaoKongYunMapper.getYaokonyunRemoteControlByIds();
    }

    @Override
    public void addYaokonyunRemoteControl(TYaokonyunRemoteControl yaokonyunRemoteControl) {
        yaoKongYunMapper.addYaokonyunRemoteControl(yaokonyunRemoteControl);
    }

    @Override
    public List<TYaoKongYunBrand> getYaoKongYunBrandByDeviceType(String deviceTypeId) {
        return yaoKongYunMapper.getYaoKongYunBrandByDeviceType(deviceTypeId);
    }

    @Override
    public void addTYaoKongYunBrand(TYaoKongYunBrand yaoKongYunBrand) {
        yaoKongYunMapper.addTYaoKongYunBrand(yaoKongYunBrand);
    }

    @Override
    public TYaokonyunRemoteControl getYaokonyunRemoteControlByRemoteId(String remoteId) {
        return yaoKongYunMapper.getYaokonyunRemoteControlByBrandId(remoteId);
    }

    @Override
    public void addTYaokonyunKeyCode(TYaokonyunKeyCode yaokonyunKeyCode) {
        yaoKongYunMapper.addTYaokonyunKeyCode(yaokonyunKeyCode);
    }

    @Override
    public List<TYaokonyunKeyCode> getYaoKongKeyCodeByRemoteId(Integer index) {
        return yaoKongYunMapper.getYaoKongKeyCodeByRemoteId(index);
    }

    @Override
    public void deleteTYaokonyunKeyCode(String serialId, String index) {
        yaoKongYunMapper.deleteTYaokonyunKeyCode(serialId,Integer.valueOf(index));
    }

    @Override
    public void deleteTYaokonyunKeyCodeByKeyName(String serialId, String index, String keyName,String keyType) {
        yaoKongYunMapper.deleteTYaokonyunKeyCodeByKeyName(serialId,Integer.valueOf(index),keyName,Integer.valueOf(keyType));
    }

    @Override
    public void deleteTYaokonyunKeyCodeBySerialId(String serialId) {
            yaoKongYunMapper.deleteTYaokonyunKeyCodeBySerialId(serialId);
    }

    @Override
    public List<TYaokonyunKeyCode> getYaoKongKeyCodeBySerialId(String serialId) {
        return yaoKongYunMapper.getYaoKongKeyCodeBySerialId(serialId);
    }

    @Override
    public void updateYaoKongKeyCodeNameBySerialIdAndIndex(String serialId, String index, String name) {
        yaoKongYunMapper.updateYaoKongKeyCodeNameBySerialIdAndIndex(serialId,index,name);
    }

    @Override
    public TYaokonyunKeyCode getYaoKongKeyCodeByKeyAndSerialIdAndIndex(Integer index, String serialId, String key) {
        return yaoKongYunMapper.getYaoKongKeyCodeByKeyAndSerialIdAndIndex(index,serialId,key);
    }

    @Override
    public void updateYaoKongKeyCodeNameBySerialIdAndIndexAndKey(String serialId, String index, String key, String codeSrc,Integer keyType) {
        yaoKongYunMapper.updateYaoKongKeyCodeNameBySerialIdAndIndexAndKey(serialId,Integer.valueOf(index),key,codeSrc,keyType);
    }

    @Override
    public List<Map<String, Object>> getUserIRDevice(Integer userId) {
        return yaoKongYunMapper.getUserIRDevice(userId);
    }

    @Override
    public List<TYaokonyunKeyCode> getIRDeviceByIndex(Integer index) {
        return yaoKongYunMapper.getIRDeviceByIndex(index);
    }

    @Override
    public TYaokonyunKeyCode getIRDeviceByIndexAndKey(Integer index, String key) {
        return yaoKongYunMapper.getIRDeviceByIndexAndKey(index,key);
    }

    @Override
    public List<TYaokonyunKeyCode> getYaoKongKeyCodeBySerialIdAndIndex(String serialId, Integer index) {
        return yaoKongYunMapper.getYaoKongKeyCodeBySerialIdAndIndex(index,serialId);
    }
}
