package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TUserAliDevice;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月17日  
 *@Version:1.1.0  
 */
public interface UserAliDevService {

	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	void deleteUserAliDev(String deviceSerialId);

	/**  
	 * @param tUserAliDev  
	 * @Description:  
	 */
	void addUserAliDev(TUserAliDevice tUserAliDev);

	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	List<TUserAliDevice> queryAliUserId(String deviceSerialId);

	TUserAliDevice queryAliDeviceBySerialiId(String deviceSerialId);

}
