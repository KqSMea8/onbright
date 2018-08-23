package com.bright.apollo.service.impl;

import java.util.List;

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

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserDeviceService#getUserDeviceByUserIdAndSerialId(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public TUserDevice getUserDeviceByUserIdAndSerialId(Integer userId, String device_serial_id) {
		return userDeviceMapper.getUserDeviceByUserIdAndSerialId(userId,device_serial_id);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserDeviceService#getUserDevicesBySerialId(java.lang.String)  
	 */
	@Override
	public List<TUserDevice> getUserDevicesBySerialId(String serialId) {
 		return userDeviceMapper.queryUserDevicesBySerialId(serialId);
	}
}
