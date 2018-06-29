package com.bright.apollo.hrstrix;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月20日  
 *@Version:1.1.0  
 */
public class BasicHystrixFeignFallback {
	/**  
	 * @return  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	protected static ResponseObject serverError() {
		ResponseObject res=new ResponseObject();
		res.setStatus(ResponseEnum.MicroServiceUnConnection.getStatus());
		res.setMessage(ResponseEnum.MicroServiceUnConnection.getMsg());
		return res;
	}
	
}
