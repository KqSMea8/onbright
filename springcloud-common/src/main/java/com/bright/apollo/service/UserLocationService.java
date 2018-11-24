package com.bright.apollo.service;

import com.bright.apollo.common.entity.TUserLocation;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
public interface UserLocationService {

	/**  
	 * @param tUserLocation  
	 * @Description:  
	 */
	int addUserLocation(TUserLocation tUserLocation);

	/**  
	 * @param location  
	 * @Description:  
	 */
	void deleteUserLocation(Integer location);

}
