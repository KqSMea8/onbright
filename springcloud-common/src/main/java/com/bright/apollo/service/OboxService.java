package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.service.base.BasicService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月16日  
 *@Version:1.1.0  
 */
public interface OboxService extends BasicService{

	/**  
	 * @param obox  
	 * @Description:  
	 */
	void update(TObox obox);

	/**  
	 * @param obox  
	 * @Description:  
	 */
	void deleteOboxById(TObox obox);

	/**  
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	TObox queryOboxBySerialId(String oboxSerialId);

	/**  
	 * @param obox  
	 * @Description:  
	 */
	@Deprecated
	int addObox(TObox obox);

	/**  
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	List<TObox> queryOboxByUserId(Integer userId, Integer pageIndex, Integer pageSize);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	int queryCountOboxByUserId(Integer userId);

	TObox queryOboxsByOboxSerialId(String oboxSerialId) throws Exception;

	TObox queryOboxsByDeviceChannelId(int deviceId) throws Exception;

	TObox queryOboxById(int id) throws Exception;

	List<TObox> getOboxsByDeviceChannel(int oboxDeviceId,int oboxId);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	List<TObox> queryOboxByUserId(Integer userId);


}
