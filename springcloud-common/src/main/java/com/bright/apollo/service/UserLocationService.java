package com.bright.apollo.service;

import java.util.List;

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

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	List<TUserLocation> queryUserLocationByUser(Integer userId);

	/**  
	 * @param userId
	 * @param location
	 * @return  
	 * @Description:  
	 */
	TUserLocation queryUserLocationByUserIdAndLocation(Integer userId, Integer location);

}
