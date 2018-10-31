package com.bright.apollo.service;

import com.bright.apollo.common.entity.TOboxDeviceConfig;

import java.util.List;

public interface OboxDeviceConfigService {

    TOboxDeviceConfig queryOboxConfigByRFAddr(int oboxId,String address);

    void updateTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig);

    TOboxDeviceConfig queryDeviceConfigBySerialID(String deviceSerialId);

    void deleteTOboxDeviceConfig(int id);

    int addTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig);

    List<TOboxDeviceConfig> getOboxDeviceConfigByOboxId(int oboxId);

    void deleteTOboxDeviceConfigByOboxId(int oboxId);

    void deleteTOboxDeviceConfigByOboxIdAndNodeAddress(int oboxId,String nodeAddress);

    List<TOboxDeviceConfig> getTOboxDeviceConfigByOboxSerialIdAndGroupAddress(String oboxId,String groupAddress);

    void updateTOboxDeviceConfigStatus(TOboxDeviceConfig tOboxDeviceConfig,String status);

    TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialId(String deviceSerialId);

    TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialIdAndAddress(String deviceSerialId,String address);

    List<TOboxDeviceConfig> getAllOboxDeviceConfig(String deviceType);

    TOboxDeviceConfig getOboxDeviceConfigById(int id);

    List<TOboxDeviceConfig> getOboxDeviceConfigByUserId(Integer userId);

	/**  
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
	List<TOboxDeviceConfig> getOboxDeviceConfigByOboxSerialId(String oboxSerialId);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	List<TOboxDeviceConfig> getDeviceTypeByUser(Integer userId);

	/**  
	 * @param userId
	 * @param deviceType
	 * @return  
	 * @Description:  
	 */
	List<TOboxDeviceConfig> getDevciesByUserIdAndType(Integer userId, String deviceType);

	/**  
	 * @param userId
	 * @param serialID
	 * @return  
	 * @Description:  
	 */
	TOboxDeviceConfig getDeviceByUserAndSerialId(Integer userId, String serialID);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	void deleteTOboxDeviceConfigByOboxSerialId(String serialId);

	/**  
	 * @param id  
	 * @Description:  
	 */
	void deleteTOboxDeviceConfigById(Integer id);

	/**  
	 * @param groupId
	 * @return  
	 * @Description:  
	 */
	List<TOboxDeviceConfig> queryDeviceByGroupId(Integer groupId);

}
