package com.bright.apollo.service;

import com.bright.apollo.common.entity.TLocation;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
public interface LocationService {

	/**  
	 * @param building
	 * @param room
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	TLocation queryLocationByUserId(String building, String room, Integer userId);

	/**  
	 * @param tLocation
	 * @return  
	 * @Description:  
	 */
	int addLocation(TLocation tLocation);

	/**  
	 * @param userId
	 * @param location
	 * @return  
	 * @Description:  
	 */
	TLocation queryLocationByUserIdAndId(Integer userId, Integer location);

	/**  
	 * @param location  
	 * @Description:  
	 */
	void deleteLocation(Integer location);

	/**  
	 * @param tLocation  
	 * @Description:  
	 */
	void updateLocation(TLocation tLocation);

}
