package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.hrstrix.HystrixFeignDeviceFallback;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月2日  
 *@Version:1.1.0  
 */
@FeignClient(name = "springcloud-provider-deivce",fallback = HystrixFeignDeviceFallback.class, configuration = FeignConfig.class)
public interface FeignDeviceClient {
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.GET)
	ResponseObject<TOboxDeviceConfig> getDevice(@PathVariable(value = "serialId")  String serialId);

	/**  
	 * @param serialId
	 * @param device
	 * @return  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.PUT)
	ResponseObject updateDevice(@PathVariable(value = "serialId") String serialId,@RequestBody TOboxDeviceConfig device);

	/**  
	 * @param serialId
	 * @param device  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.POST)
	ResponseObject addDevice(@PathVariable(value = "serialId") String serialId,@RequestBody TOboxDeviceConfig device);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.DELETE)
	ResponseObject delDevice(@PathVariable(value = "serialId") String serialId);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/deleteByOboxSerialId/{serialId}", method = RequestMethod.DELETE)
	ResponseObject deleleDeviceByOboxSerialId(@PathVariable(value = "serialId") String serialId);

	/**  
	 * @param id
	 * @param pageIndex
	 * @param pageSize
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(@PathVariable(value = "userId") Integer userId,@PathVariable(value = "pageIndex") Integer pageIndex,@PathVariable(value = "pageSize") Integer pageSize);
	 

}
