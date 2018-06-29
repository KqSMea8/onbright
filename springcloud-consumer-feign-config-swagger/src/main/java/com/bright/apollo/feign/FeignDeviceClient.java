package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.hrstrix.HystrixFeignDeviceFallback;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月2日  
 *@Version:1.1.0  
 */
@FeignClient(value = "springcloud-provider-device",fallback = HystrixFeignDeviceFallback.class, configuration = FeignConfig.class)

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
	ResponseObject<TOboxDeviceConfig> updateDevice(@PathVariable(value = "serialId") String serialId,@RequestBody TOboxDeviceConfig device);

	/**  
	 * @param serialId
	 * @param device  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.POST)
	ResponseObject<TOboxDeviceConfig> addDevice(@PathVariable(value = "serialId") String serialId,@RequestBody TOboxDeviceConfig device);

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

	@RequestMapping(value = "/device/getOboxDeviceConfigByUserId/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getOboxDeviceConfigByUserId(@PathVariable(value = "userId") Integer userId);

	/**  
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/getDevicesByOboxSerialId/{oboxSerialId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDevicesByOboxSerialId(@PathVariable(value = "oboxSerialId") String oboxSerialId);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/getDeviceByUser/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDeviceByUser(@PathVariable(value = "userId") Integer userId);

	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/getYSCameraBySerialId/{deviceSerialId}", method = RequestMethod.GET)
	ResponseObject<TYSCamera> getYSCameraBySerialId(@PathVariable(value = "deviceSerialId") String deviceSerialId);

	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/getNvrByIP/{ip}", method = RequestMethod.GET)
	ResponseObject<TNvr> getNvrByIP(@PathVariable(value = "ip") String ip);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/getDeviceTypeByUser/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDeviceTypeByUser(@PathVariable(value = "userId") Integer userId);

	/**  
	 * @param id
	 * @param deviceType
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/getDevciesByUserIdAndType/{userId}/{deviceType}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDevciesByUserIdAndType(@PathVariable(value = "userId") Integer userId,@PathVariable(value = "deviceType") String deviceType);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/device/getYSCameraByUserId/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TYSCamera>> getYSCameraByUserId(@PathVariable(value = "userId") Integer userId);


}
