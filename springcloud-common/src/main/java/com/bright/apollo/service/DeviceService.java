package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TYSCamera;
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
	@Deprecated
	TOboxDeviceConfig queryDeviceBySerialId(String serialId);

	/**  
	 * @param device  
	 * @Description:  
	 */
	@Deprecated
	void updateDeviceBySerialId(TOboxDeviceConfig device);

	/**  
	 * @param device  
	 * @Description:  
	 */
	@Deprecated
	int addDevice(TOboxDeviceConfig device);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	@Deprecated
	void deleteDeviceBySerialId(String serialId);

	/**  
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	List<TOboxDeviceConfig> queryDeviceByUserId(Integer userId, Integer pageIndex, Integer pageSize);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	int queryCountDeviceByUserId(Integer userId);

 

}
