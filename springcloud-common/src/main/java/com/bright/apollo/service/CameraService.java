package com.bright.apollo.service;

import com.bright.apollo.common.entity.TYSCamera;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月28日  
 *@Version:1.1.0  
 */
public interface CameraService {

	/**  
	 * @param deviceSerialId
	 * @return  
	 * @Description:  
	 */
	TYSCamera getYSCameraBySerialId(String deviceSerialId);

}
