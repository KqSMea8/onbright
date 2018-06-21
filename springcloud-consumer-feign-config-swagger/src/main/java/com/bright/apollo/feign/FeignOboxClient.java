package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.hrstrix.HystrixFeignOboxFallback;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@FeignClient(name = "springcloud-provider-obox", fallback = HystrixFeignOboxFallback.class, configuration = FeignConfig.class)
public interface FeignOboxClient {

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/obox/{serialId}", method = RequestMethod.GET)
	ResponseObject<TObox> getObox(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @param obox
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/obox/{serialId}", method = RequestMethod.PUT)
	ResponseObject<TObox> updateObox(@PathVariable(value = "serialId") String serialId, @RequestBody TObox obox);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/obox/{serialId}", method = RequestMethod.DELETE)
	ResponseObject deleteObox(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId 
	 * @param obox
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/obox/addObox/{serialId}", method = RequestMethod.POST)
	ResponseObject<TObox> addObox(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TObox obox);

	/**
	 * @param id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/obox/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	ResponseObject<List<TObox>> getOboxByUserAndPage(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize);


}
