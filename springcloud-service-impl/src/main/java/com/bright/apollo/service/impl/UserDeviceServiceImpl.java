package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.dao.user.mapper.TUserDeviceMapper;
import com.bright.apollo.service.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDeviceServiceImpl implements UserDeviceService {

    @Autowired
    private TUserDeviceMapper userDeviceMapper;

    @Override
    public void deleteUserDevice(String id) {
        userDeviceMapper.deleteTUserDevice(id);
    }
}
