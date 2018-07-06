package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.dao.user.mapper.TUserDeviceMapper;
import com.bright.apollo.service.UserDeviceService;
@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    @Autowired
    private TUserDeviceMapper userDeviceMapper;

    @Override
    public void deleteUserDevice(String id) {
        userDeviceMapper.deleteTUserDevice(id);
    }

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserDeviceService#addUserDevice(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public int addUserDevice(TUserDevice tUserDevice) {
		   
		return userDeviceMapper.addUserDevice(tUserDevice);
	}
}
