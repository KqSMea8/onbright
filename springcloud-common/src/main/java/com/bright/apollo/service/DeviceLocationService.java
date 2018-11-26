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

}
