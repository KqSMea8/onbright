package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TGroupDevice;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
public interface GroupDeviceService {

	/**  
	 * @param groupId
	 * @return  
	 * @Description:  
	 */
	List<TGroupDevice> queryDeviceGroupByGroupId(Integer groupId);

	/**  
	 * @param groupId
	 * @param deviceSerialId
	 * @return  
	 * @Description:  
	 */
	TGroupDevice queryDeviceGroup(Integer groupId, String deviceSerialId);

	/**  
	 * @param groupDevice  
	 * @Description:  
	 */
	int addDeviceGroup(TGroupDevice groupDevice);

	/**  
	 * @param groupId
	 * @param deviceSerialId  
	 * @Description:  
	 */
	int deleteDeviceGroup(Integer groupId, String deviceSerialId);

}
