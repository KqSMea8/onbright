package com.bright.apollo.service.impl;


import com.bright.apollo.common.entity.TYaoKongYunBrand;
import com.bright.apollo.common.entity.TYaokonyunDevice;
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
    public List<TYaoKongYunBrand> getYaoKongYunByTIdAndDeviceType(String tId, String deviceType) {
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
}
