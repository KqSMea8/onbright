package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TDeviceStatus;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月28日  
 *@Version:1.1.0  
 */
public interface DeviceStatusService {

	/**  
	 * @param serialId
	 * @param start
	 * @param count
	 * @return  
	 * @Description:  
	 */
	List<TDeviceStatus> queryDeviceStatusByCount(String serialId, int start, int count);

	/**  
	 * @param serialId
	 * @param from
	 * @param to
	 * @return  
	 * @Description:  
	 */
	List<TDeviceStatus> queryDeviceStatusByData(String serialId, long from, long to);

	/**  
	 * @param serialId
	 * @param from
	 * @param to
	 * @return  
	 * @Description:  
	 */
	List<TDeviceStatus> queryDeviceStatusByDataNoGroup(String serialId, long from, long to);

	/**  
	 * @param tDeviceStatus  
	 * @Description:  
	 */
	void addDeviceStatus(TDeviceStatus tDeviceStatus);

}
