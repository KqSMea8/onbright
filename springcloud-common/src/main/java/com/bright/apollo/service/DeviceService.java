package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.service.base.BasicService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月16日  
 *@Version:1.1.0  
 */
public interface DeviceService extends BasicService{

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	TOboxDeviceConfig queryDeviceBySerialId(String serialId);

	/**  
	 * @param device  
	 * @Description:  
	 */
	void updateDeviceBySerialId(TOboxDeviceConfig device);

	/**  
	 * @param device  
	 * @Description:  
	 */
	int addDevice(TOboxDeviceConfig device);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	void deleteDeviceBySerialId(String serialId);

	/**  
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return  
	 * @Description:  
	 */
	List<TOboxDeviceConfig> queryDeviceByUserId(Integer userId, Integer pageIndex, Integer pageSize);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	int queryCountDeviceByUserId(Integer userId);

}
