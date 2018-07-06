package com.bright.apollo.service;

import com.bright.apollo.common.entity.TUserDevice;

public interface UserDeviceService {
   void deleteUserDevice(String id);

/**  
 * @param userId
 * @param device_serial_id  
 * @Description:  
 */
int addUserDevice(TUserDevice tUserDevice);
}
