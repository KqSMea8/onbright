package com.bright.apollo.service.impl;


import com.bright.apollo.common.entity.*;
import com.bright.apollo.dao.device.mapper.YaoKongYunMapper;
import com.bright.apollo.service.YaoKongYunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
