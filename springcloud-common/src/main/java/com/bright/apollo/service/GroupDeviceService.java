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
	 * @param id
	 * @return  
	 * @Description:  
	 */
	List<TGroupDevice> queryDeviceGroupByGroupId(Integer id);

}
