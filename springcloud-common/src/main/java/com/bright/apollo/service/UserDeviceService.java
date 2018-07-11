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

/**  
 * @param userId
 * @param device_serial_id
 * @return  
 * @Description:  
 */
TUserDevice getUserDeviceByUserIdAndSerialId(Integer userId, String device_serial_id);
}
