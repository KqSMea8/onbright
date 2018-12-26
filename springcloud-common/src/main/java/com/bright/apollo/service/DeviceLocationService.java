package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TDeviceLocation;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
public interface DeviceLocationService {

	/**  
	 * @param tDeviceLocation  
	 * @Description:  
	 */
	int addDeviceLocation(TDeviceLocation tDeviceLocation);

	/**  
	 * @param location
	 * @return  
	 * @Description:  
	 */
	List<TDeviceLocation> queryDevicesByLocation(Integer location);

	/**  
	 * @param id  
	 * @Description:  
	 */
	void deleteDeviceLocation(Integer id);

	/**  
	 * @param location
	 * @param serialId
	 * @param type
	 * @return  
	 * @Description:  
	 */
	TDeviceLocation queryDevicesByLocationAndSerialIdAndType(Integer location, String serialId, String type);

	/**  
	 * @param location2  
	 * @Description:  
	 */
	void updateDeviceLocation(TDeviceLocation location2);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	List<TDeviceLocation> queryDevicesByUserId(Integer userId);

	/**  
	 * @param userName
	 * @return  
	 * @Description:  
	 */
	List<TDeviceLocation> queryDevicesByUserName(String userName);

	/**  
	 * @param serialId
	 * @param userName
	 * @return  
	 * @Description:  
	 */
	TDeviceLocation queryLocationDeviceBySerialIdAndUserName(String serialId, String userName);

}
