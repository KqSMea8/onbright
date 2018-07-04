package com.bright.apollo.service;

import com.bright.apollo.common.entity.TNvr;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月28日  
 *@Version:1.1.0  
 */
public interface NvrService {

	/**  
	 * @param ip
	 * @return  
	 * @Description:  
	 */
	TNvr getNvrByIP(String ip);

 

}
